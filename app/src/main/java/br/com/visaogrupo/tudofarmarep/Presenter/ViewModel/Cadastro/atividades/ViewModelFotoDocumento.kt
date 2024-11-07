package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelFotoDocumento () :ViewModel(){
    private val _fotoDocumento = MutableLiveData<Uri>()
    val fotoDocumento: LiveData<Uri> get() = _fotoDocumento

    fun setFotoDocumento(uri: Uri) {
        _fotoDocumento.postValue(uri)
    }
}