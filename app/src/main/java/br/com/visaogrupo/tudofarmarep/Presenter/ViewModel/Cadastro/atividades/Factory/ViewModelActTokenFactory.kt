package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.LoginUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.TokenUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActToken
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.LoginRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.TokenRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.PushFirebase
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class ViewModelActTokenFactory (   private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelActToken::class.java)) {
            val suporteTelefoneRepository = SuporteTelefoneReposytory(context)
            val tokenRepository = TokenRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val sistemaUtils = SistemaUtils(context)

            val tokenUseCase = TokenUseCase(
                tokenRepository,
                preferenciasUtils,
                sistemaUtils
            )
            val loginRepository = LoginRepository(context)
            val loginUseCase = LoginUseCase(
                loginRepository,
                preferenciasUtils,
                sistemaUtils
            )

            return ViewModelActToken(suporteTelefoneRepository,tokenUseCase, preferenciasUtils, loginUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}