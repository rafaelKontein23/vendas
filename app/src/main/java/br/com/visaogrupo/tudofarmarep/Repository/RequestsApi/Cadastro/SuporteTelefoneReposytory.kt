package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaApi
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaTelefoneSuporte
import br.com.visaogrupo.tudofarmarep.Utis.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utis.descritar
import br.com.visaogrupo.tudofarmarep.Utis.incriptar
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class SuporteTelefoneReposytory {

    fun buscarNumeroTelefoneSuporte():RespostaTelefoneSuporte?{
        try{
            val retrofitWs = RetrofitWs().createService(SincronoCadastro::class.java)
            val json = JSONObject().apply {
                put("param", "")
            }
            val jsonString = json.toString().incriptar()
            val mediaType = MediaType.parse("application/json")
            val requestBody = RequestBody.create(mediaType,jsonString)
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