package br.com.visaogrupo.tudofarmarep.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaFiltroAtribudos
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaTotalProdutos
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.DAO.DAOProdutos
import br.com.visaogrupo.tudofarmarep.Objetos.Filtros
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdapterFiltros (val listaFiltros : List<Filtros>,
                      val operadorSelcionado :Int,val atualizaTotalProdutos: AtualizaTotalProdutos, val loja:Lojas) : RecyclerView.Adapter<AdapterFiltros.ViewHolderSubFiltro>() {
    override fun onBindViewHolder(holder: ViewHolderSubFiltro, position: Int) {
        val itemSubFiltro = listaFiltros[position]
        holder.textSubFiltro.text = itemSubFiltro.nome

        Verifica(itemSubFiltro, holder)

        holder.textSubFiltro.setOnClickListener {
            checkItemInicialClick(itemSubFiltro, holder)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSubFiltro {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_sub_filtro, parent, false)
        return ViewHolderSubFiltro(view)
    }

    override fun getItemCount(): Int {
        return listaFiltros.size
    }

    class ViewHolderSubFiltro(itemView: View) : ViewHolder(itemView) {
            val textSubFiltro = itemView.findViewById<TextView>(R.id.textSubFiltro)
    }
    fun checkItemInicial(itemSubFiltro:Filtros, holder:ViewHolderSubFiltro){
        if (itemSubFiltro.checado ){
            if (!loja.listaAtrinutos.contains(itemSubFiltro.Atributo_id)){
                loja.listaAtrinutos.add(itemSubFiltro.Atributo_id)
            }
            contaFiltros(holder)

        }else{
            if (loja.listaAtrinutos.contains(itemSubFiltro.Atributo_id)){
                loja.listaAtrinutos.remove(itemSubFiltro.Atributo_id)
            }
            contaFiltros(holder)

        }
        if(loja.listaAtrinutos.contains(itemSubFiltro.Atributo_id)){
            holder.textSubFiltro.backgroundTintList = null
            holder.textSubFiltro.backgroundTintList=  ColorStateList.valueOf( ContextCompat.getColor(holder.textSubFiltro.context,R.color.blue300))
            holder.textSubFiltro.setTextColor(Color.parseColor("#F1F5F9"))
        }else{
            holder.textSubFiltro.backgroundTintList = null
            val drawable: Drawable? = ContextCompat.getDrawable(holder.itemView.context, R.drawable.borda_celular_sub_filtro)
            holder.textSubFiltro.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            holder.textSubFiltro.setTextColor(Color.parseColor("#94A3B8"))
        }
    }
    fun checkItemInicialClick(itemSubFiltro: Filtros, holder: ViewHolderSubFiltro) {
        itemSubFiltro.checado = !itemSubFiltro.checado
        checkItemInicial(itemSubFiltro, holder)
    }
    fun Verifica(itemSubFiltro:Filtros, holder:ViewHolderSubFiltro){

        if(loja.listaAtrinutos.contains(itemSubFiltro.Atributo_id)){
            holder.textSubFiltro.backgroundTintList = null
            itemSubFiltro.checado = true
            holder.textSubFiltro.backgroundTintList=  ColorStateList.valueOf( ContextCompat.getColor(holder.textSubFiltro.context,R.color.blue300))
            holder.textSubFiltro.setTextColor(Color.parseColor("#F1F5F9"))
        }else{
            holder.textSubFiltro.backgroundTintList = null
            itemSubFiltro.checado = false
            val drawable: Drawable? = ContextCompat.getDrawable(holder.itemView.context, R.drawable.borda_celular_sub_filtro)
            holder.textSubFiltro.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            holder.textSubFiltro.setTextColor(Color.parseColor("#94A3B8"))
        }
        contaFiltros(holder)
    }
   fun contaFiltros( holder:ViewHolderSubFiltro){
       CoroutineScope(Dispatchers.IO).launch {
           val daoHelper = DAOHelper(holder.itemView.context).writableDatabase
           val daoProdutos = DAOProdutos()
           val listaBarras = daoProdutos.filtraProdudos(daoHelper,loja.listaAtrinutos, operadorSelcionado, lojaID =  loja.Loja_ID)
           atualizaTotalProdutos.atualizaTotalProdutos(listaBarras.size, loja)
       }
   }
}