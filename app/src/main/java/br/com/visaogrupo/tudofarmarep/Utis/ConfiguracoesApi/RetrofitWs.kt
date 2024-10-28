package br.com.visaogrupo.tudofarmarep.Utis.ConfiguracoesApi

import br.com.visaogrupo.tudofarmarep.Utis.Constantes.URLs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitWs {
    private val cliente = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URLs.urlWsBase)
        .client(cliente)
        .addConverterFactory(GsonConverterFactory.create()).build()

    fun <S> createService(serviceClass: Class<S>?): S {
        return retrofit.create(serviceClass)
    }

}