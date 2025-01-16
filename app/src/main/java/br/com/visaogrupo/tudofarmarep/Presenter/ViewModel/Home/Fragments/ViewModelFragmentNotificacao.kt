package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.NotificacaoUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaNotificacao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentNotificacao (
    private val notificacaoUseCase: NotificacaoUseCase
): ViewModel(){
    val listaNotificacao = MutableLiveData<ArrayList<RespostaNotificacao>>()
    val _listaNotificacap get() = listaNotificacao

    fun buscanotificacao (){
        CoroutineScope(Dispatchers.IO).launch {
            val listaresultado =notificacaoUseCase.buscaNotificacao()
            listaNotificacao.postValue(listaresultado)

        }

    }

}