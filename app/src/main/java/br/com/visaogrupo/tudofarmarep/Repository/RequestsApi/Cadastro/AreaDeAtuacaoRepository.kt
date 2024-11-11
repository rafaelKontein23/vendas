package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.MessoRegiaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiaoDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.descritar
import br.com.visaogrupo.tudofarmarep.Utils.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AreaDeAtuacaoRepository(context: Context) {

    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun recuperaDadosMesorregiao(UF: MessoRegiaoRequest) : List<RespostaMessoRegiao>?{
        try{
            val json = Gson().toJson(UF)
            val  jsonIncriptado = json.incriptar()
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = jsonIncriptado.toRequestBody(mediaType)
            val response = retrofitWs.P_Portal_Cadastro_Mesorregioes_Lista(requestBody).execute()
            if(response.isSuccessful){
                val responseBody = response.body()?.string()?.descritar()
                val resposta = Gson().fromJson(responseBody, RespostaMessoRegiaoDados::class.java)
                return resposta.Dados
            }else{
                return null
            }
        }
        catch (e:Exception){
            e.printStackTrace()
            return null
        }catch (e:IOException){
            e.printStackTrace()
            return null
        }
    }


}