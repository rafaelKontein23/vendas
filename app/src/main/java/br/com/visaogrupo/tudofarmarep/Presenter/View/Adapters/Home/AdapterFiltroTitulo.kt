package br.com.visaogrupo.tudofarmarep.Adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaFiltroAtribudos
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaTotalProdutos
import br.com.visaogrupo.tudofarmarep.Objetos.FiltroCategoria
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.R


class AdapterFiltroTitulo (val listaCategorias: List<FiltroCategoria>,
                           val operadorSelcionado :Int, val atualizaTotalProdutos: AtualizaTotalProdutos, val loja:Lojas): RecyclerView.Adapter<AdapterFiltroTitulo.ViewHolderFiltroTitulo>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFiltroTitulo {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_titulo_filtro, parent, false)
        return ViewHolderFiltroTitulo(view)
    }

    class ViewHolderFiltroTitulo(itemView: View) : RecyclerView.ViewHolder(itemView) {
           val nomeTituloFiltro = itemView.findViewById<TextView>(R.id.nomeTituloFiltro)
           val recySubFiltro = itemView.findViewById<RecyclerView>(R.id.recySubFiltro)

    }
    override fun onBindViewHolder(holder: ViewHolderFiltroTitulo, position: Int) {
        val itemCategoria = listaCategorias[position]
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(4, LinearLayoutManager.HORIZONTAL)
        val adapterFiltros = AdapterFiltros(listaCategorias[position].filtros, operadorSelcionado,
            atualizaTotalProdutos, loja)
        holder.nomeTituloFiltro.text = itemCategoria.titulo
        holder.recySubFiltro.layoutManager = staggeredGridLayoutManager
        holder.recySubFiltro.adapter = adapterFiltros

    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }

}