package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.AreaDeAtuacaoUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Utils.ListaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosAreaDeAtuacao(
    val areaDeAtuacaoUseCase: AreaDeAtuacaoUseCase,
    val cadastroUseCase: CadastroUseCase
): ViewModel() {
    private val _listaEstados = MutableLiveData<List<String>>()
    val listaEstados: LiveData<List<String>> = _listaEstados
    val ufSelcionada = MutableLiveData<String>()
    val _ufSelcionada : LiveData<String> = ufSelcionada


    private val _listaMesorregiao = MutableLiveData<List<RespostaMessoRegiao>?>()

    val listaMesorregiao: MutableLiveData<List<RespostaMessoRegiao>?> = _listaMesorregiao


    init {
        _listaEstados.value = ListaUtils.EstadosUtils.obterListaEstados().toMutableList()
    }

    fun buscaDadosAreaDeAtuacaoMesorregiao(UF:String){
        viewModelScope.launch(Dispatchers.IO) {
            val resultado = areaDeAtuacaoUseCase.recuperaDadosMesorregiao(UF)
            _listaMesorregiao.postValue(resultado)

        }
    }
    fun selecionaUF(uf:String){
        ufSelcionada.value = uf
    }
}