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

    fun salvarBool( isItem: Boolean, chave: String) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(chave, isItem)
        editor.apply()
    }

    fun salvaInteiro(valor: Int, chave: String){
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(chave, valor)
        editor.apply()
    }
    fun recuperarInteiro(chave: String, valorDefault: Int = 0): Int {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(chave, valorDefault)
    }

    fun recuperarTexto(chave: String, valorDefault: String = ""): String? {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(chave, valorDefault)
    }

    fun recuperarBool(chave: String, valorDefault: Boolean = false): Boolean {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(chave, valorDefault)
    }
}