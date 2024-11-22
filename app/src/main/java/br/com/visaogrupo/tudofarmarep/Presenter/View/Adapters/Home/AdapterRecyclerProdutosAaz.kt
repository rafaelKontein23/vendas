package br.com.visaogrupo.tudofarmarep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.R

class AdapterRecyclerProdutosAaz(
    private var listaProdutos: List<Produtos>
) : RecyclerView.Adapter<AdapterRecyclerProdutosAaz.ProdutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_produtos_aaz, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProdutos[position]
      ///  holder.nomeProduto.text = produto.nome
        // Atualize outros campos conforme necessário
    }

    override fun getItemCount(): Int = listaProdutos.size

    fun updateData(newProdutos: List<Produtos>) {
        val diffCallback = ProdutoDiffCallback(this.listaProdutos, newProdutos)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listaProdutos = newProdutos
        diffResult.dispatchUpdatesTo(this)
    }

    class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val nomeProduto: TextView = itemView.findViewById(R.id.nomeProduto)
        // Adicione outros campos conforme necessário
    }

    class ProdutoDiffCallback(
        private val oldList: List<Produtos>,
        private val newList: List<Produtos>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].Barra == newList[newItemPosition].Barra

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
