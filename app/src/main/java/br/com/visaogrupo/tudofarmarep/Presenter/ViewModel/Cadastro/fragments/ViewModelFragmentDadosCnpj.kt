package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CnpjUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RepostaCnpj
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosCnpj(
    val cnpjUseCase: CnpjUseCase,
    val cadastroUseCase: CadastroUseCase,
    val sistemaUtils: SistemaUtils
) :ViewModel(){
     private val _cnpj = MutableLiveData<RepostaCnpj?>()
     val cnpj: LiveData<RepostaCnpj?> get()  = _cnpj


     private val _lista = MutableLiveData<List<String>>()
     val listaSpinner: LiveData<List<String>> get() = _lista

     fun buscaDadosCnpj(){
          viewModelScope.launch(Dispatchers.IO) {
               val responseCnpj = cnpjUseCase.buscaDadosCnpjUseCase()
               _cnpj.postValue(responseCnpj)
          }
     }
    fun salvarInformacoesCnpj(cnpj: String,
                              possuiCore: String,
                              uf: String ){
        val cnpjFormat = cnpj.filter { it.isDigit() }
        FormularioCadastro.cadastro.CNPJ = cnpjFormat
        if (possuiCore == "Sim"){
            FormularioCadastro.cadastro.possuiCore = true

        }else{
            FormularioCadastro.cadastro.possuiCore = false
        }
        FormularioCadastro.cadastro.UF = uf
        FormularioCadastro.cadastro.DeviceToken = SistemaUtils.deviceToken
        FormularioCadastro.cadastro.UDID = sistemaUtils.recuperaUdid()
        FormularioCadastro.cadastro.VersaoaAPP = ProjetoStrings.versapApp
        FormularioCadastro.cadastro.Dispositivo = sistemaUtils.recuperaNomeDispositivo()
        FormularioCadastro.cadastro.SistemaOperacional = sistemaUtils.recuperaSO()

    }
    fun enviaCadastro(){
        viewModelScope.launch(Dispatchers.IO) {
            cadastroUseCase.enviaCadastro()
        }
    }


     fun alimentaSpinerCore(){
          val lista = listOf(
               "Selecionar...",
               "Sim",
               "Não"
          )
          _lista.postValue(lista)
     }
}