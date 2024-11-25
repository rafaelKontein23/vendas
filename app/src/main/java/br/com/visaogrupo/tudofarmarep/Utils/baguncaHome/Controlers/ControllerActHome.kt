package br.com.visaogrupo.tudofarmarep.Controlers

import android.content.Context
import android.preference.PreferenceManager
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskMenuLateral
import br.com.visaogrupo.tudofarmarep.Objetos.Menulateral
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class ControllerActHome {

    suspend fun buscaMenuLateral(context: Context): ArrayList<Menulateral> {
        var listaMenulateral = ArrayList<Menulateral>()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        val reprsentanteID = prefs.getInt("reprsentante_id", 0)


        withContext(Dispatchers.IO){
            val tarefaMenu = async {
                val taskMenu = TaskMenuLateral()
                listaMenulateral = taskMenu.buscaItensMenu(reprsentanteID)
            }
            tarefaMenu.await()
        }
        return listaMenulateral
    }
}