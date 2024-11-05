package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelActCabecalho :ViewModel(){
    private val _mostraCareegando = MutableLiveData<Boolean>()
    val mostraCarregando: LiveData<Boolean> get() = _mostraCareegando
    private val _finalizaAtividade = MutableLiveData<Boolean>()
    val finalizaAtividade: LiveData<Boolean> get() = _finalizaAtividade
    private val _passoAtual = MutableLiveData<Pair<Int, Float>>()
    val passoAtual: LiveData<Pair<Int, Float>> get() = _passoAtual


    fun mostraCarregando(mostra: Boolean){
        _mostraCareegando.postValue(mostra)
    }
    fun finalizaAtividade(){
        _finalizaAtividade.postValue(true)

    }

    fun mudaProgressoCadastro(passo:Int, progress:Float){
        _passoAtual.postValue(Pair(passo, progress))
    }

}