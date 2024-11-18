package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.launch

class ViewModelFragmentDadosPessoais(
            private val preferenciasUtils: PreferenciasUtils,
            private val cadastroUseCase: CadastroUseCase
            ):ViewModel(){
    private val _numeroCelular = MutableLiveData<String?>()
    val numeroCelular: LiveData<String?> = _numeroCelular

    fun recuperaNumeroCelular() {
        val numeroCelular = preferenciasUtils.recuperarTexto(ProjetoStrings.celular)
        _numeroCelular.postValue(numeroCelular)
    }

    fun salvaCamposPessoais(nome: String,
                            sobrenome: String,
                            cpf: String,
                            dataNacimento: String,
                            email: String,
                            telefoneComercial: String) {

        val cpfFormat = cpf.filter { it.isDigit() }
        val telefoneFormat = telefoneComercial.filter { it.isDigit() }
        preferenciasUtils.salvarTexto("${nome} ${sobrenome}", ProjetoStrings.nomeCompleto)

        FormularioCadastro.cadastro.nome = nome
        FormularioCadastro.cadastro.sobrenome = sobrenome
        FormularioCadastro.cadastro.cpf = cpfFormat
        FormularioCadastro.cadastro.dataNascimento = dataNacimento
        FormularioCadastro.cadastro.email = email
        FormularioCadastro.cadastro.telefoneComercial = telefoneFormat

        enviaCadastro()

    }
    fun enviaCadastro(){
        viewModelScope.launch {
            cadastroUseCase.enviaCadastro()
        }
    }
}