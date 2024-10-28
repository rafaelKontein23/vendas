package br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaTelefoneSuporte
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory


class ViewModelMainActivity {

    fun buscarNumeroTelefoneSuporte():String{
        val numeroTelefone = SuporteTelefoneReposytory().buscarNumeroTelefoneSuporte()
        if (numeroTelefone != null){
            return numeroTelefone.LinkZap

        }else{
            return ""
        }
    }
}