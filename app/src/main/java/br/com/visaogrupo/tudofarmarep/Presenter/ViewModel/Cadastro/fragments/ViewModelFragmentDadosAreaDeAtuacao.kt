package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.AreaDeAtuacaoUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidades
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.ListaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosAreaDeAtuacao(
    private val areaDeAtuacaoUseCase: AreaDeAtuacaoUseCase,
    private val cadastroUseCase: CadastroUseCase
) : ViewModel() {
    private val listaGeralMessoRegiao = ArrayList<RespostaMessoRegiao>()
    private val _listaEstados = MutableLiveData<List<String>>()
    val listaEstados: LiveData<List<String>> = _listaEstados
    var cidadeSelecionadaTodos = false
    var mesoRegiaoSelecionadaTodos = false


    private val _listaMesorregiao = MutableLiveData<ArrayList<RespostaMessoRegiao>?>()
    val listaMesorregiao: LiveData<ArrayList<RespostaMessoRegiao>?> = _listaMesorregiao

    private val _listaCidades= MutableLiveData<ArrayList<RespostaCidades>?>()
    val listaCidadesObs: LiveData<ArrayList<RespostaCidades>?> = _listaCidades

    private val _ufSelecionada = MutableLiveData<String>()
    val ufSelecionada: LiveData<String> = _ufSelecionada

    private val _mesorregiaoSelecionada = MutableLiveData<ArrayList<RespostaMessoRegiao>?>()
    val mesorregiaoSelecionada: LiveData<ArrayList<RespostaMessoRegiao>?> = _mesorregiaoSelecionada

    private val _cidadeSelecionada = MutableLiveData<ArrayList<RespostaCidades>?>()
    val cidadeSelecionada: LiveData<ArrayList<RespostaCidades>?> = _cidadeSelecionada

    init {
        _listaEstados.value = ListaUtils.EstadosUtils.obterListaEstados().toMutableList()
    }

    fun buscaDadosAreaDeAtuacaoMesorregiao(uf: String, adicionaInicial: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val (listaRespostaMessoRegiao, listaRespostaCidades, listaGeralMessoRegiaoList) = areaDeAtuacaoUseCase.recuperaDadosMesorregiao(uf)
            if (listaGeralMessoRegiaoList != null) {
                listaGeralMessoRegiao.clear()
                listaGeralMessoRegiao.addAll(listaGeralMessoRegiaoList)
            }
            if (adicionaInicial){
                _mesorregiaoSelecionada.postValue(listaRespostaMessoRegiao)
                _cidadeSelecionada.postValue(listaRespostaCidades)
            }
            _listaMesorregiao.postValue(listaRespostaMessoRegiao)
            _listaCidades.postValue(listaRespostaCidades)
        }
    }

    fun limparListas(){
        val listaMessoRegiao = _listaMesorregiao.value
        val listaCidades = _listaCidades.value
        if (listaMessoRegiao != null && listaCidades != null){
            _listaMesorregiao.value!!.clear()
            _listaCidades.value!!.clear()
        }
        _mesorregiaoSelecionada.postValue(listaMessoRegiao)
        _cidadeSelecionada.postValue(listaCidades)
    }
    fun alternaSelecaoCidade() {
        cidadeSelecionadaTodos = !cidadeSelecionadaTodos
    }
    fun alternaSelecaoMessoRegiao() {
        mesoRegiaoSelecionadaTodos = !mesoRegiaoSelecionadaTodos
    }

    fun selecionaUF(uf: String) {
        _ufSelecionada.value = uf
    }

    fun adicionaNaListaMesorregiao(mesoRegiao: RespostaMessoRegiao) {
        val lista = _mesorregiaoSelecionada.value?.toMutableList() as? ArrayList<RespostaMessoRegiao> ?: ArrayList()
        val listaCidades = _cidadeSelecionada.value?.toMutableList() as? ArrayList<RespostaCidades> ?: ArrayList()

        if (!lista.contains(mesoRegiao)) {
            lista.add(mesoRegiao)
            adicionaCidadexMesoRegiao(lista, listaCidades)

            _listaCidades.postValue(ArrayList(listaCidades))
            _cidadeSelecionada.postValue(ArrayList(listaCidades))
            _mesorregiaoSelecionada.postValue(ArrayList(lista))
        }
    }
    fun adicionaNaListaTodasMesorregiao(){
        val lista = _listaMesorregiao.value?.toMutableList() as? ArrayList<RespostaMessoRegiao> ?: ArrayList()
        val listaCidades = _listaCidades.value?.toMutableList() as? ArrayList<RespostaCidades> ?: ArrayList()
        _mesorregiaoSelecionada.postValue(lista)
        _cidadeSelecionada.postValue(listaCidades)
    }
    fun removeDaListaTodasMesorregiao(){

        _mesorregiaoSelecionada.postValue(ArrayList())
        _cidadeSelecionada.postValue(ArrayList())
    }
    fun adicionaNaListaTodasCidades(){
        val listaCidades = _listaCidades.value?.toMutableList() ?: ArrayList()
        _cidadeSelecionada.postValue(ArrayList(listaCidades))
    }
    fun removeDaListaTodasCidades(){
        _cidadeSelecionada.postValue(ArrayList())
    }

    fun removeDaListaMesorregiao(mesoRegiao: RespostaMessoRegiao) {
        val lista = _mesorregiaoSelecionada.value?.toMutableList() as? ArrayList<RespostaMessoRegiao> ?: ArrayList()
        val listaCidades = _cidadeSelecionada.value?.toMutableList() as? ArrayList<RespostaCidades> ?: ArrayList()

        if (lista.contains(mesoRegiao)) {
            lista.remove(mesoRegiao)
            if (lista.isEmpty()) {
                listaCidades.clear()
                _cidadeSelecionada.postValue(ArrayList(listaCidades))
            } else {
                adicionaCidadexMesoRegiao(lista, listaCidades)
                _listaCidades.postValue(ArrayList(listaCidades))
                _cidadeSelecionada.postValue(ArrayList(listaCidades))
            }
            _mesorregiaoSelecionada.postValue(ArrayList(lista))
        }
    }

    fun confereMessoRegiao(mesoRegiao: RespostaMessoRegiao): Boolean {
        return _mesorregiaoSelecionada.value!!.contains(mesoRegiao)
    }

    fun confereTamanhoListaMesorregiao(): Boolean {
        if(_mesorregiaoSelecionada.value == null || _listaMesorregiao.value == null) return true
        return _mesorregiaoSelecionada.value!!.size == _listaMesorregiao.value!!.size
    }

    fun adicionaNaListaCidades(cidade: RespostaCidades) {
        val lista = _cidadeSelecionada.value?.toMutableList() as? ArrayList<RespostaCidades> ?: ArrayList()
        if (!lista.contains(cidade)) {
            lista.add(cidade)
            _cidadeSelecionada.postValue(ArrayList(lista))
        }
    }

    fun removeDaListaCidades(cidade: RespostaCidades) {
        val lista = _cidadeSelecionada.value?.toMutableList() as? ArrayList<RespostaCidades> ?: ArrayList()
        if (lista.contains(cidade)) {
            lista.remove(cidade)
            _cidadeSelecionada.postValue(ArrayList(lista))
        }
    }

    fun confereCidades(cidades: RespostaCidades): Boolean {
        return _cidadeSelecionada.value!!.contains(cidades)
    }

    fun confereTamanhoListaCidades(): Boolean {
        if(_cidadeSelecionada.value == null || _listaCidades.value == null) return true
        return _cidadeSelecionada.value!!.size == _listaCidades.value!!.size
    }

    private fun adicionaCidadexMesoRegiao(lista : ArrayList<RespostaMessoRegiao>, listaCidades: ArrayList<RespostaCidades>){
        listaCidades.clear()
        for (messoRegiaoItem in listaGeralMessoRegiao){
            for (messoRegiaoItemSecundo in lista){
                if(messoRegiaoItem.Mesorregiao_id == messoRegiaoItemSecundo.Mesorregiao_id  ){
                    val cidade = RespostaCidades(0,messoRegiaoItem.Municipio)
                    listaCidades.add(cidade)
                }
            }
        }
    }


    fun confereMessoRegiaoList(): Boolean {
        return _mesorregiaoSelecionada.value!!.isEmpty() ?: false
    }
    fun confereCidadesList(): Boolean {
        return _cidadeSelecionada.value!!.isEmpty()
    }
    fun mandaCadatro(){
        viewModelScope.launch(Dispatchers.IO) {
            val areaDeAtuacao = areaDeAtuacaoUseCase.converterParaEstado(
                _ufSelecionada.value!!,
                _mesorregiaoSelecionada.value!!,
                _cidadeSelecionada.value!!
            )
            FormularioCadastro.cadastroRequestAreaAtuacal = areaDeAtuacao

            cadastroUseCase.enviaCadastro()
        }
    }


    fun buscaCidades(cidade: String): List<RespostaCidades> {
        val listaCidadesAtual =_listaCidades.value?.toMutableList() as? ArrayList<RespostaCidades> ?: ArrayList()
        val listaFiltradaCidades = listaCidadesAtual.filter { it.Cidade.contains(cidade, ignoreCase = true) }
        if (listaFiltradaCidades.isEmpty()){

        }else

    }
}
