package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SincronoHome {

    @POST("lista/dados/bancarios")
    fun P_ListaDadosBancarios
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("lista/instituicao/bancaria")
    fun P_ListaInstituicoesBancarias
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("flags")
    fun P_FeatureFlag
    (@Body responseBody: RequestBody): Call<ResponseBody>

    @POST("loiu/venda/remota/hashlink")
    fun P_VendaRemota_GerarMeuHaskLink (@Body request: RequestBody) : Call<ResponseBody>
}