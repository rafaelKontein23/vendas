package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.AreaDeAtuacaoUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CodigoIndicaoUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentCodigoIndicacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.AreaDeAtuacaoRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CodigoIndicacaoRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelFragmentCodigoIndicacaoFactory ( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFragmentCodigoIndicacao::class.java)) {
            val codigoIndicacaoRepository = CodigoIndicacaoRepository(context)
            val codigoIndicaoUseCase = CodigoIndicaoUseCase(codigoIndicacaoRepository)
            val cadastroRepository = CadastroRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository, preferenciasUtils)
            val suporteTelefoneReposytory = SuporteTelefoneReposytory(context)
            return ViewModelFragmentCodigoIndicacao(codigoIndicaoUseCase, suporteTelefoneReposytory, cadastroUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}