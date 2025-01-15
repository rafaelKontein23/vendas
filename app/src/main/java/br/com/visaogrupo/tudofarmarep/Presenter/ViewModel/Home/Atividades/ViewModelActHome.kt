package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades

import FormularioCadastro
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.FotoDePerfilUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.VendaRemotaUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.FotoPerfilRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.Enuns.EnumMenu
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewModelActHome (
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val preferenciasUtils: PreferenciasUtils,
    private val vendaRemotaUseCase: VendaRemotaUseCase,
    private val fotoDePerfilUseCase: FotoDePerfilUseCase,
    private val cadastroUseCase: CadastroUseCase

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


    private val _mostraMenssagem = MutableLiveData<Pair<Boolean, String>>()
    val mostraMenssagem: LiveData<Pair<Boolean, String>> get() = _mostraMenssagem

    private val _mostraCarregando = MutableLiveData<Boolean>()
    val mostraCarregando : LiveData<Boolean> get() = _mostraCarregando

    private val _nomeId = MutableLiveData<Pair<String, EnumMenu>>()
    val nomeId: LiveData<Pair<String, EnumMenu>> get() = _nomeId

    fun mostraMenssagem(mostra: Boolean = false, menssagem: String = ""){
        _mostraMenssagem.postValue(Pair(mostra, menssagem))
    }

    fun mudaStatusCarga(status: Boolean, data: String = "") {
        preferenciasUtils.salvarBool(status, ProjetoStrings.fazendoCarga)
        preferenciasUtils.salvarTexto(ProjetoStrings.dataCarga, data)
    }
    fun recuperaStatusCarga() : Boolean{
        val status = preferenciasUtils.recuperarBool(ProjetoStrings.fazendoCarga, false)
        return status
    }
    fun mudaCadastroProgresso(progresso: Int) {
        _cadastroProgresso.postValue(progresso)
    }
    fun mostraCarregando(mostra: Boolean) {
        _mostraCarregando.postValue(mostra)
    }

    fun atualizaFragmentHome(nome: String, id: EnumMenu){
        _nomeId.postValue(Pair(nome, id))
    }

    fun atualizaWebView(nome: String, url: String){
        _urlNome.postValue(Pair(nome, url))

    }

    fun buscaNomeCnpj(){
        val cnpj =         preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjLogin, "-") ?: "-"
        val nomeCompleto = preferenciasUtils.recuperarTexto(ProjetoStrings.nomeCompleto, "-")?: "-"
        _informacaoCnpjNome.postValue(Pair(cnpj, nomeCompleto))
    }

    fun buscaLinkVendaremotas(){
         viewModelScope.launch(Dispatchers.IO) {
             val reprsentanteID = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
             val hash = vendaRemotaUseCase.buscaDadosVendaRemota(reprsentanteID)
             _hashVendaRemota.postValue(hash)

         }
    }
    fun mudaFotoPerfil(){
        mostraCarregando(true)
        viewModelScope.launch(Dispatchers.IO) {
            val representanteId = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
            val caminhoFoto = URLs.urlFotoPErfil
            val nomeArquivo = representanteId.toString() + "_perfil.jpg"
            var sucesso = false
            val enviaFoto = async {
                sucesso = fotoDePerfilUseCase.mandaFotoPerfilCaminho(nomeArquivo)
                if (sucesso){
                    FormularioCadastro.cadastro.FotoPerfil = caminhoFoto + nomeArquivo
                    FormularioCadastro.cadastro.isAssinaContrato = true
                    FormularioCadastro.cadastro.isTermoPolitica = true
                    FormularioCadastro.cadastro.isPoliticaPrivacidade = true
                    FormularioCadastro.cadastro.CNPJ =
                        preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjLogin, "").toString()
                    val cadastro =  cadastroUseCase.enviaCadastro(true)

                    if(!cadastro){
                        sucesso = false
                    }else{
                        FormularioCadastro.fotoPerfilUrl = caminhoFoto + nomeArquivo

                    }
                }else{
                    sucesso = false
                }
            }
            enviaFoto.await()
            mostraCarregando(false)
            val mensagem =  if (!sucesso) "Erro ao enviar foto de perfil" else "Foto de perfil enviada com sucesso"
            mostraMenssagem(sucesso, mensagem)

        }
    }
    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }



}