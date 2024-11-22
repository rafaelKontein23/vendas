package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import android.util.Log
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitCarga
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class TaskLojas {
    fun buscaLojas():JSONArray{
        try {
            val lojaSync = RetrofitCarga().createService(Isync::class.java)
            val request = lojaSync.getLoja()
            val response = request.execute()

            if(response.isSuccessful){
                val responseBody = response.body()!!.string()
                Log.d("LOG", responseBody.toString())
                val jsonLojas = JSONObject(responseBody)
                val jsonLojasArray = jsonLojas.getJSONArray("Lojas")
                return jsonLojasArray

            }else{
                return JSONArray()
            }
        }catch (e:Exception){
            e.printStackTrace()
            return JSONArray()

        }catch (e:IOException){
            e.printStackTrace()
            return JSONArray()

        }

    }
}