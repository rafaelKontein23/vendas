package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelContratoAceite
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository

class ViewModelFragmentContratoAceiteFactory ( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelContratoAceite::class.java)) {
            val cadastroRepository = CadastroRepository(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository)
            return ViewModelContratoAceite(cadastroUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}