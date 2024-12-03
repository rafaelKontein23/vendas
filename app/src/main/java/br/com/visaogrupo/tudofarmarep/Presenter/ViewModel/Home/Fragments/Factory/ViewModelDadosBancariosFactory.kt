package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.DadosBancariosUseCase

import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.DadosPessoaisRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.DadosBancariosRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class ViewModelDadosBancariosFactory (private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelDadosBancarios::class.java)) {
            val dadosBancariosRepository = DadosBancariosRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val  cadastroRepository = CadastroRepository(context)
            val sistemaUtils = SistemaUtils(context)
            val dadosPessoais = DadosPessoaisRepository(context)
            val cadastroUseCase = CadastroUseCase(cadastroRepository, preferenciasUtils, sistemaUtils, dadosPessoais)
            val dadosBancariosUseCase = DadosBancariosUseCase(dadosBancariosRepository, preferenciasUtils)

            return ViewModelDadosBancarios(dadosBancariosUseCase, preferenciasUtils, cadastroUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}