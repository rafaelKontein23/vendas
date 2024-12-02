package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.DadosPessoaisRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaDadosPessoais
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaDadosPessoaisDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class DadosPessoaisRepository(context: Context) {

    val reposytory = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun buscaDadosPessoaisCadastrais(representanteID:DadosPessoaisRequest): RespostaDadosPessoais? {
        try {
            val json = Gson().toJson(representanteID).toString().incriptar()
            val mediaType = "application/json".toMediaTypeOrNull()
            val body = json.toRequestBody(mediaType)
            val response = reposytory.P_ListaRepresentantes(body).execute()
            if (response.isSuccessful){
                  val  response = response.body()!!.string().descritar()
                  val  resposta = Gson().fromJson(response, RespostaDadosPessoaisDados::class.java)
                  val  dados = resposta.Dados
                  return dados.first()

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