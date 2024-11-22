package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskEnviaPedido {
   suspend fun enviaPedido(jsonPeido: String):Pair<Boolean, String>{
       try {
           val isync = RetrofitWS().createService(Isync::class.java)
           val jsonObject = JSONObject()
           jsonObject.put("JSON",jsonPeido )
           val body = Support.CRIPTHO.encode(jsonObject.toString(), Criptho.BASE64_MODE)
           val mediaType = "application/json".toMediaTypeOrNull()
           val requestBody = RequestBody.create(mediaType, body)
           val request = isync.P_Pedido_EntradaMobile(requestBody)
           val response = request.execute()
           if (response.isSuccessful) {
               val responseBody = response.body()!!.string()
               val responseJson = Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE)
               val jsonObjectResponse = JSONObject(responseJson)
               val mensagem = jsonObjectResponse.getString("Mensagem")
               if (mensagem == "OK") {
                   return Pair(true, jsonObjectResponse.getString("Dados"))
               }else {
                   return Pair(false,  jsonObjectResponse.getString("Dados"))
               }
           }else {
               return Pair(false,  response.message())
           }
       }catch (e:Exception){
           e.printStackTrace()
           return Pair(false,  "Erro ao enviar pedido")
       }catch (e:IOException){
           e.printStackTrace()
           return Pair(false,  "Erro ao enviar pedido")
       }
   }
}