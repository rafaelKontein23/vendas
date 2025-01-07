package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosCnpjFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosPessoaisFragment
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal

class ViewModelActCabecalho :ViewModel(){
    private val _mostraCareegando = MutableLiveData<Boolean>()
    val mostraCarregando: LiveData<Boolean> get() = _mostraCareegando
    private val _finalizaAtividade = MutableLiveData<Boolean>()
    val finalizaAtividade: LiveData<Boolean> get() = _finalizaAtividade
    private val _passoAtual = MutableLiveData<Pair<Int, Float>>()
    val passoAtual: LiveData<Pair<Int, Float>> get() = _passoAtual
    private val _infoVisvelFragement = MutableLiveData<DadosPessoaisFragment>()
    val infoVisvelFragement : LiveData<DadosPessoaisFragment> get() = _infoVisvelFragement
    private val _infoVisvelFragementCnpj = MutableLiveData<DadosCnpjFragment>()
    val infoVisvelFragementCnpj : LiveData<DadosCnpjFragment> get() = _infoVisvelFragementCnpj

    var cadastro: CadastroRequest = CadastroRequest()
    var cadastroRequestAreaAtuacal: CadastroRequestAreaAtuacal = CadastroRequestAreaAtuacal()



    fun mostraCarregando(mostra: Boolean){
        _mostraCareegando.postValue(mostra)
    }
    fun finalizaAtividade(){
        _finalizaAtividade.postValue(true)

    }

    fun mudaInfoVisivel( dadosPessoaisFragment :DadosPessoaisFragment){
        _infoVisvelFragement.postValue( dadosPessoaisFragment)
    }
    fun mudainfoVisivelCnpj(fragment: DadosCnpjFragment){
        _infoVisvelFragementCnpj.postValue(fragment)
    }

    fun mudaProgressoCadastro(passo:Int, progress:Float){
        _passoAtual.postValue(Pair(passo, progress))
    }

}