package br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome

import ResumoMes
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskResumoMes {

    fun buscaResumoMes(representanteID: Int = 0, data : String = "2024-08-01"): ArrayList<ResumoMes>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonResumoGanhos = JSONObject()
            jsonResumoGanhos.put("Representante_ID",representanteID )
            jsonResumoGanhos.put("MesSelecionado",data)
            val body = Support.CRIPTHO.encode(jsonResumoGanhos.toString(), Criptho.BASE64_MODE)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_AppHomeResumoMes(requestBody)
            val response = request.execute()
            val listaResumoMes = ArrayList<ResumoMes>()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val jsonDados = jsonResponse.getJSONObject("Dados")
                val jsonResumo = jsonDados.getJSONArray("ResumoMes")
                for(i in 0 until  jsonResumo.length()){
                    val jsonResumoMes = jsonResumo.getJSONObject(i)
                    val qtdeCNPJ = jsonResumoMes.getInt("QtdeCNPJ")
                    val pedidosRealizados = jsonResumoMes.getInt("PedidosRealizados")
                    val comissaoDisponivel = jsonResumoMes.getDouble("ComissaoDisponivel")
                    val taxaFaturamento = jsonResumoMes.getDouble("TaxaFaturamento")
                    val resumoMes = ResumoMes(comissaoDisponivel,pedidosRealizados,qtdeCNPJ,taxaFaturamento)
                    listaResumoMes.add(resumoMes)

                }
                return listaResumoMes
            }else{
                return  listaResumoMes

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