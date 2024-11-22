package br.com.visaogrupo.tudofarmarep.Adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Syncs.retrofit.URLs
import com.bumptech.glide.Glide

class AdapterFiltroLojasMarcas (MarcasLojas: ArrayList<Pair<Int, Any>>, constrainFiltroMarcaTodos:ConstraintLayout, checkFiltroMarcaTodos:CheckBox) : RecyclerView.Adapter<AdapterFiltroLojasMarcas.ViewHolderFiltroLojaMarca>() {
    var marcasLojas = MarcasLojas
    val listaLojasFiltro =  ArrayList<Pair<Int, Any>>()
    val constrainFiltroMarcaTodos = constrainFiltroMarcaTodos
    val checkFiltroMarcaTodos = checkFiltroMarcaTodos
    class ViewHolderFiltroLojaMarca(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val constrainFiltroMarca = itemView.findViewById<ConstraintLayout>(R.id.ConstrainFiltroMarca)
         val nomeMarca = itemView.findViewById<TextView>(R.id.nomeMarca)
         val checkMarcaCelula = itemView.findViewById<CheckBox>(R.id.checkMarcaCelula)
         val img_marca = itemView.findViewById<ImageView>(R.id.img_marca)

    }

    override fun onBindViewHolder(holder: ViewHolderFiltroLojaMarca, position: Int) {
        val loja = marcasLojas[position].second as Lojas

        holder.checkMarcaCelula.isChecked = loja.checada
        val colorTint = if (loja.checada) {
            ContextCompat.getColor(holder.checkMarcaCelula.context, R.color.blue300_15)
        } else {
            ContextCompat.getColor(holder.checkMarcaCelula.context, R.color.gray100)
        }
        holder.constrainFiltroMarca.backgroundTintList = ColorStateList.valueOf(colorTint)

        holder.constrainFiltroMarca.setOnClickListener {
            checaCelula(holder, loja, constrainFiltroMarcaTodos, checkFiltroMarcaTodos)
        }

        holder.checkMarcaCelula.setOnClickListener {
            checaCelula(holder, loja, constrainFiltroMarcaTodos, checkFiltroMarcaTodos)
        }
        if (loja is Lojas){
            holder.nomeMarca.text = loja .Nome
            Glide.with(holder.itemView).load(URLs.urlImagensLoja+"/"+loja.imagem).into(holder.img_marca)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFiltroLojaMarca {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_marcas_filtro, parent, false)
        return ViewHolderFiltroLojaMarca(view)
    }

    override fun getItemCount(): Int {
       return marcasLojas.size
    }
    fun checaCelula(
        holder: ViewHolderFiltroLojaMarca,
        itemLoja: Any,
        constrainFiltroMarcaTodos: ConstraintLayout,
        checkFiltroMarcaTodos: CheckBox
    ) {
        if ((itemLoja as Lojas).checada) {
            holder.checkMarcaCelula.isChecked = false
            val colorTint = ContextCompat.getColor(holder.checkMarcaCelula.context, R.color.gray100)
            holder.constrainFiltroMarca.backgroundTintList = ColorStateList.valueOf(colorTint)
            itemLoja.checada = false
            itemLoja.ProdutosAbertos = false

            if (listaLojasFiltro.contains(0 to itemLoja)) {
                listaLojasFiltro.remove(0 to itemLoja)
            }
        } else {
            holder.checkMarcaCelula.isChecked = true
            itemLoja.ProdutosAbertos = false
            val colorTint = ContextCompat.getColor(holder.checkMarcaCelula.context, R.color.blue300_15)
            holder.constrainFiltroMarca.backgroundTintList = ColorStateList.valueOf(colorTint)

            if (!listaLojasFiltro.contains(0 to itemLoja)) {
                listaLojasFiltro.add(0 to itemLoja)
            }

            checkFiltroMarcaTodos.isChecked = false
            val colorTintItem = ContextCompat.getColor(holder.checkMarcaCelula.context, R.color.gray100)
            constrainFiltroMarcaTodos.backgroundTintList = ColorStateList.valueOf(colorTintItem)

            itemLoja.checada = true
        }
    }

}