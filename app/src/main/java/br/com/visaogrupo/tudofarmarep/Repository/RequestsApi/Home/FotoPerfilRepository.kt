package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class FotoPerfilRepository(context: Context) {
    val retrofitWS = RetrofitWs(context).createService(SincronoHome::class.java)

    fun enviaFotoPerfil(nomeArquivo: String, base64: String) : Boolean{
        try {
            val jsonAssinatura = JSONObject().apply {
                put("file", base64)
                put("filename", nomeArquivo)
                put("path", "")
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonAssinatura.toString().toRequestBody(mediaType)
            val reponse = retrofitWS.enviaFotoPerfil(requestBody).execute()
            if(reponse.isSuccessful){
                val responsestr  = reponse.body()!!.string()
                return true
            }else{
                return false
            }
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }catch (e:IOException){
            e.printStackTrace()
            return false
        }


    }
}