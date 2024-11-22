package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync

import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class TaskConsultaCNPJ {

    fun consultaCNPJ(cnpj: String): Cnpj? {
        try {
            val jsonCNPJ = JSONObject()
            jsonCNPJ.put("CNPJ", cnpj)
            val body = Support.CRIPTHO.encode(jsonCNPJ.toString(), Criptho.BASE64_MODE)
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)

            val request = RetrofitWS().createService(Isync::class.java).P_Portal_Cadastro_Consulta(requestBody)!!.execute()
            if (request.isSuccessful) {
                val responseBody = request.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val jsonDados = jsonResponse.getJSONArray("Dados")

                var cnpjItem : Cnpj? = null
                 for (i in 0 until  jsonDados.length()){
                    val  jsonItem = jsonDados.getJSONObject(i)
                    val cnpj = jsonItem.getString("CNPJ")
                    val razaoSocial = jsonItem.getString("RazaoSocial")
                    val endereco = jsonItem.getString("Endereco")
                    val bairro = jsonItem.getString("Bairro")
                    val cidade = jsonItem.getString("Cidade")
                    val uf = jsonItem.getString("UF")
                     cnpjItem = Cnpj(
                        cnpj,
                        cidade,
                        "",
                        "",
                        endereco,
                        "",
                        razaoSocial,
                        uf,
                        bairro = bairro
                        )

                }
                return cnpjItem


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