package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.AreaDeAtuacaoUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.AreaDeAtuacaoRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class ViewModelFragmentDadosAreaDeAtuacaoFactory( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFragmentDadosAreaDeAtuacao::class.java)) {
            val areaDeAtuacaoRepository = AreaDeAtuacaoRepository(context)
            val areaDeAtuacaoUseCase = AreaDeAtuacaoUseCase(areaDeAtuacaoRepository)
            val cadastroRepository = CadastroRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val sistemaUtils = SistemaUtils(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository, preferenciasUtils,sistemaUtils)
            return ViewModelFragmentDadosAreaDeAtuacao(areaDeAtuacaoUseCase, cadastroUseCase, preferenciasUtils) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}