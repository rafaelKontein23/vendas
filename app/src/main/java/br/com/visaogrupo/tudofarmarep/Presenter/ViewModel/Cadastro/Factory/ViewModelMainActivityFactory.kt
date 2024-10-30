package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory

class ViewModelMainActivityFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMainActivity::class.java)) {
            val repository = SuporteTelefoneReposytory(context)
            return ViewModelMainActivity(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}