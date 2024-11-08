package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.AreaDeAtuacaoUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.CadastroUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelFragmentDadosAreaDeAtuacao(
    val areaDeAtuacaoUseCase: AreaDeAtuacaoUseCase,
    val cadastroUseCase: CadastroUseCase
): ViewModel() {
    fun buscaDadosAreaDeAtuacaoMesorregiao(UF:String){
        viewModelScope.launch(Dispatchers.IO) {
            areaDeAtuacaoUseCase.recuperaDadosMesorregiao(UF)

        }
    }

}