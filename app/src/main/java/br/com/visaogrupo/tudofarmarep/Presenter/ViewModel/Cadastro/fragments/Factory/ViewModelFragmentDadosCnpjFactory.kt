package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CnpjUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosCnpj
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CnpjRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class ViewModelFragmentDadosCnpjFactory ( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFragmentDadosCnpj::class.java)) {

            val cnpjRepository = CnpjRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val cnpjUseCase = CnpjUseCase(cnpjRepository, preferenciasUtils)
            val cadastroRepository = CadastroRepository(context)
            val sistemaUtils = SistemaUtils(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository, preferenciasUtils,sistemaUtils)
            return ViewModelFragmentDadosCnpj(cnpjUseCase, cadastroUseCase,sistemaUtils) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}