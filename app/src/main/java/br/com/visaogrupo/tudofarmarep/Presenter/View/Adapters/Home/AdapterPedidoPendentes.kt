package br.com.visaogrupo.tudofarmarep.Adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.ActLojaPadrao
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskConsultaCNPJ
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoAbertos
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.Objetos.PedidosPendentes
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.Serializable

class AdapterPedidoPendentes(listaPedidoPendentes:ArrayList<CarrinhoAbertos>, val atividade: Activity, val inciaLoja:Int) : RecyclerView.Adapter<AdapterPedidoPendentes.pedidosPendentesViewHolder>() {
    var listaPedidoPendentes = listaPedidoPendentes
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pedidosPendentesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_pedido_pendente, parent, false)
        return pedidosPendentesViewHolder(view)
    }


    override fun onBindViewHolder(holder: pedidosPendentesViewHolder, position: Int) {
         val pedidosPendentesItem = listaPedidoPendentes[position]
         holder.nomeText.text = pedidosPendentesItem.nomeMarca
         holder.cnpjText.text = FormatarTexto().formatCNPJ(pedidosPendentesItem.cnpj)
         holder.valorTotal.text = FormatarTexto().formatarParaMoeda(pedidosPendentesItem.totalCarrinho)
         holder.nomeLoja.text = pedidosPendentesItem.nomeLoja

        Glide.with(atividade).load(URLs.urlImagensLoja+"/"+pedidosPendentesItem.logoTipo).into(holder.imgMarca)

        if(pedidosPendentesItem.razoSocial.length > 20){
             holder.razaoSocial.text = pedidosPendentesItem.razoSocial.substring(0, 20) + "..."

         }else{
             holder.razaoSocial.text = pedidosPendentesItem.razoSocial

         }

         holder.constrainsCarrinhoAberta.setOnClickListener {
             CoroutineScope(Dispatchers.IO).launch {
                 val taskConsultaCNPJ = TaskConsultaCNPJ()
                 val cnpjItem =taskConsultaCNPJ.consultaCNPJ(pedidosPendentesItem.cnpj)
                 val prefs = PreferenceManager.getDefaultSharedPreferences(holder.cnpjText.context)
                 val fazendoCarga = prefs.getBoolean("fazendoCarga", false)
                 if(fazendoCarga){
                     MainScope().launch {
                         Alertas.alertaErro(holder.valorTotal.context, "Os dados estão sendo atualizados, tente novamente mais tarde.", "Ops!"){
                         }
                     }
                 }else{
                     if(cnpjItem != null  ){
                         MainScope().launch {
                             val intent = Intent(holder.valorTotal.context, ActLojaPadrao::class.java)

                             val bundle = Bundle()
                             val bundleListaCarrinho = Bundle()
                             val bundleCotacaoOperador = Bundle()
                             val bundleNomeOperador = Bundle()
                             bundleNomeOperador.putString("NomeOperadorBundle","")
                             intent.putExtra("NomeOperadorBundle","")
                             bundleCotacaoOperador.putInt("operadorCotacaoBundle", 0)
                             bundle.putSerializable("cnpjSelecionado", cnpjItem as Serializable)
                             intent.putExtra("cnpjSelecionadoBundle", bundle)
                             intent.putExtra("operadorCotacaoBundle", bundleCotacaoOperador)
                             val listaCarrinho = ArrayList<CarrinhoItemCotacao>()
                             bundleListaCarrinho.putSerializable("listaCarrinho", listaCarrinho)
                             intent.putExtra("listaCarrinhoBundle", bundleListaCarrinho)
                             atividade.startActivityForResult(intent, inciaLoja )
                         }
                     }else{
                         MainScope().launch {
                             Alertas.alertaErro(holder.valorTotal.context, "Erro ao consultar CNPJ", "Ops!"){

                             }
                         }
                     }

                 }
             }

         }
    }
    override fun getItemCount(): Int {
      return listaPedidoPendentes.size
    }

    class pedidosPendentesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nomeText = itemView.findViewById<TextView>(R.id.nomeText)
        val nomeLoja = itemView.findViewById<TextView>(R.id.nomeLoja)
        val cnpjText = itemView.findViewById<TextView>(R.id.cnpjText)
        val valorTotal = itemView.findViewById<TextView>(R.id.valorTotal)
        val razaoSocial = itemView.findViewById<TextView>(R.id.razaoSocial)
        val imgMarca = itemView.findViewById<ImageView>(R.id.imgItem)
        val constrainsCarrinhoAberta = itemView.findViewById<ConstraintLayout>(R.id.constrainsCarrinhoAberta)
    }

}