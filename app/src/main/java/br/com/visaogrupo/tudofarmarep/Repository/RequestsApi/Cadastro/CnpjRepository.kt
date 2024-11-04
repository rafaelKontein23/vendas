package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RepostaCnpj
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCnpjDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.descritar
import br.com.visaogrupo.tudofarmarep.Utils.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class CnpjRepository (context: Context) {
    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun buscaDadosCnpj(cnpj: String):RepostaCnpj?{
        try {
            val jsonCnpj = JSONObject().apply {
                put("CNPJ", cnpj)

            }.toString().incriptar()
            val mediaType = "application/json".toMediaTypeOrNull()
            val body = jsonCnpj.toRequestBody(mediaType)
            val response =  retrofitWs.P_Portal_Cadastro_Consulta(body).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()!!.string().descritar()
                val gson = Gson()
                val respostaApi = gson.fromJson(responseBody, RespostaCnpjDados::class.java)
                val dadosCnpj: List<RepostaCnpj> = respostaApi.Dados
                return dadosCnpj.first()
            } else {
                return  null
            }
        }catch (e:Exception){
            e.printStackTrace()
            return  null

        }catch (e:IOException){
            e.printStackTrace()
            return  null

        }
    }
}