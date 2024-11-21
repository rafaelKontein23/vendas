package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CodigoIndicaoUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCodigoIndicacao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentCodigoIndicacao(
    private val codigoIndicaoUseCase: CodigoIndicaoUseCase,
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val cadastroUseCase: CadastroUseCase
) : ViewModel(), ISuporteTelefone {
     private val  _dadosIndicacao = MutableLiveData<RespostaCodigoIndicacao?>()
     val dadosIndicacao:LiveData<RespostaCodigoIndicacao?> get() = _dadosIndicacao
    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte
     fun buscaInformacaoIndicacao(hash: String){
         viewModelScope.launch (Dispatchers.IO){
             val respostaCodigoIndicacao =  codigoIndicaoUseCase.buscaInfoHash(hash)
             _dadosIndicacao.postValue(respostaCodigoIndicacao)
         }
     }

    fun enviaCadadstro(hash: String){
        viewModelScope.launch (Dispatchers.IO){
            FormularioCadastro.cadastro.hash = hash
            cadastroUseCase.enviaCadastro()
        }
    }

    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }
}