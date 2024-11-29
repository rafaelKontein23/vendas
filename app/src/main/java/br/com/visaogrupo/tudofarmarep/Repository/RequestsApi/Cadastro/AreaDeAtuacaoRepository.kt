package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.AreaAtuacaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CidadesRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.MessoRegiaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaAreaAtuacaoCadastrais
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaAreaAtuacaoCadastraisDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidades
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidadesDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiaoDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AreaDeAtuacaoRepository(context: Context) {

    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

   suspend fun recuperaDadosMesorregiao(messoRegiaoRequest: MessoRegiaoRequest) : ArrayList<RespostaMessoRegiao>?{
        try{
            val json = Gson().toJson(messoRegiaoRequest)
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

    fun recuperaDadosCastraisAreaAtuacao(areaAtuacaoRequest: AreaAtuacaoRequest):ArrayList<RespostaAreaAtuacaoCadastrais>{
        try{
            val jsonRepresentanteId= Gson().toJson(areaAtuacaoRequest).toString() ?: ""
            val jsonChaveIncript = jsonRepresentanteId.incriptar()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonChaveIncript.toRequestBody(mediaType)
            val response = retrofitWs.P_ListaAreaAtuacao(requestBody).execute()
            if(response.isSuccessful){
                val resposta = response.body()!!.string().descritar()
                val  respostaAreaAtuacao = Gson().fromJson(resposta, RespostaAreaAtuacaoCadastraisDados::class.java)
                return respostaAreaAtuacao.Dados as ArrayList<RespostaAreaAtuacaoCadastrais>

            }else{
                if(response.errorBody() != null){
                    Log.d("error ListaAreaAtuacao", response.errorBody()!!.string())
                }
                return arrayListOf()
            }
        }catch (e: Exception){
            e.printStackTrace()
            return arrayListOf()
        }catch (e:IOException){
            e.printStackTrace()
            return arrayListOf()

        }
    }

}