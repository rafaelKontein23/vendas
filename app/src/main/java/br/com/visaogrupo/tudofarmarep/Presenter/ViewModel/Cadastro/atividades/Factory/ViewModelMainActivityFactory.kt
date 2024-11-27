package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.LoginUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.LoginRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class ViewModelMainActivityFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMainActivity::class.java)) {

            val repository = SuporteTelefoneReposytory(context)
            val salvaTextos = PreferenciasUtils(context)
            val loginRepository = LoginRepository(context)
            val sistemaUtils = SistemaUtils(context)
            val loginUseCase = LoginUseCase(loginRepository, salvaTextos, sistemaUtils)
            return ViewModelMainActivity(repository, salvaTextos, loginUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}