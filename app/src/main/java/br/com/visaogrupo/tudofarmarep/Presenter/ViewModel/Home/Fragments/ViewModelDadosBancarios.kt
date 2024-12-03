package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.DadosBancariosUseCase
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidades
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancaria
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancariaDados
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelDadosBancarios(
    private val dadosBancariosUseCase: DadosBancariosUseCase,
    private val preferenciasUtils: PreferenciasUtils,
    private val cadastroUseCase: CadastroUseCase
):ViewModel() {
    val _dadosBancarios = MutableLiveData<RespostaDadosBancarios?>()
    val dadosBancarios :LiveData<RespostaDadosBancarios?> = _dadosBancarios

    val _listaBanco = MutableLiveData<ArrayList< RespostaInstituicaoBancaria?>>()
    val listabanco :LiveData<ArrayList< RespostaInstituicaoBancaria?>> = _listaBanco

    val _listaBancoPesquisa = MutableLiveData<ArrayList<RespostaInstituicaoBancaria?>>()
    val listaBancoPesquisa :LiveData<ArrayList<RespostaInstituicaoBancaria?>> = _listaBancoPesquisa

    val _recuperaCNPJ = MutableLiveData<String>()
    val recuperaCNPJ :LiveData<String> = _recuperaCNPJ

    val _textoInstituicao = MutableLiveData<String>()
    val textoInstituicao :LiveData<String> = _textoInstituicao

    val _atualizaDadosBancarios = MutableLiveData<Boolean>()
    val atualizaDadosBancarios :LiveData<Boolean> = _atualizaDadosBancarios


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
    fun mandaDadosBancarios(
        conta:String,
        agencia:String,
        banco:String
    ){
        viewModelScope.launch (Dispatchers.IO){
            FormularioCadastro.dadosBancarios.Banco = banco
            FormularioCadastro.dadosBancarios.Agencia = agencia
            FormularioCadastro.dadosBancarios.Conta = conta
            FormularioCadastro.dadosBancarios.CNPJ =
                preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjLogin, "").toString()

            val cadastroUseCase = cadastroUseCase.enviaCadastro(true)

            _atualizaDadosBancarios.postValue(cadastroUseCase)


        }
    }

    fun pesquisaInstituicao(texto: String) {
        viewModelScope.launch {
            val listaOriginal = _listaBanco.value ?: emptyList()
            val listaFiltrada = listaOriginal.filter {
                it?.Descricao?.lowercase()?.contains(texto.lowercase()) == true
            }
            _listaBancoPesquisa.postValue(ArrayList(listaFiltrada))
        }
    }
}