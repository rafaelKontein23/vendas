package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidades
import br.com.visaogrupo.tudofarmarep.databinding.ItemCidadeBinding

class AdapterCidades (
    val respostaCidades: List<RespostaCidades>,
    val viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao,
    val context: Context
): RecyclerView.Adapter<AdapterCidades.ViewHolderCidades>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCidades {
        val binding = ItemCidadeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderCidades(binding)
    }
    override fun onBindViewHolder(holder: ViewHolderCidades, position: Int) {
        val respostaCidade = respostaCidades[position]
        holder.bind(respostaCidade, viewModelFragmentDadosAreaDeAtuacao,
            context)
    }
    override fun getItemCount(): Int {
        return respostaCidades.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class  ViewHolderCidades( private val binding: ItemCidadeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(respostaCidades: RespostaCidades,viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao, context: Context){
            binding.textoSelecionarCidade.text = respostaCidades.Cidade
            if(viewModelFragmentDadosAreaDeAtuacao.confereCidades(respostaCidades)){
                binding.imgCheck.isVisible = true
                binding.textoSelecionarCidade.setTextColor(context.getColor(R.color.blue500))
            }else{
                binding.imgCheck.isVisible = false
                binding.textoSelecionarCidade.setTextColor(context.getColor(R.color.gray600))
            }

            binding.textoSelecionarCidade.setOnClickListener {
                if(!viewModelFragmentDadosAreaDeAtuacao.confereCidades(respostaCidades)){
                    viewModelFragmentDadosAreaDeAtuacao.adicionaNaListaCidades(respostaCidades)
                    binding.imgCheck.isVisible = true
                    binding.textoSelecionarCidade.setTextColor(context.getColor(R.color.blue500))
                }else{
                    viewModelFragmentDadosAreaDeAtuacao.removeDaListaCidades(respostaCidades)
                    binding.imgCheck.isVisible = false
                    binding.textoSelecionarCidade.setTextColor(context.getColor(R.color.gray600))
                }
            }
        }
    }

}