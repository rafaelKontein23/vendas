package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CnpjUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RepostaCnpj
import br.com.visaogrupo.tudofarmarep.Utils.FormularioCadastro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosCnpj(
     val cnpjUseCase: CnpjUseCase,
     val cadastroUseCase: CadastroUseCase
) :ViewModel(){
     private val _cnpj = MutableLiveData<RepostaCnpj>()
     val cnpj: LiveData<RepostaCnpj> get()  = _cnpj


     private val _lista = MutableLiveData<List<String>>()
     val listaSpinner: LiveData<List<String>> get() = _lista

     fun buscaDadosCnpj(){
          viewModelScope.launch(Dispatchers.IO) {
               val responseCnpj = cnpjUseCase.buscaDadosCnpjUseCase()
               _cnpj.postValue(responseCnpj!!)
          }
     }
    fun salvarInformacoesCnpj(cnpj: String,
                              razaoSocial: String,
                              fantasia: String,
                              cep: String,
                              endereco: String,
                              cidade: String,
                              uf: String,
                              possuiCore: String){
        FormularioCadastro.cadastro.CNPJ = cnpj
        FormularioCadastro.cadastro.RazaoSocial = razaoSocial
        FormularioCadastro.cadastro.Fantasia = fantasia
        FormularioCadastro.cadastro.CEP = cep
        FormularioCadastro.cadastro.Endereco = endereco
        FormularioCadastro.cadastro.Cidade = cidade
        FormularioCadastro.cadastro.UF = uf
        FormularioCadastro.cadastro.possuiCore = possuiCore


    }
    fun enviaCadastro(){
        viewModelScope.launch(Dispatchers.IO) {
            cadastroUseCase.enviaCadastro(FormularioCadastro.cadastro)
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