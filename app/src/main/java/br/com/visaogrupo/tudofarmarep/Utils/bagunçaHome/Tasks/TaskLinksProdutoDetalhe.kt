package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.LinkProduto
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskLinksProdutoDetalhe {

    fun taskLinkProdutos(barra:String, marcaID:Int):LinkProduto?{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonParemetrosLinks  = JSONObject()
            jsonParemetrosLinks.put("Barra",barra )
            jsonParemetrosLinks.put("Marca_id",marcaID )
            val body = Support.CRIPTHO.encode(jsonParemetrosLinks.toString(),  Criptho.BASE64_MODE)
            val request = RequestBody.create( "application/json".toMediaTypeOrNull(), body)
            val  response = isync.P_ListaProdutosLinks(request).execute()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string()
                val responseJson = Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE)
                val jsonObjectResponse = JSONObject(responseJson)
                val jsonDados = jsonObjectResponse.optJSONObject("Dados") ?: JSONObject()
                if (jsonDados.length() != 0) {

                    val jsonLinks = jsonDados.getJSONArray("Links")
                    val  linkProduto = LinkProduto("","","","")
                    for (i in 0 until jsonLinks.length()){
                        var jsonLink = jsonLinks.getJSONObject(i)
                        val site = jsonLink.getString("Site")
                        val media= jsonLink.getString("Midia")
                        val bula= jsonLink.getString("Bula")
                        val ficha = jsonLink.getString("Ficha")
                        linkProduto.Bula = bula
                        linkProduto.Ficha = ficha
                        linkProduto.Midia = media
                        linkProduto.Site = site
                    }
                    return linkProduto

                }else{
                    return null
                }
            }else{
                return null
            }
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }catch (e:IOException){
            e.printStackTrace()
            return null
        }

    }
}