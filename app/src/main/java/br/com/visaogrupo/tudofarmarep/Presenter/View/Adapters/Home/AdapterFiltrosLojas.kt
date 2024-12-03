package br.com.visaogrupo.tudofarmarep.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.InterfaceScrolaLista
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import com.bumptech.glide.Glide

class AdapterFiltrosLojas(listaLojasFiltros : ArrayList<Lojas>, interfaceScrolaLista: InterfaceScrolaLista) : RecyclerView.Adapter<AdapterFiltrosLojas.viewHolderFiltroLoja>() {
    var listaLojasFiltros = listaLojasFiltros
    val interfaceScrolaLista = interfaceScrolaLista

    override fun onBindViewHolder(holder: viewHolderFiltroLoja, position: Int) {
       val itemFiltro = listaLojasFiltros[position]
        holder.textoDescricaoFiltro.text = itemFiltro.Nome

        Glide.with(holder.itemView.context)
            .load(URLs.urlImagensLoja+"/"+itemFiltro.imagem)
            .placeholder(R.drawable.padrao)
            .error(R.drawable.padrao)
            .into(holder.imgLab)
        if (itemFiltro.urlLogin.isNotEmpty()){
            holder.constraintLoja.isVisible = false
        }
        if (itemFiltro.lojaFiltroSelecionado) {
            holder.constraintLoja.setBackgroundResource(R.drawable.borda_stroke_1_solid_white)
            holder.textoDescricaoFiltro.setTextColor(Color.parseColor("#828282"))

        }else{
            holder.constraintLoja.setBackgroundResource(R.drawable.borda_stroke_1_bordercolor_white)
            holder.textoDescricaoFiltro.setTextColor(Color.parseColor("#FFFFFF"))

        }
        holder.constraintLoja.setOnClickListener {
            // Desmarca todos os itens primeiro

            for (loja in listaLojasFiltros) {
                loja.lojaFiltroSelecionado = false
            }
            itemFiltro.lojaFiltroSelecionado = true

            interfaceScrolaLista.scrollToLoja(itemFiltro)
            notifyDataSetChanged()


        }
    }
    override fun getItemCount(): Int {
        return listaLojasFiltros.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderFiltroLoja {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.celula_filtro_loja, parent, false)
        return viewHolderFiltroLoja(view)
     }
    class viewHolderFiltroLoja(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoDescricaoFiltro = itemView.findViewById<TextView>(R.id.textoDescricaoFiltro)
        val imgLab = itemView.findViewById<ImageView>(R.id.imgLab)
        val constraintLoja = itemView.findViewById<ConstraintLayout>(R.id.constraintLoja)
    }


}