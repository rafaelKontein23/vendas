package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.SolicitaTokenRquest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaApiDadosToken
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaSolicitaToken
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaTelefoneSuporte
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.descritar
import br.com.visaogrupo.tudofarmarep.Utils.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TokenRepository(context: Context) {
    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun solicitaTokenReposiory(solicitaTokenRquest: SolicitaTokenRquest) :RespostaSolicitaToken?{
        try {
            val json = Gson().toJson(solicitaTokenRquest).toString().incriptar()
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = json.toRequestBody( mediaType)
            val response = retrofitWs.P_Portal_Cadastro_SolicitaToken(requestBody).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()?.string()?.descritar()
                val gson = Gson()
                val respostaApi = gson.fromJson(responseBody, RespostaApiDadosToken::class.java)
                val dados : List<RespostaSolicitaToken> = respostaApi.Dados
                val repostaSolicitaToken = dados.first()
                return repostaSolicitaToken
            }else{
                return  null
            }
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }catch (e: IOException){
            e.printStackTrace()
            return null
        }
    }
}