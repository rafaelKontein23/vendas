package br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome

import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitWS {


    var url: String = URLs.urlWs

    var client: OkHttpClient = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build()

    var builder: Retrofit.Builder = Retrofit.Builder().client(client).baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())

    var retrofit: Retrofit = builder.build()

    fun <S> createService(serviceClass: Class<S>?): S {
        return retrofit.create(serviceClass)
    }
}