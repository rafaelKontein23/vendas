package br.com.visaogrupo.tudofarmarep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Objetos.Atributo
import br.com.visaogrupo.tudofarmarep.R
import com.bumptech.glide.Glide

 class AdapterIconeIfos(val listaAtributos: List<Atributo>) : RecyclerView.Adapter<AdapterIconeIfos.ViewHolderIcones>() {

    class ViewHolderIcones(itemView: View): RecyclerView.ViewHolder(itemView){
        val iconesAtributos = itemView.findViewById<ImageView>(R.id.iconesAtributos)
        val tituloIcones = itemView.findViewById<TextView>(R.id.tituloIcones)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderIcones {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_atributos_cnpj, parent, false)
        return ViewHolderIcones(view)
    }

    override fun getItemCount(): Int {
       return listaAtributos.size
    }

    override fun onBindViewHolder(holder: ViewHolderIcones, position: Int) {
         val atributo = listaAtributos[position]
        Glide.with(holder.itemView.context)
            .load(atributo.imagem)
            .placeholder(R.drawable.bordas_100_solid_blue400)
            .error(R.drawable.bordas_100_solid_blue400)
            .into(holder.iconesAtributos)
         holder.tituloIcones.text = atributo.Atributo


    }

}