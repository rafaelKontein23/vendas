package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskConstroiHash  (){

    fun constroiHash( representante_id: Int): String {
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val json = JSONObject()
            json.put("Representante_id", representante_id)
            val body = Support.CRIPTHO.encode(json.toString(), Criptho.BASE64_MODE)
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_VendaRemota_GerarMeuHaskLink(requestBody)
            val response = request.execute()
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

