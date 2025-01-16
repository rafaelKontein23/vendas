package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.NotificacaoUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelFragmentNotificacao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.NotificacaoRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelFragmentNotificacaoFactory( private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFragmentNotificacao::class.java)) {
            val reposytory = NotificacaoRepository(context)
            val preferenciasUtils = PreferenciasUtils(context)
            val viewModel = NotificacaoUseCase(reposytory, preferenciasUtils)

            return ViewModelFragmentNotificacao(viewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}