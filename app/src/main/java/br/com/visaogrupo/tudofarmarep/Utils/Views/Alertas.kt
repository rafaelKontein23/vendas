package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.content.Context
import androidx.appcompat.app.AlertDialog

class Alertas {
    companion object{
        fun alertaErro(context: Context, mensagem: String, titulo: String, nao:String = "", sim:String = "Ok", function: () -> Unit){
            val builder = AlertDialog.Builder(context)
            builder.setTitle(titulo)
            builder.setMessage(mensagem)
            builder.setCancelable(false)
            builder.setNegativeButton(nao){ dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton(sim) { dialog, which ->
                function()
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}