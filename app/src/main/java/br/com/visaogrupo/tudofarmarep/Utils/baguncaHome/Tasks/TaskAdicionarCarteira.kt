package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class TaskAdicionarCarteira {

    suspend fun adicionarCarteira(representanteID:Int, cnpj:String, statusCarteira : Int) : Boolean{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val json = JSONObject().apply {
                put("Representante_ID", representanteID)
                put("CNPJ", cnpj)
                put("StatusCarteira", statusCarteira)
            }
            val body = Support.CRIPTHO.encode(json.toString(), Criptho.BASE64_MODE)
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_GerenciaCarteira(requestBody).execute()
            if (request.isSuccessful){
                val responseBody = request.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                return true

            }else{
                return false
            }


        }catch (e:Exception){
            e.printStackTrace()
            return false

        }catch (i: IOException){
            i.printStackTrace()
            return  false
        }
    }
}