package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RepostaCnpj
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCnpjDados
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CnpjRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.Strings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosCnpj(
     val cnpjRepository: CnpjRepository,
     val preferenciasUtils: PreferenciasUtils
) :ViewModel(){
     val _cnpj = MutableLiveData<RepostaCnpj>()
     val cnpj: LiveData<RepostaCnpj> get()  = _cnpj

     val _lista = MutableLiveData<List<String>>()
     val listaSpinner: LiveData<List<String>> get() = _lista

     fun buscaDadosCnpj(){
          viewModelScope.launch(Dispatchers.IO) {
               val cnpj = preferenciasUtils.recuperarTexto(Strings.cnpjCadastro)
               val  responseCnpj = cnpjRepository.buscaDadosCnpj(cnpj!!)
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