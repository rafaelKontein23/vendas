package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.descritar
import br.com.visaogrupo.tudofarmarep.Utils.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AreaDeAtuacaoRepository(context: Context) {

    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun recuperaDadosMesorregiao(UF: String){
        try{
            val json = Gson().toJson(UF).toString().incriptar()
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = json.toRequestBody(mediaType)
            val response = retrofitWs.P_Portal_Cadastro_Mesorregioes_Lista(requestBody).execute()
            if(response.isSuccessful){
                val responseBody = response.body()?.string()?.descritar()
                val banana = ""
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }


}