package br.com.visaogrupo.tudofarmarep.Views.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterIconeIfos
import br.com.visaogrupo.tudofarmarep.Objetos.Atributo
import br.com.visaogrupo.tudofarmarep.R

class DialogIconesCnpjs {
    fun dialogIcones(context: Context, listaAtributos: ArrayList<Atributo>){
        val dialogIcones= Dialog(context)
        dialogIcones.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogIcones.setContentView(R.layout.dialog_icones)

        dialogIcones.show()
        dialogIcones.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogIcones.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogIcones.window!!.attributes.windowAnimations = R.style.animacaoDialog
        dialogIcones.window!!.setGravity(Gravity.BOTTOM)
        val fecharDialogModelo = dialogIcones.findViewById<android.widget.ImageView>(R.id.fecharDialogModelo)
        val recyIcones = dialogIcones.findViewById<RecyclerView>(R.id.recyIcones)
        fecharDialogModelo.setOnClickListener {
            dialogIcones.dismiss()
        }
        recyIcones.adapter = AdapterIconeIfos(listaAtributos)
        recyIcones.layoutManager = LinearLayoutManager(context)

    }
}