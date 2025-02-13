package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.ISuporteTelefone
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.DAO.DAOCadastro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelActCelular (
    private val suporteTelefoneRepository: SuporteTelefoneReposytory,
    private val salvaTextos: PreferenciasUtils,
    private val context: Context
):ViewModel(), ISuporteTelefone {

    private val _numeroTelefoneSuporte = MutableLiveData<String>()
    val numeroTelefoneSuporte: LiveData<String> get() = _numeroTelefoneSuporte
    fun exluiCadastro(){
        val dbHelper = DAOHelper(context).readableDatabase
        val daoCadastro = DAOCadastro()



      daoCadastro.deletarCadastro(dbHelper)
    }

    fun salvarCelular(celular:String){
        val celularSemFormatacao = FormataTextos.removeMascaraCelular(celular)
        FormularioCadastro.cadastro.celular = celularSemFormatacao
        salvaTextos.salvarTexto(celularSemFormatacao, ProjetoStrings.celular)
    }
    fun recuperaCelular():String?{
        return salvaTextos.recuperarTexto(ProjetoStrings.celular)
    }

    override fun buscarNumeroTelefoneSuporte() {
        viewModelScope.launch(Dispatchers.IO) {
            val numero = suporteTelefoneRepository.buscarNumeroTelefoneSuporte()
            _numeroTelefoneSuporte.postValue(numero?.LinkZap ?: "")
        }
    }
}