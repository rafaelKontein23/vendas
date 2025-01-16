package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCnpjDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaNotificacao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaNotificacaoDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.NotificacaoRequest
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class NotificacaoRepository(context: Context) {
    val retrofit = RetrofitWs(context).createService(SincronoHome::class.java)
    fun buscaNotificacoes(notificacaoRequest: NotificacaoRequest):ArrayList<RespostaNotificacao>{
        try {
            val gson = Gson().toJson(notificacaoRequest)
            val jsonCriptado = gson.toString().incriptar()
            val mediaType = "json/application".toMediaType()
            val body = jsonCriptado.toRequestBody(mediaType)
            val response = retrofit.P_ListaNotificacoes(body).execute()
            if (response.isSuccessful){
                var jsonRespostaNotificacao = response.body()!!.string().descritar()
                val gson = Gson()
                val respostaApi = gson.fromJson(jsonRespostaNotificacao, RespostaNotificacaoDados::class.java)
                val dadosNotificacao: ArrayList<RespostaNotificacao> = respostaApi.Dados
                return dadosNotificacao
            }else{
                return arrayListOf()

            }
        }catch (e:Exception){
            e.printStackTrace()
            return arrayListOf()

        }catch (e:IOException){
            e.printStackTrace()
            return arrayListOf()
        }


    }
}