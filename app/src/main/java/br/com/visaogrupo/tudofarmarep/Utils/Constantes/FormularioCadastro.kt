package br.com.visaogrupo.tudofarmarep.Utils.Constantes

import android.graphics.Bitmap
import android.net.Uri
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaLogin
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaLoginDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.DadosBancariosRequests

class FormularioCadastro {

    companion object{
       var cadastro = CadastroRequest()
       var cadastroRequestAreaAtuacal = CadastroRequestAreaAtuacal()
       var dadosBancarios = DadosBancariosRequests()
       var fotoDocumeto:Uri = Uri.EMPTY
       var base64Galeria = ""
       var base64Assinatura = ""
       var savedBitmap: Bitmap? = null
        var  FeatureFlagMeuTime:Boolean = false
        var  FeatureFlagMerchan:Boolean = false


        fun limpaCadastro(){
            cadastro = CadastroRequest()
            dadosBancarios = DadosBancariosRequests()

            cadastroRequestAreaAtuacal = CadastroRequestAreaAtuacal()
            fotoDocumeto = Uri.EMPTY
            base64Galeria = ""
            base64Assinatura = ""
            savedBitmap = null
            FeatureFlagMeuTime = false
            FeatureFlagMerchan = false

        }
    }
}