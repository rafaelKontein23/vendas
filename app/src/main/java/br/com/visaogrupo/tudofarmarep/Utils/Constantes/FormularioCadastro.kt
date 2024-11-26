package br.com.visaogrupo.tudofarmarep.Utils.Constantes

import android.graphics.Bitmap
import android.net.Uri
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal

class FormularioCadastro {

    companion object{
       var cadastro = CadastroRequest()
       var cadastroRequestAreaAtuacal = CadastroRequestAreaAtuacal()
       var fotoDocumeto:Uri = Uri.EMPTY
       var base64Galeria = ""
       var base64Assinatura = ""
        var savedBitmap: Bitmap? = null


        fun limpaCadastro(){
            cadastro = CadastroRequest()
            cadastroRequestAreaAtuacal = CadastroRequestAreaAtuacal()
            fotoDocumeto = Uri.EMPTY
            base64Galeria = ""
            base64Assinatura = ""
            savedBitmap = null

        }
    }
}