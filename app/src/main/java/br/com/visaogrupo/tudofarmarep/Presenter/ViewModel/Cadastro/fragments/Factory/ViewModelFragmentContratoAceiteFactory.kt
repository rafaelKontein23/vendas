package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelContratoAceite
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelFragmentContratoAceiteFactory ( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelContratoAceite::class.java)) {
            val cadastroRepository = CadastroRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository, preferenciasUtils)
            return ViewModelContratoAceite(cadastroUseCase, preferenciasUtils) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}