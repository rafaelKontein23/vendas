package br.com.visaogrupo.tudofarmarep.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosOperador
import br.com.visaogrupo.tudofarmarep.Objetos.CotacaoCarrinho
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.OperadorLogistico
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AdapterLooping (val listaOperadores: ArrayList<OperadorLogistico>, val loja: Lojas,  val listaOperadoresSelecionados: ArrayList<OperadorLogistico>, val atualizaInfosOperador: AtualizaInfosOperador): RecyclerView.Adapter<AdapterLooping.LoopViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_operador_looping, parent, false)
        return LoopViewHolder(view)
    }
    var selecionouOplPrincipal = false


    override fun onBindViewHolder(holder: LoopViewHolder, position: Int) {
        val operador = listaOperadores[position]
        holder.itemDescricao.text = operador.Nome

        holder.containerCheck.isEnabled = selecionouOplPrincipal
        verificaItemSelecionadoInicialEnable(holder, selecionouOplPrincipal)


        if(selecionouOplPrincipal){
            holder.numeracaoOpl.text = ""
            if (listaOperadoresSelecionados.contains(operador)) {
                val index = listaOperadoresSelecionados.indexOf(operador)

                holder.numeracaoOpl.text = "${index + 1}º"
                operador.itemSelcionado = true


            } else {
                operador.itemSelcionado = false
            }
            verificaItemSelecionadoInicial(holder, operador)

        }


        holder.containerCheck.setOnClickListener {
            operador.itemSelcionado = !operador.itemSelcionado

            if (!operador.itemSelcionado) {
                listaOperadoresSelecionados.remove(operador)
            } else {
                val totalSelecionados = listaOperadoresSelecionados.size
                if (totalSelecionados == loja.QtdeMaxOperador) {
                    MainScope().launch {
                        Toast.makeText(holder.itemView.context, "Máximo de ${loja.QtdeMaxOperador} selecionados", Toast.LENGTH_SHORT).show()
                        operador.itemSelcionado = false
                        return@launch
                    }
                } else {
                    operador.principal = false
                    listaOperadoresSelecionados.add(operador)
                }
            }
            for (i in listaOperadores.indices) {
                listaOperadores[i].itemSelcionado = listaOperadoresSelecionados.contains(listaOperadores[i])
            }

            verificaItemSelecionadoInicial(holder, operador)

            MainScope().launch {
                atualizaInfosOperador.atualizaTExtoOperador(false, true)
                notifyDataSetChanged()
            }
        }
    }




    class LoopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemDescricao = itemView.findViewById<TextView>(R.id.itemDescricao)
        val checkItem = itemView.findViewById<ConstraintLayout>(R.id.checkItem)
        val marcadorItem = itemView.findViewById<ConstraintLayout>(R.id.marcadorItem)
        val fundoMarcado = itemView.findViewById<ConstraintLayout>(R.id.fundoMarcado)
        val numeracaoOpl = itemView.findViewById<TextView>(R.id.numeracaoOpl)
        val containerCheck = itemView.findViewById<ConstraintLayout>(R.id.containerCheck)
    }
    override fun getItemCount(): Int {
        return  listaOperadores.size
    }
    fun verificaItemSelecionadoInicialEnable(holder: LoopViewHolder,  selecionouOplPrincipal: Boolean){
        if(selecionouOplPrincipal){

            holder.marcadorItem.visibility = View.GONE
            holder.fundoMarcado.visibility = View.GONE
            holder.itemDescricao.setTextColor(Color.parseColor("#64748B"))
            holder.containerCheck.setBackgroundResource(R.drawable.bordas_pontilhadas_opl_pagamento)
        }else{
            holder.marcadorItem.visibility = View.GONE
            holder.fundoMarcado.visibility = View.GONE
            val color = ContextCompat.getColor(holder.itemDescricao.context, R.color.gray300)
            holder.itemDescricao.setTextColor(color)
            holder.containerCheck.setBackgroundResource(R.drawable.bordas_pontilhadas_opl_pagamento_clara)
            holder.numeracaoOpl.text = ""

        }
    }

    fun verificaItemSelecionadoInicial(holder: LoopViewHolder, operador: OperadorLogistico){
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
}