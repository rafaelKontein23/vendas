package br.com.visaogrupo.tudofarmarep.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Objetos.CotacaoCarrinho
import br.com.visaogrupo.tudofarmarep.Objetos.FormaPagamento
import br.com.visaogrupo.tudofarmarep.Objetos.OperadorLogistico
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AdapterFormaDePagamento (val listaFormaDePagamento: List<FormaPagamento>,
                               val listaFormaDePagamentoSelecionada: ArrayList<FormaPagamento>,
                                var  cotacaoCarrinho: CotacaoCarrinho?

    ): RecyclerView.Adapter<AdapterFormaDePagamento.ViewHolderFormaDePagamento>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFormaDePagamento {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_item_opl_forma_pagamento, parent, false)
        return ViewHolderFormaDePagamento(view)
    }
    override fun onBindViewHolder(holder: ViewHolderFormaDePagamento, position: Int) {
        val item = listaFormaDePagamento[position]
        holder.itemDescricao.text = item.Descricao
        verificaItemSelecionadoInicial(holder, item)
        if (listaFormaDePagamento.size == 1){
            item.itemSelcionado = true
            verificaItemSelecionado(holder, item)
            listaFormaDePagamentoSelecionada.clear()
            listaFormaDePagamentoSelecionada.add(item)
        }else{
            if(cotacaoCarrinho != null){
                if(item.formaPagamentoMarcas_ID == cotacaoCarrinho!!.formaDePagamentoMarcaID) {
                    item.itemSelcionado = true
                    verificaItemSelecionado(holder, item)
                    listaFormaDePagamentoSelecionada.clear()
                    listaFormaDePagamentoSelecionada.add(item)
                    cotacaoCarrinho = null
                }
            }
        }
        holder.containerCheck.setOnClickListener {
            item.itemSelcionado = !item.itemSelcionado
            verificaItemSelecionado(holder, item)
            if(item.itemSelcionado){
                listaFormaDePagamentoSelecionada.clear()
                listaFormaDePagamentoSelecionada.add(item)
            }else{
                listaFormaDePagamentoSelecionada.clear()
            }
        }
    }

    override fun getItemCount(): Int {
        return listaFormaDePagamento.size
    }

    class ViewHolderFormaDePagamento(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemDescricao = itemView.findViewById<TextView>(R.id.itemDescricao)
        val checkItem = itemView.findViewById<ConstraintLayout>(R.id.checkItem)
        val marcadorItem = itemView.findViewById<ConstraintLayout>(R.id.marcadorItem)
        val fundoMarcado = itemView.findViewById<ConstraintLayout>(R.id.fundoMarcado)
        val containerCheck = itemView.findViewById<ConstraintLayout>(R.id.containerCheck)

    }
    fun verificaItemSelecionado(holder: ViewHolderFormaDePagamento, formaPagamento: FormaPagamento){

        for(formaPagamentoItem in listaFormaDePagamento){
            if(formaPagamentoItem != formaPagamento){
                formaPagamentoItem.itemSelcionado = false
            }
        }
        MainScope().launch {
            notifyDataSetChanged()

        }
    }
    fun verificaItemSelecionadoInicial(holder: ViewHolderFormaDePagamento, formaPagamento: FormaPagamento){
        if(formaPagamento.itemSelcionado){
            holder.containerCheck.setBackgroundResource(R.drawable.borda_pontilhada_blue_500)
            holder.marcadorItem.visibility = View.VISIBLE
            holder.fundoMarcado.visibility = View.VISIBLE
            holder.itemDescricao.setTextColor(Color.parseColor("#0E3AAA"))

        }else{
            holder.marcadorItem.visibility = View.GONE
            holder.fundoMarcado.visibility = View.GONE
            holder.itemDescricao.setTextColor(Color.parseColor("#64748B"))
            holder.containerCheck.setBackgroundResource(R.drawable.bordas_pontilhadas_opl_pagamento)
        }

    }

}