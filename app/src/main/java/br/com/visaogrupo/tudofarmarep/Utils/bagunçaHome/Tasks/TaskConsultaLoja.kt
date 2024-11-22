package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Adapter.AdapterItenCnpj
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.LojaComparador
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.incriptar
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskConsultaLoja {
    fun  buscaLojaDisponivel(cnpj: String , reprsentanteID:Int):ArrayList<LojaComparador>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonParemetro = JSONObject()
            jsonParemetro.put("CNPJ", cnpj)
            jsonParemetro.put("Representante_id", reprsentanteID)
            val body = jsonParemetro.toString().incriptar()
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val  request = isync.P_Loja_Consulta(requestBody).execute()
            val listaLojaComparador = ArrayList<LojaComparador>()
            if (request.isSuccessful){
                val response = request.body()!!.string()
                val descriptJson = Support.CRIPTHO.decode(response, Criptho.BASE64_MODE)
                val jsonDados = JSONObject(descriptJson).getJSONObject("Dados")
                val jsonArrayLojas = jsonDados.getJSONArray("Lojas")
                for( i in 0 until   jsonArrayLojas.length()){
                    val jsonLoja = jsonArrayLojas.getJSONObject(i)
                    val lojaID = jsonLoja.getInt("Loja_ID")
                    val flagFiltros = jsonLoja.getBoolean("FlagFiltros")
                    val urlLoginPortal  = if(jsonLoja.has("URL_Login_Portal"))jsonLoja.getString("URL_Login_Portal") else ""
                    val lojaComapra = LojaComparador(lojaID, flagFiltros, urlLoginPortal)
                    listaLojaComparador.add(lojaComapra)

                }
                return listaLojaComparador

            }else{
                return ArrayList()
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ArrayList()
        }catch (io:IOException){
            io.printStackTrace()
            return ArrayList()
        }

    }
}