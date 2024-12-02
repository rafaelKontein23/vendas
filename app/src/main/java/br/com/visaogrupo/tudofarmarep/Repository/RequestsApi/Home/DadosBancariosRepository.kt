package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.DadosBancariosRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaDadosBancariosDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancaria
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancariaDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class DadosBancariosRepository(
    val context: Context
) {
    val retrofit = RetrofitWs(context).createService(SincronoHome::class.java)

     fun buscaDadosBancarios(
        dadosBancarios: DadosBancariosRequest
     ): RespostaDadosBancarios? {
         try {
             val json = Gson().toJson(dadosBancarios).toString().incriptar()
             val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
             val requestBody = json.toRequestBody(mediaType)
             val response = retrofit.P_ListaDadosBancarios(requestBody).execute()
             if (response.isSuccessful){
                 val body = response.body()!!.string().descritar()
                 val dados = Gson().fromJson(body, RespostaDadosBancariosDados::class.java)
                 if (dados.Dados.isEmpty()){
                     val dadosItem = RespostaDadosBancarios(
                         "", "", "", "", "", 0
                     )
                     return dadosItem
                 }else{
                     val dadosBancarios = dados.Dados.first()
                     return dadosBancarios
                 }
             }else{
                 return null
             }
         }catch (e:Exception){
             e.printStackTrace()
             return null

         }catch (i:IOException) {
             i.printStackTrace()
             return null

         }
     }

    fun buscaInstituicaoBancaria():ArrayList< RespostaInstituicaoBancaria?> {
        try {
            val json = JSONObject().apply {
                put("Sucesso", 1)
            }
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = json.toString().incriptar().toRequestBody(mediaType)
            val response = retrofit.P_ListaInstituicoesBancarias(requestBody).execute()
            if(response.isSuccessful){
                val body = response.body()!!.string().descritar()
                val dados = Gson().fromJson(body, RespostaInstituicaoBancariaDados::class.java)
                return dados.Dados as ArrayList<RespostaInstituicaoBancaria?>
            }else{
                return ArrayList()


            }
        }catch (e:Exception){
            e.printStackTrace()
            return ArrayList()

        }catch (e:IOException){
            e.printStackTrace()
            return ArrayList()

        }
    }
}