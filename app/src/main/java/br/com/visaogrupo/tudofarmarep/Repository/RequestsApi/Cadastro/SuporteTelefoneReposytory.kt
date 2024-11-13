package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaApi
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaTelefoneSuporte
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class SuporteTelefoneReposytory(
    private val context: Context
) {

    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun buscarNumeroTelefoneSuporte():RespostaTelefoneSuporte?{
        try{
            val json = JSONObject().apply {
                put("param", "")
            }
            val jsonString = json.toString().incriptar()
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = jsonString.toRequestBody(mediaType)
            val response = retrofitWs.P_Mobile_Suporte_LinkZap_Lista(requestBody).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()?.string()!!.descritar()
                val gson = Gson()
                val respostaApi = gson.fromJson(responseBody, RespostaApi::class.java)
                val dadosTelefoneSuporte: List<RespostaTelefoneSuporte> = respostaApi.Dados
                return dadosTelefoneSuporte.first()
            }else{
                return null
            }
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }catch (e:IOException){
            e.printStackTrace()
            return null
        }
    }
}