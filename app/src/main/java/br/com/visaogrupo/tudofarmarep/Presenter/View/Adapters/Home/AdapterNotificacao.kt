package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaNotificacao
import br.com.visaogrupo.tudofarmarep.Utils.DataUltis
import br.com.visaogrupo.tudofarmarep.databinding.ItemNotificacaoBinding

class AdapterNotificacao(val listaNotificacao:ArrayList<RespostaNotificacao>) : RecyclerView.Adapter<AdapterNotificacao.notificacaoViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notificacaoViewHolder {
        var view = ItemNotificacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return notificacaoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaNotificacao.size
    }

    override fun onBindViewHolder(holder: notificacaoViewHolder, position: Int) {
        val notificacao = listaNotificacao[position]
        holder.bind(notificacao)

    }
    class notificacaoViewHolder(val binding: ItemNotificacaoBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(notificacao:RespostaNotificacao
       ) {
           binding.tituloNotificacao.text = notificacao.Titulo
           binding.descriacaoNotificacao.text = notificacao.Mensagem
           val data  = DataUltis.formatarDataISO(notificacao.DtPush)
           binding.horarioNotificacao.text = data
           if (notificacao.Lido){

               binding.imgNotificacao.setImageDrawable(null)
               binding.tituloNotificacao.setTextColor(binding.tituloNotificacao.context.getColor(R.color.gray600))
               binding.descriacaoNotificacao.setTextColor(binding.tituloNotificacao.context.getColor(R.color.gray500))
               binding.horarioNotificacao.setTextColor(binding.tituloNotificacao.context.getColor(R.color.gray500))


           }
           if (notificacao.Categoria.equals("Comercial")){
               binding.imgNotificacao.setImageResource(R.drawable.compras_icone)

           }else if(notificacao.Categoria.equals("Adm. de Prepostos")){
               binding.imgNotificacao.setImageResource(R.drawable.pessoas_icone)

           }else if(notificacao.Categoria.equals("Gestão de Vendas")){
               binding.imgNotificacao.setImageResource(R.drawable.caixa_icone)

           }else if (notificacao.Categoria.equals("Atualização")){
               binding.imgNotificacao.setImageResource(R.drawable.jornal_icone)

           }else if (notificacao.Categoria.equals("Pagamentos")){
               binding.imgNotificacao.setImageResource(R.drawable.pagamento_icone)

           }else if (notificacao.Categoria.equals("APP / Técnico")){
               binding.imgNotificacao.setImageResource(R.drawable.remoto)

           }


       }
    }
}