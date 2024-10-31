package br.com.visaogrupo.tudofarmarep.Utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PreferenciasUtils (val context: Context) {

    fun salvarTexto( texto: String, chave: String) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(chave, texto)
        editor.apply()
    }

    fun recuperarTexto(chave: String, valorDefault: String = ""): String? {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(chave, valorDefault)
    }


}