package br.com.visaogrupo.tudofarmarep.Utils.baguncaHome

import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCliente {


    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URLs.urlMarcas)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()) // Converter factory for JSON responses
        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}