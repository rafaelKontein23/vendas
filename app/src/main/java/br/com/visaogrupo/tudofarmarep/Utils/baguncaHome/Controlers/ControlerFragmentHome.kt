package br.com.visaogrupo.tudofarmarep.Controlers

import ResumoMes
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskMenuLateral
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskBanners
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskCotacao
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskGraficoHome
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskPedidoPendentes
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskResumoMes
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskVericaCarga
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaCargaProgresso
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.RecuperasDatas
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.Objetos.ListasHome
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Requests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ControlerFragmentHome {

  suspend  fun buscaInfosIniciais (context:Context): ListasHome{
        var listaHome = ListasHome()
        val dataFormat =SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataAtual = dataFormat.format(Date())
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val reprsentanteID = prefs.getInt(ProjetoStrings.reprenteID, 0)

        withContext(Dispatchers.IO) {
            try {
                val tarefasBuscaBanners = async {
                    val taskBanners = TaskBanners()
                    listaHome.banners = taskBanners.buscaBanners(reprsentanteID)
                }

                val tarefaBuscaCotacao = async {
                    val taskCotacao = TaskCotacao()
                     listaHome.listaCotacao = taskCotacao.buscaCotacao(reprsentanteID)
                }
               val tarefasGraficosHome = async {
                    val taskGraficosHome = TaskGraficoHome()

                    listaHome.graficosHome = taskGraficosHome.buscaGraficoHome(reprsentanteID, data = dataAtual)
                }
                val tarefaPedidoPendentes = async {
                      val daoHelper = br.com.visaogrupo.tudofarmarep.DAO.DAOHelper(context).writableDatabase

                      val DAOCarrinho = DAOCarrinho()
                      listaHome.pedidosPendentes = DAOCarrinho.buscaCarrinhoAbertos(daoHelper)

                }
                val tarefaResumoGanhos = async {
                      val taskResumoMes = TaskResumoMes()
                      listaHome.resumoMes = taskResumoMes.buscaResumoMes(representanteID =  reprsentanteID, data = dataAtual)

                }

                tarefaResumoGanhos.await()
                tarefaBuscaCotacao.await()
                tarefaPedidoPendentes.await()
                tarefasGraficosHome.await()
                tarefasBuscaBanners.await()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        return listaHome
    }

   fun verificaAtualizacaoCarga(context: Context, atualizaCargaProgresso: AtualizaCargaProgresso){
       val taskVericaCarga = TaskVericaCarga()
      val fazCarga =  taskVericaCarga.verificaAtualizacaoCarga(context)
       if (fazCarga){
           val prefs = PreferenceManager.getDefaultSharedPreferences(context)
           val editor = prefs.edit()
           editor.putBoolean("fazendoCarga", true)
           editor.apply()
           atualizaCargaProgresso.atualizaCargaProgresso(1)
           val request = Requests()
           request.corrotinasMarcas(context, atualizaCargaProgresso)
           MainScope().launch {
               Alertas.alertaErro(context, "Valores dos produtos serão atualizados em alguns instantes.", "Atualização"){

               }
           }
       }
   }
  suspend fun  buscaResumoMes(datSelecionada:String,context: Context):ArrayList<ResumoMes>{
      val listaResumoMes = ArrayList<ResumoMes>()
      val prefs = PreferenceManager.getDefaultSharedPreferences(context)
      val reprsentanteID = prefs.getInt(ProjetoStrings.reprenteID, 0)
      withContext(Dispatchers.IO){
          val tarefaBuscaMes = async {
              val taskResumoMes = TaskResumoMes()
              val  listaResumoMesTask = taskResumoMes.buscaResumoMes(data = datSelecionada, representanteID = reprsentanteID)
              listaResumoMes.addAll(listaResumoMesTask)
          }

          tarefaBuscaMes.await()

      }
      return listaResumoMes
  }

}