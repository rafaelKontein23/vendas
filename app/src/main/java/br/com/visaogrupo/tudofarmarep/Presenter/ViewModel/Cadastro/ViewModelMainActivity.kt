package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModelMainActivity(
    private val suporteTelefoneRepository: SuporteTelefoneReposytory

) :ViewModel(){
    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    private val _contadorModal = MutableLiveData<Int>()
    val contadorModal: LiveData<Int> get() = _contadorModal
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte


    fun buscarNumeroTelefoneSuporte(){
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }

    fun abrirModalContator(){
        _contadorModal.postValue(1)
        if (_contadorModal.value == 5){
            _contadorModal.value = 0
        }
    }
}