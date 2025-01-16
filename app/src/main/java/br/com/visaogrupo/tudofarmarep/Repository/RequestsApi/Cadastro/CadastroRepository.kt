package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import FormularioCadastro
import android.content.Context
import android.util.Base64
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro.CadastroUseCase
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCadastro
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCadastroDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCodigoIndicacaoDados
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.descritar
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class CadastroRepository(context: Context) {
    val contextUser  = context

    val retrofit = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun enviaCadastro(reprsentanteID:Int = 0, islimpaCadastroUseCase : Boolean = false, atualizaAreaDeAtuacao: Boolean = false): Boolean{
        try {
            var jsonAreaAtucao =""
            var jsonCadastro =""
            var jsonDadosBancarios =""
            if (!FormularioCadastro.cadastro.CNPJ.isEmpty()){
                jsonCadastro =  Gson().toJson(FormularioCadastro.cadastro).toString()
            }
            if (FormularioCadastro.cadastroRequestAreaAtuacal.UF != ""){
                jsonAreaAtucao = Gson().toJson(FormularioCadastro.cadastroRequestAreaAtuacal).toString()
            }
            if (FormularioCadastro.dadosBancarios.CNPJ != ""){
                jsonDadosBancarios = Gson().toJson(FormularioCadastro.dadosBancarios).toString()
            }
            if (atualizaAreaDeAtuacao){
                jsonCadastro = ""
            }


            val jsonChave = JSONObject().apply {
                put("JsonCadastro", jsonCadastro)
                put("JsonAreaAtuacao", jsonAreaAtucao)
                put("JsonDadosBancarios",jsonDadosBancarios)
                put("Representante_ID", reprsentanteID)
                put("StepId", FormularioCadastro.stepid)
            }


            val jsonChaveIncript = jsonChave.toString().incriptar()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonChaveIncript.toRequestBody(mediaType)
            val reponse = retrofit.P_Cadastro(requestBody).execute()
            if(reponse.isSuccessful){
                Log.d("Sucesso", "sucesso, no Envio do Cadastro")
                val responsestr  = reponse.body()!!.string().descritar()
                if (islimpaCadastroUseCase){
                    FormularioCadastro.limpaCadastro()
                }
                return true

            }else{
                if(reponse.errorBody() != null){
                    Log.d("error cadastro",    reponse.errorBody()!!.string())
                }
                return false

            }
        }catch (e: Exception){
            e.printStackTrace()
            return false
        }catch (e:IOException){
            e.printStackTrace()
            return false
        }
    }

    fun envioFotoDocumentoBase64(base64: String, nomeArquivo: String):Boolean{
        try {
            val jsonFoto = JSONObject().apply {
                put("file", base64)
                put("filename", nomeArquivo)
                put("path", "")
            }
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonFoto.toString().toRequestBody(mediaType)
            val reponse = retrofit.enviaFotoDocumento(requestBody).execute()
            if(reponse.isSuccessful){
                Log.d("Sucesso", "sucesso, no Envio da Foto")
                return true
            }else{
                return false
            }

        }catch (e: Exception){
            e.printStackTrace()
            return false
        }catch (e:IOException){
            e.printStackTrace()
            return false

        }
    }


    fun enviaCadastroFinal() : Boolean {
        try {
            val jsonAreaAtucao = Gson().toJson(FormularioCadastro.cadastroRequestAreaAtuacal) ?: ""
            val jsonCadastro = Gson().toJson(FormularioCadastro.cadastro)

            val jsonChave = JSONObject().apply {
                put("JsonCadastro", JSONObject(jsonCadastro))
                put("JsonAreaAtuacao", JSONObject(jsonAreaAtucao))
                put("JsonDadosBancarios", JSONObject(Gson().toJson(FormularioCadastro.dadosBancarios)))
                put("Representante_ID", 0)
                put("StepId", 6)
            }



            val jsonChaveIncript = jsonChave.toString().incriptar()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonChaveIncript.toRequestBody(mediaType)
            val reponse = retrofit.P_Cadastro(requestBody).execute()
            if(reponse.isSuccessful){
                val responsestr  = reponse.body()!!.string().descritar()
                return true
            }else{
                if(reponse.errorBody() != null){
                    Log.d("error cadastro",    reponse.errorBody()!!.string())
                }
                return false
            }
        }catch (e: Exception){
            e.printStackTrace()
            return false
        }catch (e:IOException){
            e.printStackTrace()
            return false
        }
    }

    fun enviaAssinatura(base64: String, nomeArquivo: String):Boolean{
        try {
            val jsonAssinatura = JSONObject().apply {
                put("file", base64)
                put("filename", nomeArquivo)
                put("path", "assinatura")

            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonAssinatura.toString().toRequestBody(mediaType)
            val reponse = retrofit.enviaAssinatura(requestBody).execute()
            if(reponse.isSuccessful){
                Log.d("Sucesso", "sucesso, no Envio da Assinatura")
                return true
            }else{
                return  false
            }
        }catch (e:Exception){
            e.printStackTrace()
            return false

        }catch (e:IOException){
            e.printStackTrace()
            return false
        }


    }
}