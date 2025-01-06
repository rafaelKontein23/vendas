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
        fun mandaParaInstagram(context: Context, link: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            intent.setPackage("com.instagram.android")

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                // Instagram não está instalado, abrir no navegador
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                context.startActivity(webIntent)
            }
        }
        fun mandaParaLinkeDin(context: Context, link: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            intent.setPackage("com.instagram.android")

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                // Instagram não está instalado, abrir no navegador
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                context.startActivity(webIntent)
            }
        }
    }

}