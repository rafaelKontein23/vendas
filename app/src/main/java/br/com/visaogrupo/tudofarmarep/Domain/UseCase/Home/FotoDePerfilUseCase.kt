package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.FotoPerfilRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.SincronoHome
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ImagensUltis

class FotoDePerfilUseCase (val context: Context, val repository: FotoPerfilRepository) {

    suspend fun mandaFotoPerfilCaminho(nomeArquivo: String):Boolean{
        var base64 = ""
        if (FormularioCadastro.base64Galeria.isEmpty()){
            base64 = ImagensUltis.uriToBase64WithResolver(context = context, FormularioCadastro.fotoPerfil).toString()

        }else{
            base64 = FormularioCadastro.base64Galeria
        }
         val response = repository.enviaFotoPerfil(nomeArquivo,  base64)
         return response
    }
}