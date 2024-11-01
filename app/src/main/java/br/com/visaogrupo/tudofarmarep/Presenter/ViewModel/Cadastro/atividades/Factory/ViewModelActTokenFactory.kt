package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActToken
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.TokenRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelActTokenFactory (   private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelActToken::class.java)) {
            val repository = SuporteTelefoneReposytory(context)
            val tokenRepository = TokenRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            return ViewModelActToken(repository, tokenRepository, preferenciasUtils) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}