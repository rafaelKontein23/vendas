package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelActSucessoCadastro(
    val suporteTelefoneReposytory: SuporteTelefoneReposytory
):ViewModel(), ISuporteTelefone {
    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte
    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneReposytory.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }


}