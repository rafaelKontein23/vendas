package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import android.app.Activity
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.LoginUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaLogin
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.CriptografiaChavesSenha
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModelMainActivity(
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val salvaTextos: PreferenciasUtils,
    private val loginUseCase: LoginUseCase


) :ViewModel(), ISuporteTelefone{
    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte

    private val _contadorModal  = MutableLiveData(0)
    val contadorModal: LiveData<Int> get() = _contadorModal


    private val _login = MutableLiveData<RespostaLogin?>()
    val login: LiveData<RespostaLogin?> get() = _login
    private val _senhaVisualizar = MutableLiveData<Boolean>()
    val senhaVisualizar: LiveData<Boolean> = _senhaVisualizar

    private val _confereSenha = MutableLiveData<Boolean>()
    val confereSenha: LiveData<Boolean> = _confereSenha

    private val _ambiente = MutableLiveData<Int>()
    val ambiente: LiveData<Int> = _ambiente

    private  val _fezCadastro = MutableLiveData<Boolean>()
    val  fezCadastro: LiveData<Boolean> = _fezCadastro

    private  val _nomeusaurio = MutableLiveData<String>()
    val nomeusaurio: LiveData<String> = _nomeusaurio
    val _celularUsuario = MutableLiveData<String>()
    val celularUsuario: LiveData<String> = _celularUsuario



    fun verificaCadastro( cnpj: String){
        val cnpjFormat = FormataTextos.removeMascaraCNPJ(cnpj)
        var cadastro = salvaTextos.recuperarBool(ProjetoStrings.casdastro)
        val cnpjSalvo = salvaTextos.recuperarTexto(ProjetoStrings.cnpjLogin)
        if(cnpjSalvo != cnpjFormat){
            cadastro = false
            salvaTextos.salvarBool(cadastro, ProjetoStrings.casdastro)
        }

        _fezCadastro.value = cadastro
    }

    fun abrirModalContator(){
        val novoValor = (_contadorModal.value ?: 0) + 1
        _contadorModal.value = if (novoValor == 6) 0 else novoValor
    }

    fun salvaCnpj(cnpj:String){
        viewModelScope.launch(Dispatchers.IO) {
            val cnpjSemFormatacao = FormataTextos.removeMascaraCNPJ(cnpj)
            FormularioCadastro.cadastro.CNPJ = cnpjSemFormatacao
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

    fun buscaInformacoesLogin(){
        viewModelScope.launch(Dispatchers.IO) {
            val login= loginUseCase.logaUsuario()
            _login.postValue(login)
        }
    }
    fun salvarDadosUsuario(id:Int, nome:String, hash:String, fotoPerfil:String, uf:String){
        salvaTextos.salvaInteiro(id,ProjetoStrings.reprenteID)
        salvaTextos.salvarTexto(nome,ProjetoStrings.nomeCompleto)
        salvaTextos.salvarTexto(hash,ProjetoStrings.hash)
        salvaTextos.salvarTexto(fotoPerfil,ProjetoStrings.caminhoFotoPerfil)
        salvaTextos.salvarTexto(uf,ProjetoStrings.uf)


    }
    private fun trocaAmbiente(ambiente:String){
       URLs.trocaAmbiente(ambiente)
    }

    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }

    fun recuperaInformacoesUser(){
        val nome = salvaTextos.recuperarTexto(ProjetoStrings.nomeCompleto, "") ?: ""
        val celular = salvaTextos.recuperarTexto(ProjetoStrings.celular) ?: ""
        _nomeusaurio.postValue(nome)
        _celularUsuario.postValue(celular)

    }

    fun showBiometricPrompt(context: MainActivity) {
        val executor = ContextCompat.getMainExecutor(context)

        val biometricPrompt = BiometricPrompt(context, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    buscaInformacoesLogin()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    var kasmkavs= ""
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    var kasmkavs= ""
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação Requerida")
            .setSubtitle("Use sua biometria ou o PIN do dispositivo para continuar")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun checkBiometricSupport(context: MainActivity): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                false
            }
            else -> false
        }
    }

}