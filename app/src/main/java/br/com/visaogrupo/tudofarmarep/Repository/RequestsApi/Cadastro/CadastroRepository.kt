package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.descritar
import br.com.visaogrupo.tudofarmarep.Utils.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class CadastroRepository(context: Context) {

    val retrofit = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun enviaCadastro(cadastroRequest: CadastroRequest){
        try {

            val jsonCadastro = Gson().toJson(cadastroRequest).toString()
            val jsonChave = "$jsonCadastro${jsonCadastro}".incriptar()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonChave.toRequestBody(mediaType)
            val reponse = retrofit.P_Cadastro(requestBody).execute()
            if(reponse.isSuccessful){
                Log.d("Sucesso", "sucesso, no Envio do Cadastro")

            }else{
                if(reponse.errorBody() != null){
                    Log.d("error cadastro",    reponse.errorBody()!!.string())
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}