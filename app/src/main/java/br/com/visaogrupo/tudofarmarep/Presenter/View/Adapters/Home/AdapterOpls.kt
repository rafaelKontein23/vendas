package br.com.visaogrupo.tudofarmarep.Adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProdutosOperadores
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Operadores
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Syncs.retrofit.URLs
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogOpls
import com.bumptech.glide.Glide
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdapterOpls(listaOpls: ArrayList<Operadores>, dialogOpls: Dialog,
                  atualizaProdutosOperadores: AtualizaProdutosOperadores, lojas: Lojas)  :RecyclerView.Adapter<AdapterOpls.MyViewHolderOpls>(){
    private var listaOpls = listaOpls
    private var dialogOpls = dialogOpls
    private var  atualizaProdutosOperadores= atualizaProdutosOperadores
    private var lojas = lojas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderOpls {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_opls,parent,false)
        return MyViewHolderOpls(view)
    }
    override fun onBindViewHolder(holder: MyViewHolderOpls, position: Int) {
        val operadores = listaOpls[position]
        Glide.with(holder.itemView.context)
            .load("${URLs.urlImagensOpls}${operadores.imagem}")
            .into( holder.imgOperadores)

        holder.celulaOpls.setOnClickListener {

            atualizaProdutosOperadores.atualizaProdutosOperadores(operadores.operadoresID, lojaId = lojas.Loja_ID, operadores.nome,lojas.marcaID, lojas.ValorMaximo)
           MainScope().launch {
               delay(500)
               dialogOpls.dismiss()

           }


        }
    }
    override fun getItemCount(): Int {
        return  listaOpls.size
    }


    class MyViewHolderOpls(itemView: View) : ViewHolder(itemView) {
          val imgOperadores = itemView.findViewById<ImageView>(R.id.imgOperadores)
         // val valorMinimo = itemView.findViewById<ImageView>(R.id.valorMinimo)
         val celulaOpls = itemView.findViewById<ConstraintLayout>(R.id.celulaOpls)
    }


}