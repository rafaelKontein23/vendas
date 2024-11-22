package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import android.util.Log
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.OperadorLogistico
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

class TaskOperadorXLoja {
    fun buscaOperadorxLoja(lojaID:Int, UF:String):ArrayList<OperadorLogistico>{
        try {
            val isycOperador =  RetrofitWS().createService(Isync::class.java)
            val jsonOperadores = JSONObject()
            jsonOperadores.put("Loja_id", lojaID)
            jsonOperadores.put("UF", UF)
            val corpoIncriptado =  Support.CRIPTHO.encode(jsonOperadores.toString(), Criptho.BASE64_MODE)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, corpoIncriptado)
            val rquest = isycOperador.P_Carrinho_OperadoresXLojas(requestBody)
            val response = rquest.execute()
            val listaOperador = ArrayList<OperadorLogistico>()
            if(response.isSuccessful){
                val body = response.body()!!.string()
                val json = JSONObject(Support.CRIPTHO.decode(body, Criptho.BASE64_MODE))
                val jsonDados = json.getJSONObject("Dados")
                val jsonOperadorXLoja = jsonDados.getJSONArray("OperadoresXLojas")
                for (i in 0 until jsonOperadorXLoja.length()){
                    val jsonOperador = jsonOperadorXLoja.getJSONObject(i)
                    val operadorLogisticoGrupoID = jsonOperador.getInt("OperadorLogistico_Grupo_ID")
                    val operadorLogisticoID = jsonOperador.getInt("OperadorLogistico_ID")
                    val nome = jsonOperador.getString("Nome")
                    val operadorLogisco = OperadorLogistico(operadorLogisticoGrupoID, operadorLogisticoID, nome)
                    listaOperador.add(operadorLogisco)

                }
            }else{
                return listaOperador
            }
            return  listaOperador
        }catch (e:Exception){
            e.printStackTrace()
            return ArrayList()
        }

    }
}