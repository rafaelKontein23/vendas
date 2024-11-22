package br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome

import android.content.Context
import android.preference.PreferenceManager
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject


class TaskVericaCarga {
    fun verificaAtualizacaoCarga(context: Context):Boolean{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val  json = JSONObject()
            json.put("Representante_ID", 0)
            val body = Support.CRIPTHO.encode(json.toString(), Criptho.BASE64_MODE)
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_ListaCarga(requestBody).execute()
            if (request.isSuccessful){
                val responseBody = request.body()!!.string()
                val supoort = Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE)
                val jsonResponse = JSONObject(supoort).getJSONObject("Dados")
                val dataRequest = jsonResponse.getString("DataCarga")
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)

                val data = prefs.getString("DataCarga", "")
                if (data.isNullOrEmpty() || data != dataRequest) {
                    val editor = prefs.edit()
                    editor.putString("DataCarga", dataRequest)
                    editor.apply()
                    return true
                } else {
                    return false
                }

            }else{
                return false
            }
        }catch (e:Exception){
            e.printStackTrace()
            return false

        }catch (io:IOException){
            io.printStackTrace()
            return false

        }


    }
}