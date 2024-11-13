package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CodigoIndicacaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCodigoIndicacao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCodigoIndicacaoDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class CodigoIndicacaoRepository(
    private val context: Context
) {
    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun cadastrarCodigoIndicacao(codigoIndicacao: CodigoIndicacaoRequest): RespostaCodigoIndicacao? {
        try {
            val json = Gson().toJson(codigoIndicacao).toString().incriptar()
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = json.toRequestBody(mediaType)
            val  response =retrofitWs.P_Convidado_Hash_Consulta(requestBody).execute()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string().descritar()
                val gson = Gson().fromJson(responseBody, RespostaCodigoIndicacaoDados::class.java)
                val dados = gson.Dados
                return dados.first()
            }else{
                return null
            }

        }catch (e: Exception){
            e.printStackTrace()
            return null

        }catch (e:IOException){
            e.printStackTrace()
            return null
        }

    }


}