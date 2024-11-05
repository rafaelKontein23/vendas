package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.TokenUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaConfirmaToken
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaSolicitaToken
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.Strings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelActToken(
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val tokenUseCase: TokenUseCase,
    private val preferenciasUtils: PreferenciasUtils

) : ViewModel(), ISuporteTelefone {

    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte

    private val _repostaSolicita = MutableLiveData<RespostaSolicitaToken?>()
    val repostaSolicita: LiveData<RespostaSolicitaToken?> get() = _repostaSolicita

    private val _numeroCelular = MutableLiveData<String>()
    val numeroCelular: LiveData<String> get() = _numeroCelular

    private val _confirmaToken = MutableLiveData<RespostaConfirmaToken?>()
    val confirmaToken: LiveData<RespostaConfirmaToken?> get() = _confirmaToken

    fun solicitaToken(telefone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val respostaSolicitaToken = tokenUseCase.solicitaToken(telefone)
            _repostaSolicita.postValue(respostaSolicitaToken)
        }
    }
    fun recuperarNumeroCelular() {
        viewModelScope.launch {
            val numero = preferenciasUtils.recuperarTexto(Strings.celular)
            _numeroCelular.postValue(numero!!)
        }
    }

    fun confirmaToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val respostaConfirmaToken = tokenUseCase.confirmaToken(token)
            _confirmaToken.postValue(respostaConfirmaToken)
        }
    }

    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }
}