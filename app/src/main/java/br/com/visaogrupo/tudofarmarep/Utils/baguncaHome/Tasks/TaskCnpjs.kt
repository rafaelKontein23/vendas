package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.Atributo
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject


class TaskCnpjs {
    suspend  fun buscaCnpjs(representanteID: Int = 0, lat:String = "", long:String = "", isProximo:Boolean = false, isBuscaLocal:Boolean = false, buscastr:String = "", minhaCarteira:Boolean = false): Pair <ArrayList<Cnpj>, ArrayList<Atributo> >{
        try {


            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonListaCnpj = JSONObject()
            jsonListaCnpj.put("Representante_id", representanteID)
            jsonListaCnpj.put("Carteira",minhaCarteira)
            jsonListaCnpj.put("Latitude", long)
            jsonListaCnpj.put("Longitude", lat)
            if (isBuscaLocal){
                jsonListaCnpj.put("Busca", buscastr)
            }

            val body = Support.CRIPTHO.encode(jsonListaCnpj.toString(), Criptho.BASE64_MODE)
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_ListaEmpresas(requestBody)
            val response = request.execute()
            val listaCnpj = ArrayList<Cnpj>()
            val listaAtributos = ArrayList<Atributo>()
            if (response.isSuccessful){
                val responseBody = response.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val jsonDados = jsonResponse.getJSONObject("Dados")
                val jsonCnpjs = jsonDados.getJSONArray("Empresas")
                for ( i in 0 until  jsonCnpjs.length()){
                    val jsonCnpj = jsonCnpjs.getJSONObject(i)
                    val cnpj = jsonCnpj.getString("CNPJ")
                    val razaoSocial = jsonCnpj.getString("RazaoSocial")
                    val endereco = jsonCnpj.getString("Endereco")
                    val Carteira = jsonCnpj.getBoolean("Carteira")
                    val numero = jsonCnpj.getString("Numero")
                    val complemento = jsonCnpj.getString("Complemento")
                    val cidade = jsonCnpj.getString("Cidade")
                    val estado = jsonCnpj.getString("UF")
                    val bairro = jsonCnpj.getString("Bairro")
                    val distancia = jsonCnpj.getString("Distancia")
                    val jsonAtribudos = jsonCnpj.getJSONArray("Atributos")
                    val listaAtribudo = ArrayList<Atributo>()
                    for (j in 0 until jsonAtribudos.length()){
                        val jsonAtributo = jsonAtribudos.getJSONObject(j)
                        val atributo = jsonAtributo.getString("Atributo")
                        var imagem = jsonAtributo.getString("Imagem")
                        imagem = URLs.urlImagensCnpjs + imagem
                        val atributoObj = Atributo(atributo,imagem)
                        listaAtribudo.add(atributoObj)
                        if (!listaAtributos.contains(atributoObj)){
                            listaAtributos.add(atributoObj)
                        }
                    }
                    val cnpjs = Cnpj(cnpj,cidade,complemento,distancia,endereco,numero,razaoSocial,estado,listaAtribudo, carteira = Carteira, bairro = bairro)
                    listaCnpj.add(cnpjs)


                }
                return Pair( listaCnpj, listaAtributos)

            }else{
                return Pair(ArrayList(), ArrayList())
            }
        }catch (e:Exception){
            e.printStackTrace()
            return Pair(ArrayList(), ArrayList())
        }catch (e:IOException){
            e.printStackTrace()
            return Pair(ArrayList(), ArrayList())
        }
    }
}