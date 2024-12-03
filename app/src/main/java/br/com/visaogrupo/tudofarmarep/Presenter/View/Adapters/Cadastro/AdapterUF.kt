package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.databinding.ItemUfBinding

class AdapterUF(private val estados: List<String>,
                val viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao,
               val context: Context, val dialog:Dialog) : RecyclerView.Adapter<AdapterUF.ViewHolderUF>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUF {
        val binding = ItemUfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderUF(binding)
    }

    override fun getItemCount(): Int = estados.size

    override fun onBindViewHolder(holder: ViewHolderUF, position: Int) {
        holder.bind(estados[position], viewModelFragmentDadosAreaDeAtuacao, context, dialog)
    }

    class ViewHolderUF(private val binding: ItemUfBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(estado: String,
                 viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao,
                 context: Context,
                 dialog: Dialog) {

             binding.textoSelecionarUF.text = estado

             binding.constrainUF.setOnClickListener {
                 binding.imgCheck.isVisible = true
                 dialog.dismiss()
                 binding.textoSelecionarUF.setTextColor(context.getColor(R.color.blue500))
                 val sigla = estado.split(" - ").last()
                 viewModelFragmentDadosAreaDeAtuacao.limparListas()
                 viewModelFragmentDadosAreaDeAtuacao.buscaDadosAreaDeAtuacaoMesorregiao(sigla, true)
                 viewModelFragmentDadosAreaDeAtuacao.selecionaUF(estado)
             }
            val siglas = estado.split(" - ").last()
            val ufItem = viewModelFragmentDadosAreaDeAtuacao.ufSelecionada.value ?: viewModelFragmentDadosAreaDeAtuacao.ufTextoObs.value
            val siglasUFSelecionada = ufItem?.split(" - ")?.last()
            if(siglas == siglasUFSelecionada){
                binding.imgCheck.isVisible = true
                binding.textoSelecionarUF.setTextColor(context.getColor(R.color.blue500))
            }else{
                binding.imgCheck.isVisible = false
                binding.textoSelecionarUF.setTextColor(context.getColor(R.color.gray600))
            }
        }
    }
}