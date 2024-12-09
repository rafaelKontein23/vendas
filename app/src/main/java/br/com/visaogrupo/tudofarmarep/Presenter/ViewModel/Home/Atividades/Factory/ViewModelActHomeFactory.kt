package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.FotoDePerfilUseCase
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home.VendaRemotaUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.ViewModelActHome
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.FotoPerfilRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.VendaRemotaRepository
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class ViewModelActHomeFactory  (private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelActHome::class.java)) {
            val repository = SuporteTelefoneReposytory(context)
            val salavarTexto = PreferenciasUtils(context)
            val vendaRemotaRepository = VendaRemotaRepository(context)
            val vendaRemotaUseCase = VendaRemotaUseCase(vendaRemotaRepository)
            val fotoPerfilRepository = FotoPerfilRepository(context)
            val fotoDePerfilUseCase = FotoDePerfilUseCase(context, fotoPerfilRepository)
            val cadastroRepository = CadastroRepository(context)
            val sistemaUtils = SistemaUtils(context)
            val cadastroUseCase = CadastroUseCase( cadastroRepository, salavarTexto, sistemaUtils)
            return ViewModelActHome(repository,
                salavarTexto,
                vendaRemotaUseCase,
                fotoDePerfilUseCase,
                cadastroUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}