package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Menu
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterItenCnpj
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterMenuLateral
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaWebView
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Objetos.Menulateral
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.R

class DialogMenuLateral {

    fun dialogMenu(context: Context, ListaMenuLateral: ArrayList<Menulateral>,  atualizaWebView: AtualizaWebView,  nomeRepresetanteText : String, cnpjRepresentanteText : String) {
        var nomeRepresetanteTextFormat = nomeRepresetanteText
        val dialogMenuLateral = Dialog(context)
        dialogMenuLateral.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogMenuLateral.setContentView(R.layout.dialog_menu_lateral)

        dialogMenuLateral.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogMenuLateral.getWindow()?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialogMenuLateral.window!!.attributes.windowAnimations = R.style.DialoAnimationMenu
        dialogMenuLateral.window!!.setGravity(Gravity.LEFT)
        dialogMenuLateral.show()

        var recymenuLateral = dialogMenuLateral.findViewById<RecyclerView>(R.id.RecymenuLateral)
        var nomeRepresentante = dialogMenuLateral.findViewById<TextView>(R.id.nomeRepresentante)
        val cnpjRepresentante = dialogMenuLateral.findViewById<TextView>(R.id.cnpjRepresentante)
        if (nomeRepresetanteTextFormat.length >= 15){
            nomeRepresetanteTextFormat = nomeRepresetanteTextFormat.substring(0,14) + "..."
        }

        nomeRepresentante.text = nomeRepresetanteTextFormat
        cnpjRepresentante.text = FormatarTexto().formatCNPJ(cnpjRepresentanteText)
        val sair = dialogMenuLateral.findViewById<TextView>(R.id.sair)
        val sairIcone = dialogMenuLateral.findViewById<ImageView>(R.id.sairIcone)
        val adapterMenuLateral = AdapterMenuLateral(ListaMenuLateral, atualizaWebView, dialogMenuLateral)
        var linearLayout = LinearLayoutManager(context)

        sair.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        sairIcone.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        recymenuLateral.layoutManager = linearLayout
        recymenuLateral.adapter = adapterMenuLateral
        recymenuLateral.setHasFixedSize(true)

    }
}