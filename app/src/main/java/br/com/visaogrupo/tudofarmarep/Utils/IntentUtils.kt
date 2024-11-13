package br.com.visaogrupo.tudofarmarep.Utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class IntentUtils {
    companion object{
        fun mandaParaWhatsApp(context: Context, numero: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(numero)
            context.startActivity(intent)
        }
    }

}