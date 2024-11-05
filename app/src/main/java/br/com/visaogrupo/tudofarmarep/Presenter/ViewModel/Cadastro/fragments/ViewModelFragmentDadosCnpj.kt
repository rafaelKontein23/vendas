package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CnpjUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RepostaCnpj
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosCnpj(
     val cnpjUseCase: CnpjUseCase
) :ViewModel(){
     val _cnpj = MutableLiveData<RepostaCnpj>()
     val cnpj: LiveData<RepostaCnpj> get()  = _cnpj

     private val _lista = MutableLiveData<List<String>>()
     val listaSpinner: LiveData<List<String>> get() = _lista

     fun buscaDadosCnpj(){
          viewModelScope.launch(Dispatchers.IO) {
               val responseCnpj = cnpjUseCase.buscaDadosCnpjUseCase()
               _cnpj.postValue(responseCnpj!!)
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