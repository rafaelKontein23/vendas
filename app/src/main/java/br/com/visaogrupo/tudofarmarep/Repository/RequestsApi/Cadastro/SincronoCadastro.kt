package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SincronoCadastro {

    @POST("tudofarmarep/retorna/link/suporte/whatsapp")
    fun P_Mobile_Suporte_LinkZap_Lista
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("tudofarmarep/solicita/token/celular")
    fun P_Portal_Cadastro_SolicitaToken
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("tudofarmarep/confirma/token")
    fun P_Portal_Cadastro_ConfirmaToken
    (@Body responseBody: RequestBody): Call <ResponseBody>

}