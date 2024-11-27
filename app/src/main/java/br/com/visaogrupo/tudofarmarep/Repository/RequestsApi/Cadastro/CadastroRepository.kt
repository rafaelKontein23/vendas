package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import android.util.Base64
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class CadastroRepository(context: Context) {
    val contextUser  = context

    val retrofit = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun enviaCadastro(){
        try {
            val jsonAreaAtucao = Gson().toJson(FormularioCadastro.cadastroRequestAreaAtuacal).toString()  ?: ""
            val jsonCadastro = Gson().toJson(FormularioCadastro.cadastro).toString()

            val jsonChave = JSONObject().apply {
                put("JsonCadastro", jsonCadastro)
                put("JsonAreaAtuacao", jsonAreaAtucao)
            }


            val jsonChaveIncript = jsonChave.toString().incriptar()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonChaveIncript.toRequestBody(mediaType)
            val reponse = retrofit.P_Cadastro(requestBody).execute()
            if(reponse.isSuccessful){
                Log.d("Sucesso", "sucesso, no Envio do Cadastro")

            }else{
                if(reponse.errorBody() != null){
                    Log.d("error cadastro",    reponse.errorBody()!!.string())
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }

    fun envioFotoDocumentoBase64(base64: String, nomeArquivo: String):Boolean{
        try {
            val jsonFoto = JSONObject().apply {
                put("file", base64)
                put("filename", nomeArquivo)
                put("path", "")
            }
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonFoto.toString().toRequestBody(mediaType)
            val reponse = retrofit.enviaFotoDocumento(requestBody).execute()
            if(reponse.isSuccessful){
                Log.d("Sucesso", "sucesso, no Envio da Foto")
                return true
            }else{
                return false
            }

        }catch (e: Exception){
            e.printStackTrace()
            return false
        }catch (e:IOException){
            e.printStackTrace()
            return false

        }
    }

    fun enviaCadastroFinal() :Boolean{
        try {
            val jsonAreaAtucao = Gson().toJson(FormularioCadastro.cadastroRequestAreaAtuacal) ?: ""
            val jsonCadastro = Gson().toJson(FormularioCadastro.cadastro)

            val jsonChave = JSONObject().apply {
                put("JsonCadastro", JSONObject(jsonCadastro))  // Aqui você converte a string para um objeto JSON
                put("JsonAreaAtuacao", JSONObject(jsonAreaAtucao))  // Aqui você faz o mesmo
            }


            val jsonChaveIncript = jsonChave.toString().incriptar()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonChaveIncript.toRequestBody(mediaType)
            val reponse = retrofit.P_Cadastro(requestBody).execute()
            if(reponse.isSuccessful){
                val responsestr  = reponse.body()!!.string().descritar()
                Log.d("Sucesso", "sucesso, no Envio do Cadastro")

                return true


            }else{
                if(reponse.errorBody() != null){
                    Log.d("error cadastro",    reponse.errorBody()!!.string())
                }
                return false
            }
        }catch (e: Exception){
            e.printStackTrace()
            return false
        }catch (e:IOException){
            e.printStackTrace()
            return false
        }
    }

    fun enviaAssinatura(base64: String, nomeArquivo: String):Boolean{
        try {
            val jsonAssinatura = JSONObject().apply {
                put("file", base64)
                put("filename", nomeArquivo)
                put("path", "assinatura")

            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonAssinatura.toString().toRequestBody(mediaType)
            val reponse = retrofit.enviaAssinatura(requestBody).execute()
            if(reponse.isSuccessful){
                Log.d("Sucesso", "sucesso, no Envio da Assinatura")
                return true
            }else{
                return  false
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