package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import FormularioCadastro
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.AreaDeAtuacaoUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.Mesorregiao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaAreaAtuacaoCadastrais
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidades
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.ListaUtils
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosAreaDeAtuacao(
    private val areaDeAtuacaoUseCase: AreaDeAtuacaoUseCase,
    private val cadastroUseCase: CadastroUseCase,
    private val preferenciasUtils: PreferenciasUtils
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

    private val _listaCidadeBusca= MutableLiveData<ArrayList<RespostaCidades>?>()
    val listaCidadesBusca: LiveData<ArrayList<RespostaCidades>?> = _listaCidadeBusca

    private val _listaMessoRegiaoBusca= MutableLiveData<ArrayList<RespostaMessoRegiao>?>()
    val listaMessoRegiaoBusca: LiveData<ArrayList<RespostaMessoRegiao>?> = _listaMessoRegiaoBusca


    val _editaDados = MutableLiveData<Boolean>() ;
    val editaDadosObs: LiveData<Boolean> = _editaDados



    private val _ufSelecionada = MutableLiveData<String>()
    val ufSelecionada: LiveData<String> = _ufSelecionada

    private val _ufTexto = MutableLiveData<String>()
    val ufTextoObs: LiveData<String> = _ufTexto

    private val _mesorregiaoSelecionada = MutableLiveData<ArrayList<RespostaMessoRegiao>?>()
    val mesorregiaoSelecionada: LiveData<ArrayList<RespostaMessoRegiao>?> = _mesorregiaoSelecionada

    private val _cidadeSelecionada = MutableLiveData<ArrayList<RespostaCidades>?>()
    val cidadeSelecionada: LiveData<ArrayList<RespostaCidades>?> = _cidadeSelecionada

    init {
        _listaEstados.value = ListaUtils.EstadosUtils.obterListaEstados().toMutableList()
    }

    fun buscaAreaAtuacaoCadastroExistente(uf: String){
        viewModelScope.launch(Dispatchers.IO) {
            val (listaRespostaMessoRegiao, listaRespostaCidades, listaGeralMessoRegiaoList) = areaDeAtuacaoUseCase.recuperaDadosMesorregiao(uf)
            if (listaGeralMessoRegiaoList != null) {
                listaGeralMessoRegiao.clear()
                listaGeralMessoRegiao.addAll(listaGeralMessoRegiaoList)
            }
            _listaMesorregiao.postValue(listaRespostaMessoRegiao)
            _listaCidades.postValue(listaRespostaCidades)
            val listaRespostasMessoRegiao = mapearMesorregioesParaRespostas(FormularioCadastro.cadastroRequestAreaAtuacal.Mesorregioes)
            val listaRespostasCidade = mapearCidadesParaRespostas(FormularioCadastro.cadastroRequestAreaAtuacal.Mesorregioes)

            _mesorregiaoSelecionada.postValue(listaRespostasMessoRegiao)
            _cidadeSelecionada.postValue(listaRespostasCidade)
        }
    }

    fun buscaDadosAreaDeAtuacaoMesorregiao(uf: String, adicionaInicial: Boolean, isEditavel: Boolean = true, listaAreaAtuacaoCadastrais:ArrayList<RespostaAreaAtuacaoCadastrais> = arrayListOf()) {
        viewModelScope.launch(Dispatchers.IO) {
            val (listaRespostaMessoRegiao, listaRespostaCidades, listaGeralMessoRegiaoList) = areaDeAtuacaoUseCase.recuperaDadosMesorregiao(uf)
            if (listaGeralMessoRegiaoList != null) {
                listaGeralMessoRegiao.clear()
                listaGeralMessoRegiao.addAll(listaGeralMessoRegiaoList)
            }
            _listaMesorregiao.postValue(listaRespostaMessoRegiao)
            _listaCidades.postValue(listaRespostaCidades)

            if(!isEditavel){
                if (listaAreaAtuacaoCadastrais.isNotEmpty()) {
                    val mesorregioesIdsSelecionadas = listaAreaAtuacaoCadastrais.map { it.Mesorregiao_id }.toSet()
                    val cidadesIdsSelecionadas = listaAreaAtuacaoCadastrais.map { it.Cidade }.toSet()
                    val listaSelecionadosMeso = listaRespostaMessoRegiao
                        ?.filter { it.Mesorregiao_id in mesorregioesIdsSelecionadas }
                        ?.distinctBy { it.Mesorregiao_id }
                        ?.let { ArrayList(it) }
                        ?: ArrayList()
                    val listaSelecionadosCidades  = listaRespostaCidades
                        ?.filter { it.Cidade in cidadesIdsSelecionadas }
                        ?.distinctBy { it.Cidade }
                        ?.let { ArrayList(it) }
                        ?: ArrayList()

                    _mesorregiaoSelecionada.postValue(listaSelecionadosMeso)
                    _cidadeSelecionada.postValue(listaSelecionadosCidades)
                }
            }else{
                if (adicionaInicial){
                    _mesorregiaoSelecionada.postValue(listaRespostaMessoRegiao)
                    _cidadeSelecionada.postValue(listaRespostaCidades)
                }
            }
        }
    }

    fun buscaDadosAreaDeAtuacaoEdicao() {
        viewModelScope.launch(Dispatchers.IO) {
            val representanteID  = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
            val listaAreaAtuacaoCadastrais = areaDeAtuacaoUseCase.recuperaDadosCadastraisAreaAtuacao(representanteID)
            if(listaAreaAtuacaoCadastrais.isNotEmpty()){
                val uf = listaAreaAtuacaoCadastrais.first().UF
                _ufTexto.postValue(uf)
                buscaDadosAreaDeAtuacaoMesorregiao(uf, false, isEditavel =  false, listaAreaAtuacaoCadastrais = listaAreaAtuacaoCadastrais)
            }
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
                    val cidade = RespostaCidades(messoRegiaoItem.Mesorregiao_id,messoRegiaoItem.Municipio)
                    listaCidades.add(cidade)
                }
            }
        }
        if(!listaCidades.contains( RespostaCidades(0, "Todas"))){
            val cidade = RespostaCidades(0, "Todas")
            listaCidades.add(0,cidade)
        }
    }


    fun confereMessoRegiaoList(): Boolean {
        return _mesorregiaoSelecionada.value!!.isEmpty()
    }
    fun confereCidadesList(): Boolean {
        return _cidadeSelecionada.value!!.isEmpty()
    }
    fun mandaCadatro(islimpaCadastroUseCase: Boolean ){
        viewModelScope.launch(Dispatchers.IO) {
            val areaDeAtuacao = areaDeAtuacaoUseCase.converterParaEstado(
                (_ufSelecionada.value ?: _ufTexto.value).toString(),
                _mesorregiaoSelecionada.value!!,
                _cidadeSelecionada.value!!
            )
            FormularioCadastro.cadastroRequestAreaAtuacal = areaDeAtuacao

            val edtitaDados = cadastroUseCase.enviaCadastro(islimpaCadastroUseCase)
            _editaDados.postValue(edtitaDados)
        }
    }


    fun buscaCidades(cidade: String) {
        val listaCidadesAtual =_listaCidades.value?.toMutableList() as? ArrayList<RespostaCidades> ?: ArrayList()
        val listaFiltradaCidades = listaCidadesAtual.filter { it.Cidade.contains(cidade, ignoreCase = true) }
        if (listaFiltradaCidades.isEmpty() && cidade.isEmpty()){
            _listaCidadeBusca.postValue(ArrayList(listaCidadesAtual))
        }else{
            _listaCidadeBusca.postValue(ArrayList(listaFiltradaCidades))
        }

    }
    fun buscaMessoRegiao(uf: String) {
        val listaMessoRegiaoAtual =_listaMesorregiao.value?.toMutableList() as? ArrayList<RespostaMessoRegiao> ?: ArrayList()
        val listaFiltradaMessoRegiao = listaMessoRegiaoAtual.filter { it.Mesorregiao_Nome.contains(uf, ignoreCase = true) }
        if (listaFiltradaMessoRegiao.isEmpty() && uf.isEmpty()){
            _listaMessoRegiaoBusca.postValue(listaMessoRegiaoAtual)
        }else{
            _listaMessoRegiaoBusca.postValue(ArrayList(listaFiltradaMessoRegiao))
        }

    }
    fun mapearMesorregioesParaRespostas(mesorregioes: List<Mesorregiao>): ArrayList<RespostaMessoRegiao> {
        val respostas = ArrayList<RespostaMessoRegiao>()
        val idsAdicionados = mutableSetOf<Int>()
        for (mesorregiao in mesorregioes) {
            if (mesorregiao.Mesorregiao_id !in idsAdicionados) {
                respostas.add(
                    RespostaMessoRegiao(
                        Mesorregiao_id = mesorregiao.Mesorregiao_id,
                        Mesorregiao_Nome = mesorregiao.Mesorregiao,
                        Municipio = ""
                    )
                )
                idsAdicionados.add(mesorregiao.Mesorregiao_id)
            }
        }

        return respostas
    }
    fun mapearCidadesParaRespostas(mesorregioes: List<Mesorregiao>): ArrayList<RespostaCidades> {
        val respostas = ArrayList<RespostaCidades>()

        for (mesorregiao in mesorregioes) {
            for (cidade in mesorregiao.Cidades) {
                respostas.add(
                    RespostaCidades(
                        ID = mesorregiao.Mesorregiao_id,
                        Cidade = cidade.Cidade
                    )
                )
            }
        }

        return respostas
    }


}
