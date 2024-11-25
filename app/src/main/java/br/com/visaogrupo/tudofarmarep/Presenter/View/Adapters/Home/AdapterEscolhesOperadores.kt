package br.com.visaogrupo.tudofarmarep.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosOperador
import br.com.visaogrupo.tudofarmarep.Objetos.CotacaoCarrinho
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.OperadorLogistico
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AdapterEscolhesOperadores(val listaOperadores: ArrayList<OperadorLogistico>,
                                val loja:Lojas,
                                val listaOperadoresSelecionados: ArrayList<OperadorLogistico>,
                                val atualizaInfosOperador: AtualizaInfosOperador,
                                val listaOplSelecionadosPricipal:ArrayList<OperadorLogistico>,var cotacaoCarrinho: CotacaoCarrinho?) :RecyclerView.Adapter<AdapterEscolhesOperadores.ViewHolderOperadores>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOperadores {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_item_opl_forma_pagamento, parent, false)
        return ViewHolderOperadores(view)
    }


    override fun onBindViewHolder(holder: ViewHolderOperadores, position: Int) {
        val operador = listaOperadores[position]
        holder.itemDescricao.text = operador.Nome

        if (listaOperadores.size == 1) {
            selecionaPrimeiroItem(holder, operador)
        }else if (listaOperadores.size > 1 && cotacaoCarrinho != null){
            if(cotacaoCarrinho!!.operadorLogistico1 == operador.OperadorLogistico_ID
                || cotacaoCarrinho!!.operadorLogistico2 == operador.OperadorLogistico_ID
                || cotacaoCarrinho!!.operadorLogistico3 == operador.OperadorLogistico_ID
                || cotacaoCarrinho!!.operadorLogistico4 == operador.OperadorLogistico_ID
                || cotacaoCarrinho!!.operadorLogistico5 == operador.OperadorLogistico_ID){
                selecionaPrimeiroItem(holder, operador, true)
            }

        }

        for ( (position, opl) in listaOperadoresSelecionados.withIndex()){
            if (opl == operador){
                holder.numeracaoOpl.text = "${position + 1}º "
            }
        }
        verificaItemSelecionadoInicial(holder, operador)


        holder.containerCheck.setOnClickListener {
            val totalItens = if(!operador.itemSelcionado) listaOperadoresSelecionados.size + 1 else listaOperadoresSelecionados.size
            cotacaoCarrinho = null

            if (totalItens > loja.QtdeMaxOperador) {
                MainScope().launch {
                    Toast.makeText(holder.itemView.context, "Máximo de ${loja.QtdeMaxOperador} selecionados", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }
            if (operador.itemSelcionado) {
                operador.itemSelcionado = false
                listaOperadoresSelecionados.remove(operador)
                listaOplSelecionadosPricipal.remove(operador)
                if(listaOplSelecionadosPricipal.size == 1 && listaOperadoresSelecionados.size > 1){
                    listaOperadoresSelecionados.removeIf {
                        it.principal
                    }
                    if(!listaOperadoresSelecionados.contains(listaOplSelecionadosPricipal.first())){
                        listaOperadoresSelecionados.add(0, listaOplSelecionadosPricipal.first())

                    }
                }
            } else {
                operador.itemSelcionado = true
                operador.principal = true
                listaOperadoresSelecionados.add(operador)
                listaOplSelecionadosPricipal.add(operador)

            }


            verificaItemSelecionadoInicial(holder, operador)

            MainScope().launch {

                atualizaInfosOperador.atualizaTExtoOperador(true)
                notifyDataSetChanged()

            }
        }
    }

    override fun getItemCount(): Int {
        return listaOperadores.size
    }

    class ViewHolderOperadores(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDescricao = itemView.findViewById<TextView>(R.id.itemDescricao)
        val checkItem = itemView.findViewById<ConstraintLayout>(R.id.checkItem)
        val marcadorItem = itemView.findViewById<ConstraintLayout>(R.id.marcadorItem)
        val fundoMarcado = itemView.findViewById<ConstraintLayout>(R.id.fundoMarcado)
        val  numeracaoOpl = itemView.findViewById<TextView>(R.id.numeracaoOpl)
        val containerCheck = itemView.findViewById<ConstraintLayout>(R.id.containerCheck)
    }

    fun verificaItemSelecionadoInicial(holder: ViewHolderOperadores, operador: OperadorLogistico){
        if(operador.itemSelcionado){
            holder.containerCheck.setBackgroundResource(R.drawable.borda_pontilhada_blue_500)
            holder.marcadorItem.visibility = View.VISIBLE
            holder.fundoMarcado.visibility = View.VISIBLE
            holder.itemDescricao.setTextColor(Color.parseColor("#0E3AAA"))

        }else{
            holder.marcadorItem.visibility = View.GONE
            holder.fundoMarcado.visibility = View.GONE
            holder.itemDescricao.setTextColor(Color.parseColor("#64748B"))
            holder.containerCheck.setBackgroundResource(R.drawable.bordas_pontilhadas_opl_pagamento)
            holder.numeracaoOpl.text = ""

        }
    }
    fun selecionaPrimeiroItem(holder: ViewHolderOperadores, operador: OperadorLogistico, isEnabler: Boolean = false){
        holder.containerCheck.setBackgroundResource(R.drawable.borda_pontilhada_blue_500)
        holder.marcadorItem.visibility = View.VISIBLE
        holder.fundoMarcado.visibility = View.VISIBLE
        holder.itemDescricao.setTextColor(Color.parseColor("#0E3AAA"))
        operador.itemSelcionado = true
        holder.containerCheck.isEnabled = isEnabler
        if(!listaOplSelecionadosPricipal.contains(operador) ){
            listaOplSelecionadosPricipal.add(operador)

        }

        if (!listaOperadoresSelecionados.contains(operador)){
            listaOperadoresSelecionados.add(operador)
        }


        atualizaInfosOperador.atualizaTExtoOperador(true)
    }
}