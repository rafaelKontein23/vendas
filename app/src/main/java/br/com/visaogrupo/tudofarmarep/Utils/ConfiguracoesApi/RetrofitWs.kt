package br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitWs(context: Context) {

    private val cliente: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .apply {
            val preferenciasUtils = PreferenciasUtils(context)
            val ambiente = preferenciasUtils.recuperarTexto(ProjetoStrings.ambiente, "")
            if (!ambiente.equals("www") || !ambiente.equals("stage")) {
                addInterceptor(ChuckerInterceptor.Builder(context).build())
            }
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URLs.urlWsBase)
        .client(cliente)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

}