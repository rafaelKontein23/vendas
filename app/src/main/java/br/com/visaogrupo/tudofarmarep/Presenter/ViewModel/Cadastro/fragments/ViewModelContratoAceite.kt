package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewModelContratoAceite(
    private val cadastroUseCase: CadastroUseCase,
    private val preferenciasUtils: PreferenciasUtils
)  : ViewModel(){
    val  _contratoAssinado = MutableLiveData<Boolean>()
    val  contratoAssinado get() = _contratoAssinado

    val  _fazCadastro = MutableLiveData<Boolean>()
    val  fazCadastro get() = _fazCadastro
    fun assinaturaContrato(){
         FormularioCadastro.cadastro.isAssinaContrato = true
        _contratoAssinado.value = true

    }
    fun salvaCnpj(cnpjLogin:String){

         preferenciasUtils.salvarTexto(cnpjLogin, ProjetoStrings.cnpjLogin)
    }


    fun enviaCadastroFinal() {


       viewModelScope.launch (Dispatchers.IO) {
           var retornoFoto = false
           var retornoCadastro = false
           var retornoAssinatura = false


            val tarefaFoto = async {
                retornoFoto =  cadastroUseCase.mandaFotoCadastro()

            }
            val tarefaAssinatura = async {
                retornoAssinatura = cadastroUseCase.enviaAssinatura()
            }
           val tarefaCadastro = async {
               retornoCadastro = cadastroUseCase.enviaCadastroFinal()
           }
            tarefaCadastro.await()
            tarefaFoto.await()
            tarefaAssinatura.await()
            MainScope().launch {
                if (retornoAssinatura && retornoCadastro && retornoFoto){
                    _fazCadastro.value = true
                    preferenciasUtils.salvarTexto(FormularioCadastro.cadastro.CNPJ, ProjetoStrings.cnpjLogin)

                }else{
                    _fazCadastro.value = false
                }
            }
        }
    }
}