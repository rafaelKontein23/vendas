package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.Strings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class ViewModelFragmentDadosPessoais(
            private val preferenciasUtils: PreferenciasUtils
            ):ViewModel(){
    private val _numeroCelular = MutableLiveData<String?>()
    val numeroCelular: LiveData<String?> = _numeroCelular

    fun recuperaNumeroCelular() {
        val numeroCelular = preferenciasUtils.recuperarTexto(Strings.celular)
        _numeroCelular.postValue(numeroCelular)
    }


}