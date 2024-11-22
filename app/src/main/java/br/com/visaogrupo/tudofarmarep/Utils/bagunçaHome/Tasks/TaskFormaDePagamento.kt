package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.FormaPagamento
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitCarga
import okio.IOException
import org.json.JSONObject

class TaskFormaDePagamento {
    fun buscaFormaDePagamento():ArrayList<FormaPagamento>{
        try {
            val isync = RetrofitCarga().createService(Isync::class.java)
            val request = isync.getLojaXFormaPagamento()
            val response = request.execute()
            val listaFormaPagamento = ArrayList<FormaPagamento>()
            if(response.isSuccessful){
                val body = response.body()!!.string()
                val json = JSONObject(body)
                val lojasXFormaPagamento = json.getJSONArray("LojasXFormaPagamento")
                for (i in 0 until lojasXFormaPagamento.length()){
                    val lojaXFormaPagamento = lojasXFormaPagamento.getJSONObject(i)
                    val formaPagamentoMarcasID = lojaXFormaPagamento.getInt("FormaPagamentoMarcas_ID")
                    val descricao = lojaXFormaPagamento.getString("Descricao")

                    val lojaJSONArray = lojaXFormaPagamento.getJSONArray("Loja")
                    for (j in 0 until lojaJSONArray.length()){
                        val lojaID = lojaJSONArray.getJSONObject(j).getInt("Loja_ID")
                        val formaPagamento = FormaPagamento(formaPagamentoMarcasID,descricao,lojaID)
                        listaFormaPagamento.add(formaPagamento)
                    }

                }
                return listaFormaPagamento

            }else{
                return ArrayList()
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ArrayList()
        }catch (e:IOException){
            e.printStackTrace()
            return ArrayList()
        }
        return ArrayList()
    }
}