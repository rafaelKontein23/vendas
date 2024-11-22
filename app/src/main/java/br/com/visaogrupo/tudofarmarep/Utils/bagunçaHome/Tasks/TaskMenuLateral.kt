package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.Agrupado
import br.com.visaogrupo.tudofarmarep.Objetos.Menulateral
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support

import com.google.android.gms.tasks.Task
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskMenuLateral {

    fun buscaItensMenu(representante_id:Int):ArrayList<Menulateral>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)

            val jsonObject = JSONObject()
            jsonObject.put("Representante_id", representante_id)
            val body = Support.CRIPTHO.encode(jsonObject.toString(), Criptho.BASE64_MODE)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_Portal_Funcionalidades_Lista(requestBody)
            val response = request.execute()
            val listaMenuLateral = ArrayList<Menulateral>()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val jsonDados = jsonResponse.getJSONObject("Dados")
                val jsonItens = jsonDados.getJSONArray("Arquivo")
                for ( i in 0 until jsonItens.length()){
                    val jsonItem = jsonItens.getJSONObject(i)
                    val link =  if (jsonItem.has("Link"))  jsonItem.getString("Link") else "";
                    val funcionalidade = jsonItem.getInt("Funcionalidade_id");
                    val iconeClass = jsonItem.getString("IconeClass");
                    val titulo = jsonItem.getString("Titulo");
                    val agrupado = jsonItem.getBoolean("Agrupado");
                    val status = jsonItem.getInt("Status_cod");
                    val clicavel = jsonItem.getInt("Clicavel");
                    val ordenacao = jsonItem.getInt("Ordenacao");
                    val agrupados = jsonItem.getJSONArray("Agrupados");
                    val mensagem = if (jsonItem.has("Mensagem")) {
                        jsonItem.getString("Mensagem")
                    } else {
                        "Mensagem não disponível"
                    }
                    val menuLateral = Menulateral(agrupado,  clicavel, funcionalidade, iconeClass, link, mensagem, ordenacao, status, titulo )
                    if (!agrupado){
                        listaMenuLateral.add(menuLateral)
                    }

                    if (agrupados.length() > 1){
                        for (j in 0 until agrupados.length()){
                            val jsonAgrupado = agrupados.getJSONObject(j)
                            val linkAgrupado = if (jsonAgrupado.has("Link"))  jsonAgrupado.getString("Link") else "";
                            val funcionalidadeAgrupado = if (jsonAgrupado.has("Funcionalidade_id"))  jsonAgrupado.getInt("Funcionalidade_id") else 0;
                            val iconeClassAgrupado = if (jsonAgrupado.has("IconeClass")) jsonAgrupado.getString("IconeClass") else ""
                            val tituloAgrupado = if (jsonAgrupado.has("Titulo")) jsonAgrupado.getString("Titulo") else ""
                            val agrupadoAgrupado = if (jsonAgrupado.has("Agrupado")) jsonAgrupado.getBoolean("Agrupado") else false
                            val statusAgrupado = if (jsonAgrupado.has("Status_cod")) jsonAgrupado.getInt("Status_cod") else 0
                            val clicavelAgrupado = if (jsonAgrupado.has("Clicavel")) jsonAgrupado.getInt("Clicavel") else 0
                            val ordenacaoAgrupado = if (jsonAgrupado.has("Ordenacao")) jsonAgrupado.getInt("Ordenacao") else 0

                            var agrupado = Menulateral(agrupadoAgrupado,  clicavelAgrupado, funcionalidadeAgrupado, iconeClassAgrupado, linkAgrupado, mensagem, ordenacao, statusAgrupado, tituloAgrupado )
                            if(!agrupado.Link.isEmpty()){
                                listaMenuLateral.add(agrupado)

                            }

                        }
                    }
                }
                listaMenuLateral.sortBy { it.Ordenacao }
                return  listaMenuLateral
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

    }
}