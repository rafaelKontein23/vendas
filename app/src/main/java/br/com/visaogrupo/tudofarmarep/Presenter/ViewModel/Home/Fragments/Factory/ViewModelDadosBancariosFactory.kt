package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.DadosBancariosUseCase

import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.DadosBancariosRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelDadosBancariosFactory (private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelDadosBancarios::class.java)) {
            val dadosBancariosRepository = DadosBancariosRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val dadosBancariosUseCase = DadosBancariosUseCase(dadosBancariosRepository, preferenciasUtils)

            return ViewModelDadosBancarios(dadosBancariosUseCase, preferenciasUtils) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}