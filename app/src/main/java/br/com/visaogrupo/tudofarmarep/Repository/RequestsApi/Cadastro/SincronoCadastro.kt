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

    @POST("tudofarmarep/dados/empresa")
    fun P_Portal_Cadastro_Consulta
    (@Body responseBody: RequestBody): Call <ResponseBody>

    @POST("envia/cadastro")
    fun P_Cadastro
    (@Body responseBody: RequestBody): Call <ResponseBody>

    @POST("tudofarmarep/obtem/mesorregioes")
    fun P_Portal_Cadastro_Mesorregioes_Lista
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("tudofarmarep/retorna/cidades/uf")
    fun P_Mobile_Cadastro_Cidade_Consulta
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("indicacao/hash")
    fun P_Convidado_Hash_Consulta
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("envia/foto/documento")
    fun enviaFotoDocumento
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("envio/assinatura")
    fun enviaAssinatura
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("loga/usuario")
    fun P_Login
    (@Body responseBody: RequestBody): Call<ResponseBody>


}