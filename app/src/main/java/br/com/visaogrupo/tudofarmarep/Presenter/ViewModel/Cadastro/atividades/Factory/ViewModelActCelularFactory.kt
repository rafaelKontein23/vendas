package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCelular
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelActCelularFactory ( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelActCelular::class.java)) {
            val repository = SuporteTelefoneReposytory(context)
            val salavarTexto = PreferenciasUtils(context)
            return ViewModelActCelular(repository, salavarTexto, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}