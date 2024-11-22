package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import android.content.Context
import android.preference.PreferenceManager
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.CnpjsImportados
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome.Support
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class TaskImportarCarteira {
    fun importarCarteira(listaJson: MutableList<String>, context: Context):Triple<ArrayList<CnpjsImportados>, ArrayList<String>, ArrayList<String>> {
        try {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val reprsentanteID = prefs.getString("reprsentante_id", "0")!!.toInt()
            val isync = RetrofitWS().createService(Isync::class.java)
            var cnpjs = ""
            for ((position, cnpj) in listaJson.withIndex()) {
                if ((position + 1) == listaJson.size) {
                    cnpjs += "$cnpj"
                } else {
                    cnpjs += "$cnpj,"
                }
            }
            val jsonParemetros = JSONObject()
            jsonParemetros.put("Representante_id", reprsentanteID)
            jsonParemetros.put("CNPJs", cnpjs)
            val body = Support.CRIPTHO.encode(jsonParemetros.toString(), Criptho.BASE64_MODE)
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_MinhaCarteira_Importa(requestBody).execute()
            val listaImportados = ArrayList<CnpjsImportados>()
            val listaErrosEncontrado = ArrayList<String>()
            val listaErrosSituacaoCadastral = ArrayList<String>()
            if (request.isSuccessful) {
                val body = request.body()!!.string()
                val json = Support.CRIPTHO.decode(body, Criptho.BASE64_MODE)
                val jsonObject = JSONObject(json).getJSONArray("Dados") ?: JSONArray()
                if (jsonObject.length() > 0) {
                    for (i in 0 until jsonObject.length()) {
                        val item = jsonObject.getJSONObject(i)
                        val cnpj = item.getString("CNPJ")
                        val encontrado = item.getBoolean("Encontrado")
                        val situacaoCadastral = item.getBoolean("SituacaoCadastral")
                        val QtdImportada = item.getInt("QtdImportada")

                        if(!encontrado){
                            listaErrosEncontrado.add(cnpj)
                        }else if(!situacaoCadastral){
                            listaErrosSituacaoCadastral.add(cnpj)
                        }else{
                            val cnpjImportado =
                                CnpjsImportados(cnpj, encontrado, QtdImportada, situacaoCadastral)
                            listaImportados.add(cnpjImportado)
                        }
                    }
                    return  Triple(listaImportados, listaErrosEncontrado, listaErrosSituacaoCadastral)
                } else {
                    return  Triple(ArrayList(), ArrayList(), ArrayList())
                }
            } else {
                return  Triple(ArrayList(), ArrayList(), ArrayList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return  Triple(ArrayList(), ArrayList(), ArrayList())
        } catch (e: IOException) {
            e.printStackTrace()
            return  Triple(ArrayList(), ArrayList(), ArrayList())
        }
    }
}