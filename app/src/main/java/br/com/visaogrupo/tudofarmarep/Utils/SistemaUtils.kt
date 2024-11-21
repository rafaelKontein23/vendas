package br.com.visaogrupo.tudofarmarep.Utils

import android.content.Context
import android.os.Build
import android.provider.Settings


class SistemaUtils(private val context: Context) {
     companion object{
         var deviceToken = ""
     }
    fun recuperaUdid(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun recuperaNomeDispositivo():String{
        val versao = Build.VERSION.RELEASE

        val produto = Build.PRODUCT
        val modelo = Build.MODEL
        val fabricante = Build.MANUFACTURER
        return  "$versao $produto $modelo $fabricante"

    }

}
