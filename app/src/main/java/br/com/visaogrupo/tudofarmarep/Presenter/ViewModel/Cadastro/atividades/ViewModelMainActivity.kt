package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.CriptografiaChavesSenha
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModelMainActivity(
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val salvaTextos: PreferenciasUtils

) :ViewModel(), ISuporteTelefone{
    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte

    private val _contadorModal  = MutableLiveData(0)
    val contadorModal: LiveData<Int> get() = _contadorModal

    private val _senhaVisualizar = MutableLiveData<Boolean>()
    val senhaVisualizar: LiveData<Boolean> = _senhaVisualizar

    private val _confereSenha = MutableLiveData<Boolean>()
    val confereSenha: LiveData<Boolean> = _confereSenha

    private val _ambiente = MutableLiveData<Int>()
    val ambiente: LiveData<Int> = _ambiente

    private  val _fezCadastro = MutableLiveData<Boolean>()
    val  fezCadastro: LiveData<Boolean> = _fezCadastro


    fun verificaCadastro(){
        val cadastro = salvaTextos.recuperarBool(ProjetoStrings.casdastro)
        _fezCadastro.value = cadastro
    }

    fun abrirModalContator(){
        val novoValor = (_contadorModal.value ?: 0) + 1
        _contadorModal.value = if (novoValor == 6) 0 else novoValor
    }

    fun salvaCnpj(cnpj:String){
        viewModelScope.launch(Dispatchers.IO) {
            val cnpjSemFormatacao = FormataTextos.removeMascaraCNPJ(cnpj)
            salvaTextos.salvarTexto(cnpjSemFormatacao, ProjetoStrings.cnpjCadastro)
        }
    }

    fun recuperaCnpj():String?{
        return salvaTextos.recuperarTexto(ProjetoStrings.cnpjCadastro)
    }

    fun alterarSenhaVisualizar() {
        _senhaVisualizar.value = !(_senhaVisualizar.value ?: false)

    }

    fun confereSenha(senhaCap:String) {
        _confereSenha.value = senhaCap == CriptografiaChavesSenha.senhaAmbiente
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

    private fun salvaAmbiente(ambiente:String){
        trocaAmbiente(ambiente)
        salvaTextos.salvarTexto(ambiente, ProjetoStrings.ambiente)
    }

    fun recuperaAmbiente(){
        val ambiente = salvaTextos.recuperarTexto(ProjetoStrings.ambiente)
        when(ambiente){
            "www" -> {
               trocaAmbiente("www")
            }
            "wwwe"  ->{
                trocaAmbiente("wwwe")
                _ambiente.postValue(3)

            }
            "qa" -> {
                trocaAmbiente("qa")
                _ambiente.postValue(2)

            }
            "wwwi" -> {
                trocaAmbiente("wwwi")
                _ambiente.postValue(4)

            }
            "stage" -> {
                trocaAmbiente("stage")
            }
            else -> {
                trocaAmbiente("wwwe")
            }
        }
    }


    private fun trocaAmbiente(ambiente:String){
        URLs.urlWsBase = "https://${ambiente}.visaogrupo.com.br/ws/"

    }

    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }
}