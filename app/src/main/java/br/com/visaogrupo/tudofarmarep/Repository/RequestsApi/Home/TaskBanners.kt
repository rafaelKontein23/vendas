package br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome

import android.util.Log
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.Banners
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskBanners {

    fun buscaBanners(representanteID: Int = 0, cnpj: String = ""): ArrayList<Banners>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonBanner =JSONObject()
            jsonBanner.put("Representante_ID", representanteID)
            jsonBanner.put("CNPJ", cnpj)

            val body = Support.CRIPTHO.encode(jsonBanner.toString(), Criptho.BASE64_MODE)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val requestBanner = isync.P_AppHomeBanner(requestBody)
            val request = requestBanner.execute()
            val listaBanners = ArrayList<Banners>()
            if (request.isSuccessful){

                val response = request.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(response, Criptho.BASE64_MODE))
                Log.d("BANNER", response)
                val jsonDados = jsonResponse.getJSONObject("Dados")
                val jsonArrayBanners = jsonDados.getJSONArray("Banner")
                for (i in 0 until jsonArrayBanners.length()){
                    val jsonBanner = jsonArrayBanners.getJSONObject(i)
                    val homeBannerID = jsonBanner.getInt("HomeBanner_Id")
                    val marcaID = jsonBanner.getInt("Marca_id")
                    val imagem = jsonBanner.getString("Imagem")
                    val ordem = jsonBanner.getInt("Ordem")
                    val link = jsonBanner.getString("Link")
                    val banners = Banners(homeBannerID,marcaID,imagem, ordem, link)
                    listaBanners.add(banners)

                }
                return  listaBanners
            }else{
                return listaBanners
            }
        }catch (e:IOException){
            e.printStackTrace()
            return ArrayList()
        }catch (e:Exception){
            e.printStackTrace()
            return ArrayList()
        }
    }
}