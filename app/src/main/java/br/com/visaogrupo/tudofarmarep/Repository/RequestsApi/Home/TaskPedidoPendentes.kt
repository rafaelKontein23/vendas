package br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.PedidosPendentes
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskPedidoPendentes {

    fun buscaPedidosPendentes(representanteID: Int = 0): ArrayList<PedidosPendentes>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonPedidoPendentes = JSONObject()
            jsonPedidoPendentes.put("Representante_ID",representanteID )
            val body = Support.CRIPTHO.encode(jsonPedidoPendentes.toString(), Criptho.BASE64_MODE)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_AppHomePedidosPendentesAprovacao(requestBody)
            val response = request.execute()
            val listaPedidosPendentes = ArrayList<PedidosPendentes>()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val jsonDados = jsonResponse.getJSONObject("Dados")
                val jsonArrayPedidos = jsonDados.getJSONArray("PedidosPendentesAprovacao")
                for (i in 0 until jsonArrayPedidos.length()){
                    val jsonPedido = jsonArrayPedidos.getJSONObject(i)
                    val marcaID = jsonPedido.getInt("Marca_ID")
                    val marca = jsonPedido.getString("Marca")
                    val cnpj = jsonPedido.getString("CNPJ")
                    val totalPedido = jsonPedido.getDouble("TotalPedido")
                    val dataPedidoPendentes = jsonPedido.getString("DtPedido")
                    val pedidoID = jsonPedido.getInt("Pedido_ID")
                    val pedido = PedidosPendentes(cnpj,dataPedidoPendentes,marca,marcaID,pedidoID,totalPedido)
                    listaPedidosPendentes.add(pedido)
                }
                return   listaPedidosPendentes


            }else{
                return   listaPedidosPendentes
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