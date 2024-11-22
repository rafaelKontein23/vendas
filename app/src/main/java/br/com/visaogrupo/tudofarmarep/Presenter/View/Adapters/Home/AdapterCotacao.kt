package br.com.visaogrupo.tudofarmarep.Adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.ActLojaPadrao
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskCotacao
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProgress
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.Objetos.Cotacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Syncs.retrofit.URLs
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogCarrinhoAberto
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class AdapterCotacao (val listaCotacao: ArrayList<Cotacao>, val representanteId: Int, val atualizaProgres: AtualizaProgress,
    val  atividade:Activity, val inciaLoja:Int)  : RecyclerView.Adapter<AdapterCotacao.ViewHolderCotcao>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCotcao {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_cotacao, parent, false)
        return ViewHolderCotcao(view)
    }
    override fun onBindViewHolder(holder: ViewHolderCotcao, position: Int) {
       val cotacao = listaCotacao[position]
        holder.razaoSocialCotacao.text = cotacao.RazaoSocial
        holder.cnpjCotacao.text = FormatarTexto().formatCNPJ(cotacao.CNPJ)
        holder.nomeDonoFarmacia.text = cotacao.Nome
        holder.nomeLojaCotcao.text = cotacao.Nome
        val urlimg = URLs.urlImagensLoja + "/" + cotacao.ImagemMarca
        if (listaCotacao.size -1 == position){
            holder.viewLinha.isVisible = false
        }

        holder.dtaPedido.text =  FormatarTexto().formataDataMesAno(cotacao.DataPedido)
        holder.valorTotalPedido.text = FormatarTexto().formatarParaMoeda(cotacao.TotalPedido)
        holder.constarinCotacao.setOnClickListener {
           MainScope().launch {
               atualizaProgres.escondeProgress(true)
               withContext(Dispatchers.IO) {
                   var listaCarrinho  = ArrayList<CarrinhoItemCotacao>()
                   val tarefa = async {
                       val taskCotacaoCarrinho = TaskCotacao()
                       listaCarrinho = taskCotacaoCarrinho.buscaCarrinhoCotacao(representanteId, cotacao.CarrinhoId)
                   }
                   tarefa.await()

                   if (listaCarrinho.isEmpty()){
                       MainScope().launch {
                           atualizaProgres.escondeProgress(false)
                           Alertas.alertaErro(holder.itemView.context, "Não conseguimos carregar os dados, tente novamente.", "Ops!"){

                           }
                       }
                   }else{

                       CoroutineScope(Dispatchers.IO).launch {

                           val daoCarrinho = DAOCarrinho()
                           val daoHelper =  DAOHelper(holder.itemView.context).writableDatabase
                           val isCarrinho =  daoCarrinho.conferiCarrinhoLoja(cotacao.lojaID, cotacao.CNPJ,daoHelper)
                           MainScope().launch {
                               atualizaProgres.escondeProgress(false)
                           }
                           if (isCarrinho){
                               MainScope().launch {
                                   val dialogCarrinhoAberto = DialogCarrinhoAberto()
                                   dialogCarrinhoAberto.dialogDialogAberto(holder.constarinCotacao.context, listaCarrinho,cotacao, atividade, inciaLoja)
                               }
                           }else{
                               val intent = Intent(holder.imgMarca.context, ActLojaPadrao::class.java)

                               val bundle = Bundle()
                               val bundleListaCarrinho = Bundle()
                               val bundleCotacaoOperador = Bundle()
                               val bundleNomeOperador = Bundle()
                               val bundleCotacao = Bundle()
                               bundleNomeOperador.putString("NomeOperadorBundle", cotacao.nomeOperadpr)
                               intent.putExtra("NomeOperadorBundle",bundleNomeOperador)
                               val cnpjItem = Cnpj(cotacao.CNPJ, cotacao.cidade, "", "", cotacao.endereco, cotacao.numero, cotacao.RazaoSocial, cotacao.UF)
                               bundle.putSerializable("cnpjSelecionado", cnpjItem as Serializable)
                               bundleListaCarrinho.putSerializable("listaCarrinho", listaCarrinho as Serializable)
                               bundleCotacaoOperador.putInt("operadorCotacao", cotacao.operedorGrupoId)
                               bundleCotacao.putSerializable("cotacao", cotacao as Serializable)
                               intent.putExtra("operadorCotacaoBundle", bundleCotacaoOperador)
                               intent.putExtra("cnpjSelecionadoBundle", bundle)
                               intent.putExtra("listaCarrinhoBundle", bundleListaCarrinho)
                               intent.putExtra("cotacaoBundle", bundleCotacao)

                               atividade.startActivityForResult(intent, inciaLoja)

                           }
                       }
                   }

               }
           }

        }
        Glide.with(holder.itemView.context)
            .load(urlimg)
            .placeholder(R.drawable.padrao)
            .error(R.drawable.padrao)
            .into(holder.imgMarca)
    }
    override fun getItemCount(): Int {
        return  listaCotacao.size
    }

    class ViewHolderCotcao(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgMarca = itemView.findViewById<ImageView>(R.id.img_marca)
        val razaoSocialCotacao = itemView.findViewById<TextView>(R.id.razaoSocialCotacao)
        val cnpjCotacao = itemView.findViewById<TextView>(R.id.cnpjCotacao)
        val nomeDonoFarmacia = itemView.findViewById<TextView>(R.id.nomeDonoFarmacia)
        val nomeLojaCotcao = itemView.findViewById<TextView>(R.id.nomeLojaCotacao)
        val valorTotalPedido = itemView.findViewById<TextView>(R.id.valorTotalPedido)
        val dtaPedido = itemView.findViewById<TextView>(R.id.dtaPedido)
        val  viewLinha = itemView.findViewById<View>(R.id.viewLinha)
        val constarinCotacao = itemView.findViewById<View>(R.id.constarinCotacao)

    }
}