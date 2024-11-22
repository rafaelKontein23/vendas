package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.FiltroCategoria
import br.com.visaogrupo.tudofarmarep.Objetos.Filtros
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


import org.json.JSONObject

class TaskFiltroProdutos {

    fun taskFiltroProdutos(marcaID: Int, lojaId:Int):ArrayList<FiltroCategoria> {
        val listaCategoria = ArrayList<FiltroCategoria>()
        try {
            val isyncFiltro = RetrofitWS().createService(Isync::class.java)
            val jsonParam = JSONObject()
            jsonParam.put("Marca_id", marcaID)
            jsonParam.put("Loja_id", lojaId)
            val jsonParamKey = Support.CRIPTHO.encode(jsonParam.toString(), Criptho.BASE64_MODE)
            val mediaType = "application/json".toMediaTypeOrNull()
            val body = RequestBody.create(mediaType, jsonParamKey)
            val requestJson = isyncFiltro.P_Loja_Filtros_Lista(body)

            val request = requestJson.execute()
            if (request.isSuccessful) {
                val response = request.body()?.string()
                val jsonObject = JSONObject(Support.CRIPTHO.decode(response, Criptho.BASE64_MODE))
                val dadosJson = jsonObject.getJSONArray("Dados")
                for (i in 0 until dadosJson.length()) {
                    val dados = dadosJson.getJSONObject(i)
                    val arquivoString = dados.getString("Arquivo")
                    val arquivoJson = JSONObject(arquivoString)
                    val jsonArrayFiltro = arquivoJson.getJSONArray("Filtros")

                    for (j in 0 until jsonArrayFiltro.length()) {
                        val itensJson = jsonArrayFiltro.getJSONObject(j)
                        val categoriaId = itensJson.getInt("Categoria_id")
                        val categoria = itensJson.getString("Categoria")
                        val filtroCategoria = FiltroCategoria(categoria, categoriaId, emptyList())

                        val atributoArray = itensJson.getJSONArray("Atributo")
                        val listaAtributo = ArrayList<Filtros>()
                        for (k in 0 until atributoArray.length()) {
                            val atributoJson = atributoArray.getJSONObject(k)
                            val atributoId = atributoJson.getInt("Atributo_id")
                            val atributo = atributoJson.getString("Atributo_nome")
                            val filtro = Filtros(atributo, atributoId)
                            listaAtributo.add(filtro)

                        }
                        filtroCategoria.filtros = listaAtributo
                        listaCategoria.add(filtroCategoria)
                    }
                    println(dados)
                }
                println(response)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listaCategoria
    }

}