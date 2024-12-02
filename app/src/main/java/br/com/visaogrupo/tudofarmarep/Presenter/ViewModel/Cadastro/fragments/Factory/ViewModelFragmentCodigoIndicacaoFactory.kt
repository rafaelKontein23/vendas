package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CodigoIndicaoUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentCodigoIndicacao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CodigoIndicacaoRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.DadosPessoaisRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class ViewModelFragmentCodigoIndicacaoFactory ( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFragmentCodigoIndicacao::class.java)) {
            val codigoIndicacaoRepository = CodigoIndicacaoRepository(context)
            val codigoIndicaoUseCase = CodigoIndicaoUseCase(codigoIndicacaoRepository)
            val cadastroRepository = CadastroRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val sistemaUtils = SistemaUtils(context)
            val dadosPessoaisRepository = DadosPessoaisRepository(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository, preferenciasUtils,sistemaUtils, dadosPessoaisRepository)
            val suporteTelefoneReposytory = SuporteTelefoneReposytory(context)
            return ViewModelFragmentCodigoIndicacao(codigoIndicaoUseCase, suporteTelefoneReposytory, cadastroUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}