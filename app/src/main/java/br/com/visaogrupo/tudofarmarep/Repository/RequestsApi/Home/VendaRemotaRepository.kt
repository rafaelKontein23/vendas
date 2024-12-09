package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.HashVendaRemotaRequest
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar

import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject

class VendaRemotaRepository  (context: Context){
    val retrofitWS = RetrofitWs(context).createService(SincronoHome::class.java)

    fun constroiHash( representante_id: HashVendaRemotaRequest): String {
        try {
            val json = Gson().toJson(representante_id).toString()
            val body = json.incriptar()
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = body.toRequestBody(mediaType)
            val response = retrofitWS.P_VendaRemota_GerarMeuHaskLink(requestBody).execute()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val hash = jsonResponse.getString("Dados")
                return hash;
            }else{
                return ""
            }
        }catch (e:Exception){
            return ""
        }catch (e:IOException){
            return ""
        }
    }
}

