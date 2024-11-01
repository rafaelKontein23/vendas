package br.com.visaogrupo.tudofarmarep.Utils

import android.content.Context
import android.provider.Settings

class SistemaUtils(private val context: Context) {

    fun recuperaUdid(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

}
