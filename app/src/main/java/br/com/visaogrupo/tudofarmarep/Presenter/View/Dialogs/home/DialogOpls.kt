package br.com.visaogrupo.tudofarmarep.Views.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterOpls
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProdutosOperadores
import br.com.visaogrupo.tudofarmarep.DAO.DAOProgressiva
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Operadores
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DialogOpls {

    fun dialogOperadors(context: Context, lojas: Lojas,atualizaProdutosOperadores: AtualizaProdutosOperadores, listaOpls:ArrayList<Operadores>){
        val dialogOpl = Dialog(context)
        dialogOpl.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogOpl.setContentView(R.layout.dialog_escolha_opls)

        dialogOpl.show()
        dialogOpl.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogOpl.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogOpl.window!!.attributes.windowAnimations =R.style.animacaoDialog
        dialogOpl.window!!.setGravity(Gravity.BOTTOM)
        val  recyOpls = dialogOpl.findViewById<RecyclerView>(R.id.recyOpls)
        val fecharOpls = dialogOpl.findViewById<ImageView>(R.id.fecharOpls)

        fecharOpls.setOnClickListener {
            dialogOpl.dismiss()
        }
        CoroutineScope(Dispatchers.IO).launch {

            MainScope().launch {
                val linearLayout = LinearLayoutManager(context)
                val adapterOpls = AdapterOpls(listaOpls,dialogOpl, atualizaProdutosOperadores, lojas)
                recyOpls.layoutManager = linearLayout
                recyOpls.adapter = adapterOpls
            }
        }

    }
}