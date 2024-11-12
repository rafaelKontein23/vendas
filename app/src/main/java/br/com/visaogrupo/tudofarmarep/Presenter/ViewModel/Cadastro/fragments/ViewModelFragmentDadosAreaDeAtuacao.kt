package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.AreaDeAtuacaoUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Model.MessoRegiao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Utils.ListaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosAreaDeAtuacao(
    private val areaDeAtuacaoUseCase: AreaDeAtuacaoUseCase,
    private val cadastroUseCase: CadastroUseCase
) : ViewModel() {

    private val _listaEstados = MutableLiveData<List<String>>()
    val listaEstados: LiveData<List<String>> = _listaEstados

    private val _ufSelecionada = MutableLiveData<String>()
    val ufSelecionada: LiveData<String> = _ufSelecionada

    private val _mesorregiaoSelecionada = MutableLiveData<List<MessoRegiao>?>()
    val mesorregiaoSelecionada: LiveData<List<MessoRegiao>?> = _mesorregiaoSelecionada

    private val _listaMesorregiao = MutableLiveData<List<RespostaMessoRegiao>?>()
    val listaMesorregiao: LiveData<List<RespostaMessoRegiao>?> = _listaMesorregiao

    init {
        _listaEstados.value = ListaUtils.EstadosUtils.obterListaEstados().toMutableList()
    }

    fun buscaDadosAreaDeAtuacaoMesorregiao(uf: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultado = areaDeAtuacaoUseCase.recuperaDadosMesorregiao(uf)
            _listaMesorregiao.postValue(resultado)
        }
    }

    fun selecionaUF(uf: String) {
        _ufSelecionada.value = uf
    }

    fun adicionaNaListaMesorregiao(mesoRegiao: MessoRegiao) {
        val lista = _mesorregiaoSelecionada.value?.toMutableList() ?: mutableListOf()
        if (!lista.contains(mesoRegiao)) {
            lista.add(mesoRegiao)
            _mesorregiaoSelecionada.value = lista
        }
    }

    fun removeDaListaMesorregiao(mesoRegiao: MessoRegiao) {
        val lista = _mesorregiaoSelecionada.value?.toMutableList()
        if (lista != null && lista.contains(mesoRegiao)) {
            lista.remove(mesoRegiao)
            _mesorregiaoSelecionada.value = lista
        }
    }

    fun confereMessoRegiao(mesoRegiao: MessoRegiao): Boolean {
        return if (_mesorregiaoSelecionada.value!!.contains(mesoRegiao)) true else false
    }
}
