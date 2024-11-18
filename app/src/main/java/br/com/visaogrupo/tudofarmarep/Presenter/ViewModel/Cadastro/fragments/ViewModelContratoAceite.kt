package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class ViewModelContratoAceite(
    private val cadastroUseCase: CadastroUseCase
)  : ViewModel(){
    val  _contratoAssinado = MutableLiveData<Boolean>()
    val  contratoAssinado get() = _contratoAssinado
    fun assinaturaContrato(){
         FormularioCadastro.cadastro.isAssinaContrato = true
        _contratoAssinado.value = true
    }


    fun enviaCadastroFinal(){
        viewModelScope.launch (Dispatchers.IO) {
            var retornoFoto = false
            var retornoCadastro = false

            val tarefaCadastro = async {
                retornoCadastro = cadastroUseCase.enviaCadastroFinal()
            }
            val tarefaFoto = async {
                retornoFoto =  cadastroUseCase.mandaFotoCadastro()

            }
            tarefaCadastro.await()
            tarefaFoto.await()

            Log.d("asffsa", "retornoFoto $retornoFoto")

        }
    }
}