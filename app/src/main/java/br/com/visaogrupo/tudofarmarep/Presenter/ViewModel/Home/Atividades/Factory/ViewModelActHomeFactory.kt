package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.ViewModelActHome
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelActHomeFactory  (private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelActHome::class.java)) {
            val repository = SuporteTelefoneReposytory(context)
            val salavarTexto = PreferenciasUtils(context)
            return ViewModelActHome(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}