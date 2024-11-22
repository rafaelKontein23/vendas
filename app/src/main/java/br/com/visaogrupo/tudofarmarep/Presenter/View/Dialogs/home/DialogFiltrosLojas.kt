package br.com.visaogrupo.tudofarmarep.Views.dialogs

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterFiltroLojasMarcas
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaListaFiltro
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.R

class DialogFiltrosLojas {
    fun doFilter(context: Context, listLojas:ArrayList<Pair<Int, Any>>,atualizaFiltroLista:AtualizaListaFiltro){
        val dialogFiltroLoja = Dialog(context)
        dialogFiltroLoja.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFiltroLoja.setContentView(R.layout.dialog_filtra_lojas)

        dialogFiltroLoja.show()
        dialogFiltroLoja.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogFiltroLoja.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogFiltroLoja.window!!.attributes.windowAnimations = R.style.animacaoDialog
        dialogFiltroLoja.window!!.setGravity(Gravity.BOTTOM)
        val fecharModal = dialogFiltroLoja.findViewById<ImageView>(R.id.fecharModal)
        val constrainFiltroMarcaTodos = dialogFiltroLoja.findViewById<ConstraintLayout>(R.id.ConstrainFiltroMarcaTodos)
        val recyMarcasFiltro = dialogFiltroLoja.findViewById<RecyclerView>(R.id.recyMarcasFiltro)
        val marcasBotaoFiltro = dialogFiltroLoja.findViewById<TextView>(R.id.MarcasBotaoFiltro)
        val checkMarcaCelula = dialogFiltroLoja.findViewById<CheckBox>(R.id.checkMarcaCelula)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapterMarcas = AdapterFiltroLojasMarcas(listLojas, constrainFiltroMarcaTodos,checkMarcaCelula)
        recyMarcasFiltro.layoutManager = linearLayoutManager
        recyMarcasFiltro.adapter = adapterMarcas
        recyMarcasFiltro.setHasFixedSize(true)
        fecharModal.setOnClickListener {
            dialogFiltroLoja.dismiss()
        }

        marcasBotaoFiltro.setOnClickListener {
            val listaFiltro = adapterMarcas.listaLojasFiltro
            atualizaFiltroLista.atualizaListaLojas(listaFiltro)
            dialogFiltroLoja.dismiss()
        }
        checkMarcaCelula.isClickable = false
        checkMarcaCelula.isFocusable = false

        fun atualizarEstadoMarcaTodos(isChecked: Boolean) {
            checkMarcaCelula.isChecked = isChecked
            val colorTint = if (isChecked) {
                ContextCompat.getColor(context, R.color.blue300_15)
            } else {
                ContextCompat.getColor(context, R.color.gray100)
            }
            constrainFiltroMarcaTodos.backgroundTintList = ColorStateList.valueOf(colorTint)


            for (loja in listLojas) {
                if (loja.second is Lojas) {
                    (loja.second as Lojas).checada = isChecked
                }
            }
            adapterMarcas.listaLojasFiltro.clear()


            adapterMarcas.marcasLojas = listLojas
            adapterMarcas.notifyDataSetChanged()
        }
        constrainFiltroMarcaTodos.setOnClickListener {
            val novoEstado = !checkMarcaCelula.isChecked
            atualizarEstadoMarcaTodos(novoEstado)
        }

    }
}