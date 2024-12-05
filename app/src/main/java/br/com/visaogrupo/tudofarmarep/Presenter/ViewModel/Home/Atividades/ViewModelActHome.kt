package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.VendaRemotaUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Enuns.EnumMenu
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelActHome (
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val preferenciasUtils: PreferenciasUtils,
    private val vendaRemotaUseCase: VendaRemotaUseCase

    ):ViewModel() , ISuporteTelefone {

    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte

    private val _cadastroProgresso = MutableLiveData<Int>()
    val cadastroProgresso: LiveData<Int> get() = _cadastroProgresso

    private val _informacaoCnpjNome = MutableLiveData<Pair<String, String>>()
    val informacaoCnpjNome: LiveData<Pair<String, String>> get() = _informacaoCnpjNome

    private val _hashVendaRemota = MutableLiveData<String>()
    val hashVendaRemota: LiveData<String> get() = _hashVendaRemota

    private val _urlNome = MutableLiveData<Pair<String, String>>()
    val urlNome: LiveData<Pair<String, String>> get() = _urlNome

    private val _nomeId = MutableLiveData<Pair<String, EnumMenu>>()
    val nomeId: LiveData<Pair<String, EnumMenu>> get() = _nomeId

    fun mudaStatusCarga(status: Boolean, data: String = "") {
        preferenciasUtils.salvarBool(status, ProjetoStrings.fazendoCarga)
        preferenciasUtils.salvarTexto(ProjetoStrings.dataCarga, data)
    }
    fun mudaCadastroProgresso(progresso: Int) {
        _cadastroProgresso.postValue(progresso)
    }

    fun atualizaFragmentHome(nome: String, id: EnumMenu){
        _nomeId.postValue(Pair(nome, id))
    }

    fun atualizaWebView(nome: String, url: String){
        _urlNome.postValue(Pair(nome, url))

    }

    fun buscaNomeCnpj(){
        val cnpj =         preferenciasUtils.recuperarTexto(ProjetoStrings.nomeCompleto, "-") ?: "-"
        val nomeCompleto = preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjLogin, "-")?: "-"
        _informacaoCnpjNome.postValue(Pair(cnpj, nomeCompleto))
    }

    fun buscaLinkVendaremotas(){
         viewModelScope.launch(Dispatchers.IO) {
             val reprsentanteID = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
             val hash = vendaRemotaUseCase.buscaDadosVendaRemota(reprsentanteID)
             _hashVendaRemota.postValue(hash)

         }
    }

    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }



}