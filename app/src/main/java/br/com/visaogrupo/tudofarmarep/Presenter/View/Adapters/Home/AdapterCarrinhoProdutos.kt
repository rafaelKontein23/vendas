package br.com.visaogrupo.tudofarmarep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosItens
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Objetos.Progressiva
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogProdutoDetalhe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AdapterCarrinhoProdutos(val listaProdutos: ArrayList<Produtos>, val atualizaInfosItens: AtualizaInfosItens, val loja:Lojas,val razaoSocial:String) : RecyclerView.Adapter<AdapterCarrinhoProdutos.ViewHolderCarrinhoProduto>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarrinhoProduto {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_produto_aaz_carrinho, parent, false)
        return ViewHolderCarrinhoProduto(view)
    }

    override fun getItemCount(): Int {
        return listaProdutos.size
    }

    override fun onBindViewHolder(holder: ViewHolderCarrinhoProduto, position: Int) {
        val produto = listaProdutos[position]
        val formataTexto = FormatarTexto()
        val formatarValortotal = formataTexto.formatarParaMoeda(produto.valorProdutoTotal)
        holder.nomeProdutoAaZ.text = produto.Nome
        holder.quatidadeProduto.text = "${produto.quantidadeAdicionada} uni."
        holder.totalSomado.text = "${formatarValortotal}"
        holder.barra.text = produto.Barra
        holder.valorPF.text = "${formataTexto.formatarParaMoeda(produto.progressiva!!.valorUnitario)}"
        holder.textDeconto.text = "${produto.progressiva!!.desconto}% desc."
        holder.valorComDesconto.text = "${formataTexto.formatarParaMoeda(produto.progressiva!!.valorUnitarioDesconto)}"
        if(produto.progressiva!!.stUnitario > 0.0){
            holder.stTextUnitario.text = "+ST: ${formataTexto.formatarParaMoeda(produto.progressiva!!.stUnitario)}"
            val valorTotalST = produto.progressiva!!.stUnitario * produto.quantidadeAdicionada
            holder.stTextTotal.text = "+ ST: ${formataTexto.formatarParaMoeda(valorTotalST)}"
        }else{
            holder.stTextUnitario.visibility = View.GONE
            holder.stTextTotal.visibility = View.GONE
        }

        holder.celulaProdutoCarrinho.setOnClickListener {
          val dialogProdutoDetalhe = DialogProdutoDetalhe()
          dialogProdutoDetalhe.dialogPrudutoDetalhe(holder.itemView.context, produto, atualizaInfosItens, loja, carrinho = true, razaoSocial)
        }
        if (position == listaProdutos.size - 1){
             holder.view2.visibility = View.GONE
        }

    }

    class ViewHolderCarrinhoProduto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val barra = itemView.findViewById<TextView>(R.id.barra)
        val nomeProdutoAaZ = itemView.findViewById<TextView>(R.id.nomeProdutoAaZ)
        val valorPF = itemView.findViewById<TextView>(R.id.valorPF)
        val textDeconto = itemView.findViewById<TextView>(R.id.textDeconto)
        val valorComDesconto = itemView.findViewById<TextView>(R.id.valorComDesconto)
        val quatidadeProduto = itemView.findViewById<TextView>(R.id.quatidadeProduto)
        val totalSomado = itemView.findViewById<TextView>(R.id.totalSomado)
        val celulaProdutoCarrinho = itemView.findViewById<ConstraintLayout>(R.id.celulaProdutoCarrinho)
        val view2 = itemView.findViewById<View>(R.id.view2)
        val stTextUnitario = itemView.findViewById<TextView>(R.id.stTextUnitario)
        val stTextTotal = itemView.findViewById<TextView>(R.id.stTextTotal)
    }


}