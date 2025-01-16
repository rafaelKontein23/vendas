package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaNotificacao
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
       fun bind(listaNotificacao:RespostaNotificacao
       ) {
           binding.tituloNotificacao.text = listaNotificacao.titulo
       }
    }
}