package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosPessoais
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelFragmentDadosPessoalFactory  (
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFragmentDadosPessoais::class.java)) {

            val preferenciasUtils = PreferenciasUtils(context)
            val cadastroRepository = CadastroRepository(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository, preferenciasUtils)
            return ViewModelFragmentDadosPessoais(preferenciasUtils, cadastroUseCase ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}