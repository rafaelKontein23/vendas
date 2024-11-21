package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.LoginRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCodigoIndicacaoDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaLogin
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaLoginDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginRepository(
    context: Context
) {
    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

     fun logaUsuario(loginRequest: LoginRequest) : RespostaLogin? {
        try {
            val jsonLogin = Gson().toJson(loginRequest).toString().incriptar()
            val requestBody = jsonLogin.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val response =  retrofitWs.P_Login(requestBody).execute()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string().descritar()
                val gson = Gson().fromJson(responseBody, RespostaLoginDados::class.java)
                val dados = gson.Dados
                return dados.first()
            }else{
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