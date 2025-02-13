package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActSucessoCadastro
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory

class ViewModelActSucessoCadastroFactory  (   private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelActSucessoCadastro::class.java)) {
            val suporteTelefoneRepository = SuporteTelefoneReposytory(context)

            return ViewModelActSucessoCadastro(suporteTelefoneRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}