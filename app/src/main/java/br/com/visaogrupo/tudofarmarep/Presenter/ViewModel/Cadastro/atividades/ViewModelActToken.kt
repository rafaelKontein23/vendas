package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.SolicitaTokenRquest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaSolicitaToken
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.TokenRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.Strings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelActToken(
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private  val tokenRepository: TokenRepository,
    private val  preferenciasUtils: PreferenciasUtils
):ViewModel(), ISuporteTelefone {

    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte
    val _repostaSolicita = MutableLiveData<RespostaSolicitaToken?>()
    val repostaSolicita: LiveData<RespostaSolicitaToken?> get() = _repostaSolicita

   fun solicitaToken(telefone:String) {
       viewModelScope.launch (Dispatchers.IO){
           val solicitaTokenRquest = SolicitaTokenRquest("", telefone)
           val respostaSolicitaToken = tokenRepository.solicitaTokenReposiory(solicitaTokenRquest)
           _repostaSolicita.postValue(respostaSolicitaToken!!)
       }
   }
    fun recuprarNumeroCelular():String?{
        return  preferenciasUtils.recuperarTexto(Strings.celular)
    }

    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }
}