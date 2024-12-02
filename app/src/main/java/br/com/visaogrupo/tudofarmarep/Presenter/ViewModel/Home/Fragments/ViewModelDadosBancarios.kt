package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.DadosBancariosUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancaria
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancariaDados
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelDadosBancarios(
    private val dadosBancariosUseCase: DadosBancariosUseCase,
    private val preferenciasUtils: PreferenciasUtils
):ViewModel() {
    val _dadosBancarios = MutableLiveData<RespostaDadosBancarios?>()
    val dadosBancarios :LiveData<RespostaDadosBancarios?> = _dadosBancarios

    val _listaBanco = MutableLiveData<ArrayList< RespostaInstituicaoBancaria?>>()
    val listabanco :LiveData<ArrayList< RespostaInstituicaoBancaria?>> = _listaBanco
    val _recuperaCNPJ = MutableLiveData<String>()
    val recuperaCNPJ :LiveData<String> = _recuperaCNPJ

    val _textoInstituicao = MutableLiveData<String>()
    val textoInstituicao :LiveData<String> = _textoInstituicao

    fun buscaDadosBancarios(){
        viewModelScope.launch (Dispatchers.IO){
            val dadosBancarios = dadosBancariosUseCase.dadosBancarios()
            _dadosBancarios.postValue(dadosBancarios)
        }
    }

    fun buscaDadosInstituicaoBancaria(){
        viewModelScope.launch (Dispatchers.IO){
            val  listaDados = dadosBancariosUseCase.dadosInstituicaoBancaria()
            _listaBanco.postValue(listaDados)
        }
    }
    fun recuperaCNPJ(){
        val cnpj = preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjLogin, "") ?: ""
        _recuperaCNPJ.postValue(cnpj)
    }
    fun alterarTextoInstituicao(texto:String){
        _textoInstituicao.postValue(texto)
    }
}