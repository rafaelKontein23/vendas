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
    val listaNotificacaoLidas = MutableLiveData<ArrayList<RespostaNotificacao>>()
    val _listaNotificacaoLista get() = listaNotificacaoLidas
    val listaNotificacaoNaoLidas = MutableLiveData<ArrayList<RespostaNotificacao>>()
    val _listaNotificacaoNaoLista get() = listaNotificacaoNaoLidas

    fun buscanotificacao (pushsId:Int = 0, lerTodas:Int = 0){
        CoroutineScope(Dispatchers.IO).launch {
            val listaresultado =notificacaoUseCase.buscaNotificacao(pushsId, lerTodas)
            if(listaresultado.isNotEmpty()){
                val listaLidas = listaresultado.filter { it -> it.Lido }
                val listaNaoLidas = listaresultado.filter { it -> !it.Lido }
                _listaNotificacaoNaoLista.postValue(listaNaoLidas as ArrayList<RespostaNotificacao>)
                _listaNotificacaoLista.postValue(listaLidas as ArrayList<RespostaNotificacao>)

            }else{
                _listaNotificacaoLista.postValue(arrayListOf())
                _listaNotificacaoNaoLista.postValue(arrayListOf())
            }

        }

    }

}