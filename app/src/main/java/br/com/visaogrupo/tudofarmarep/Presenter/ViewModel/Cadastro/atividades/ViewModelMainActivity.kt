package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.CriptografiaChavesSenha
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.Strings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModelMainActivity(
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val salvaTextos: PreferenciasUtils

) :ViewModel(){
    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte

    private val _contadorModal  = MutableLiveData(0)
    val contadorModal: LiveData<Int> get() = _contadorModal

    private val _senhaVisualizar = MutableLiveData<Boolean>()
    val senhaVisualizar: LiveData<Boolean> = _senhaVisualizar

    val _confereSenha = MutableLiveData<Boolean>()
    val confereSenha: LiveData<Boolean> = _confereSenha

    val _ambiente = MutableLiveData<Int>()
    val ambiente: LiveData<Int> = _ambiente


    fun buscarNumeroTelefoneSuporte(){
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }

    fun abrirModalContator(){
        val novoValor = (_contadorModal.value ?: 0) + 1
        _contadorModal.value = if (novoValor == 6) 0 else novoValor
    }

    fun alterarSenhaVisualizar() {
        _senhaVisualizar.value = !(_senhaVisualizar.value ?: false)

    }

    fun confereSenha(senhaCap:String) {
         if(senhaCap.equals(CriptografiaChavesSenha.senhaAmbiente)){
             _confereSenha.value = true
         }else{
             _confereSenha.value = false
         }
    }

    fun trocaAmbienteModal(ambiente:Int){
        viewModelScope.launch(Dispatchers.IO) {
            when(ambiente){
                1 -> {
                    _ambiente.postValue(1)
                    salvaAmbiente("www")
                }
                2 -> {
                    _ambiente.postValue(2)
                    salvaAmbiente("qa")

                }
                3 -> {
                    _ambiente.postValue(3)
                    salvaAmbiente("wwwe")


                }
                4 -> {
                    _ambiente.postValue(4)
                    salvaAmbiente("wwwi")

                }
                5 -> {
                    _ambiente.postValue(5)
                    salvaAmbiente("stage")

                }
                else -> {
                    _ambiente.postValue(1)
                    salvaAmbiente("www")


                }
            }
        }
    }

    fun salvaAmbiente(ambiente:String){
        trocaAmbiente(ambiente)
        salvaTextos.salvarTexto(ambiente, Strings.ambiente)
    }

    fun recuperaAmbiente(){
        val ambiente = salvaTextos.recuperarTexto(Strings.ambiente)
        when(ambiente){
            "www" -> {
               trocaAmbiente("www")
            }
            "wwwe"  ->{
                trocaAmbiente("wwwe")
            }
            "qa" -> {
                trocaAmbiente("qa")
            }
            "wwwi" -> {
                trocaAmbiente("wwwi")
            }
            "stage" -> {
                trocaAmbiente("stage")
            }
            else -> {
                trocaAmbiente("5456456456465465")
            }
        }
    }

    fun trocaAmbiente(ambiente:String){
        URLs.urlWsBase = "https://${ambiente}.visaogrupo.com.br/ws/"

    }
}