package br.com.visaogrupo.tudofarmarep.Carga.ultis

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskFormaDePagamento
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskLojas
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskZip
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskZipProgressiva
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksMarcas
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaCargaProgresso
import br.com.visaogrupo.tudofarmarep.DAO.DAOFiltros
import br.com.visaogrupo.tudofarmarep.DAO.DAOFormaPagamento
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.DAO.DAOLojas
import br.com.visaogrupo.tudofarmarep.DAO.DAOMarcas
import br.com.visaogrupo.tudofarmarep.DAO.DAOProdutos
import br.com.visaogrupo.tudofarmarep.DAO.DAOProgressiva
import br.com.visaogrupo.tudofarmarep.Objetos.FiltroProduto
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Marcas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Objetos.Progressiva
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

class Requests {
    var listaMarcas = emptyList<Marcas>()
    fun corrotinasMarcas(context: Context, atualizaCargaProgresso: AtualizaCargaProgresso){
        val daoHelper = DAOHelper(context).writableDatabase
        val daoLoja = DAOLojas(context)
        daoLoja.deletar(daoHelper)
       CoroutineScope(Dispatchers.IO).launch {
            try {
                var path = ""
                var pathProgressiva = ""
                var error = false



                val tarefaZip =  launch {
                    var taskZip = TaskZip()
                    path = taskZip.buscaZipProdutos(context)
                    error = path.isEmpty()
                }

                val tarefaBaixaPrgressiva = launch {
                    var taskBaixaProgressiva = TaskZipProgressiva()
                    pathProgressiva=    taskBaixaProgressiva.buscaZipProgressiva(context,"SP")
                    error = pathProgressiva.isEmpty()
                }

                val tarefaFormaDePagamento = launch {
                    val taskFormaDePagamento = TaskFormaDePagamento()
                    val listaFormaDePagamento = taskFormaDePagamento.buscaFormaDePagamento()
                    error = listaFormaDePagamento.isEmpty()

                    for (formaDePagamento in listaFormaDePagamento){
                        var daoFormaDePagamento = DAOFormaPagamento()
                        daoFormaDePagamento.inserirFormaPagamento(daoHelper,formaDePagamento)
                    }
                }
                val tarefaMarcas = launch {
                    var tasksMarcas = TasksMarcas()
                    listaMarcas = tasksMarcas.buscaMarcas(context)
                    error = listaMarcas.isEmpty()

                    for (marcas in listaMarcas){
                        var daoMarcas = DAOMarcas(context)
                        daoMarcas.inserir(daoHelper,marcas)
                    }

                }
                var tarefaLoja = launch {
                    var tasksLojas = TaskLojas()
                    val jsonLojasArray = tasksLojas.buscaLojas()
                    error = jsonLojasArray.length() == 0

                    for (i in 0 until jsonLojasArray.length()){
                        val jsonLoja = jsonLojasArray.getJSONObject(i)
                        val macaID = jsonLoja.getInt("Marca_ID")
                        val jsonLojasItens = jsonLoja.getJSONArray("Lojas")
                        for (j in 0 until jsonLojasItens.length()) {
                            val jsonLojaItem = jsonLojasItens.getJSONObject(j)
                            val Loja_ID = jsonLojaItem.getInt("Loja_ID")
                            val Nome = jsonLojaItem.getString("Nome")
                            val LojaTipo_ID = jsonLojaItem.getInt("LojaTipo_ID")
                            val ValorMinimo = jsonLojaItem.getDouble("ValorMinimo")
                            val ValorMaximo = jsonLojaItem.getDouble("ValorMaximo")
                            val QtdeMinOperador = jsonLojaItem.getInt("QtdeMinOperador")
                            val QtdeMaxOperador = jsonLojaItem.getInt("QtdeMaxOperador")
                            val QtdeMaxVendas = jsonLojaItem.getInt("QtdeMaxVendas")
                            val Loja_id_Portal = jsonLojaItem.getInt("Loja_id_Portal")
                            val loja = Lojas(LojaTipo_ID, Loja_ID, Loja_id_Portal, Nome, QtdeMaxOperador, QtdeMaxVendas, QtdeMinOperador,ValorMinimo, ValorMaximo, macaID  ,0)
                            val daoLoja = DAOLojas(context)
                            daoLoja.inserir(daoHelper,loja)
                        }
                    }

                }
                tarefaZip.join()
                tarefaFormaDePagamento.join()
                tarefaMarcas.join()
                tarefaLoja.join()
                tarefaBaixaPrgressiva.join()
                if (error){
                    atualizaCargaProgresso.atualizaCargaProgresso(3)
                    return@launch
                }

                val tarefaGravaBanco = launch {
                    var dao = DAOHelper(context).writableDatabase
                    var daoProdutos =  DAOProdutos()
                    daoProdutos.drop(dao)

                    for (i in listaMarcas){
                        var json = LerJson().readTextFileFromZip(path,"Produtos_"+i.Marca_ID+".json","")
                        if (json != null){
                            var jsonArray = JSONArray(JSONObject(json).getString("Produtos"))

                            for (j in 0 until jsonArray.length()){
                                var jsonItemProduto = jsonArray.getJSONObject(j)
                                val barra  = jsonItemProduto.getString("Barra")
                                val  nome = jsonItemProduto.getString("Nome")
                                val  marcaID  = jsonItemProduto.getInt("Marca_ID")
                                val  imagem  = jsonItemProduto.getString("Imagem")

                                val produto = Produtos(barra,marcaID,nome,imagem, lojaID = 1, cnpj = "")


                                val jsonArrayItemFiltro = if (jsonItemProduto.has("Filtros")) {
                                    JSONArray(jsonItemProduto.getString("Filtros"))
                                } else {
                                    JSONArray()
                                }
                               val lerProdutos = launch {
                                   daoProdutos.inserir(daoHelper,produto)

                               }

                                val lerFiltro =launch {
                                    val daoFiltro = DAOFiltros(context)
                                    for (k in 0 until jsonArrayItemFiltro.length()){
                                        var jsonItemFiltro = jsonArrayItemFiltro.getJSONObject(k)
                                        var categoriaID= jsonItemFiltro.getInt("Categoria_id")
                                        var AtributoId = jsonItemFiltro.getInt("Atributo_id")
                                        val filtroProduto = FiltroProduto(AtributoId,barra,categoriaID);
                                        daoFiltro.inserir(daoHelper, filtroProduto)
                                    }
                                }
                                lerFiltro.join()
                                lerProdutos.join()
                            }
                            Log.d("json",jsonArray.toString())
                        }

                    }

                }
                val tarefaGravaProgresiva = launch {
                     val lerJson = LerJson()
                     val progressivasJson = lerJson.readAllJsonFilesFromZip(pathProgressiva)
                    Log.d("listafIM", listaMarcas.toString())

                    for ( itens in progressivasJson){
                        var jsonArraysProgressiva = JSONArray(itens.getString("Progressiva"))
                        for (i in 0 until jsonArraysProgressiva.length()) {
                            var jsonItesnProgressiva = jsonArraysProgressiva.getJSONObject(i)
                            var OoeradorLogisticoGrupoID = jsonItesnProgressiva.getInt("OperadorLogistico_Grupo_ID")
                            var Imagem = jsonItesnProgressiva.getString("Imagem")
                            var Nome = jsonItesnProgressiva.getString("Nome")
                            var jsonArrayProgressiva  = JSONArray(jsonItesnProgressiva.getString("Progressiva"))
                            for (j in 0 until jsonArrayProgressiva.length()){
                                var jsonItemProgressiva = jsonArrayProgressiva.getJSONObject(j)
                                val progressiva = Progressiva(
                                    lojaID = jsonItemProgressiva.getInt("Loja_ID"),
                                    barra = jsonItemProgressiva.getString("Barra"),
                                    quantidadePedido = jsonItemProgressiva.getInt("QuantidadePedido"),
                                    valorUnitario = jsonItemProgressiva.getDouble("ValorUnitario"),
                                    desconto = jsonItemProgressiva.getDouble("Desconto"),
                                    valorUnitarioDesconto = jsonItemProgressiva.getDouble("ValorUnitarioDesconto"),
                                    valorTotalDesconto = jsonItemProgressiva.getDouble("ValorTotalDesconto"),
                                    marcaID = jsonItemProgressiva.getInt("Marca_id"),
                                    lojaIDPortal = jsonItemProgressiva.getInt("Loja_id_Portal"),
                                    quantidadeMaxima = jsonItemProgressiva.getInt("QuantidadeMaxima"),
                                    uf = jsonItemProgressiva.getString("UF"),
                                    origem = jsonItemProgressiva.getString("Origem"),
                                    arquivoPreco = jsonItemProgressiva.getString("ArquivoPreco"),
                                    imagem = Imagem,
                                    nome = Nome,
                                    operadorLogisticoGrupoID = OoeradorLogisticoGrupoID,
                                    comissao = jsonItemProgressiva.getDouble("Comissao"),
                                    comissaoTotal = 0.0,
                                    stUnitario =  if(jsonItemProgressiva.has("STUnitario") ) jsonItemProgressiva.getDouble("STUnitario") else 0.0 )
                                if (progressiva.barra == "6910021007206"){
                                    Log.d("daad", "faasf")
                                }
                                val daoProgressiva = DAOProgressiva()
                                daoProgressiva.inserir(daoHelper,progressiva)

                            }
                        }
                    }

                }
                tarefaGravaBanco.join()



               runBlocking {
                  tarefaGravaProgresiva.join()

               }
                Log.d("listafIM", listaMarcas.toString())
                Log.d("caminho", path)
                daoHelper.close()
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = prefs.edit()
                editor.putBoolean("fazendoCarga", false)
                editor.apply()
                atualizaCargaProgresso.atualizaCargaProgresso(2)

            }catch (e:Exception){
                e.printStackTrace()
                atualizaCargaProgresso.atualizaCargaProgresso(3)
            }

        }
    }
}