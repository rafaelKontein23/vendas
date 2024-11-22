package br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.GraficoMarca

import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject


class TaskGraficoHome {

    fun buscaGraficoHome (representanteID: Int = 0, data : String = "2024-05-01"):ArrayList<GraficoMarca>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonGrafico = JSONObject()
            jsonGrafico.put("Representante_ID",representanteID)
            jsonGrafico.put("MesSelecionado",data)
            val body = Support.CRIPTHO.encode(jsonGrafico.toString(), Criptho.BASE64_MODE)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_AppHomeGraficoMarcas(requestBody)
            val response = request.execute()
            val listaGraficoMarca = ArrayList<GraficoMarca>()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val jsonDados = jsonResponse.getJSONObject("Dados")
                val jsonGrafico = jsonDados.getJSONArray("GraficoMarcas")
                for (i in 0 until  jsonGrafico.length()){
                    val jsonGraficoMarca = jsonGrafico.getJSONObject(i)
                    val pedidosRealizados = jsonGraficoMarca.getInt("PedidosRealizados")
                    val marca = jsonGraficoMarca.getString("Marca")
                    val corMarca = jsonGraficoMarca.getString("CorMarca")
                    val graficoMarca = GraficoMarca(corMarca,marca,pedidosRealizados)
                    listaGraficoMarca.add(graficoMarca)

                }
                return  listaGraficoMarca

            }else{
                return ArrayList()
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