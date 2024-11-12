package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Domain.UseCase.Model.MessoRegiao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.databinding.ItemMessorregiaoBinding
import br.com.visaogrupo.tudofarmarep.databinding.ItemUfBinding

class AdapterMessoRegiao(val respostaMessoRegiao: List<RespostaMessoRegiao>,
                         val viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao ) : RecyclerView.Adapter<AdapterMessoRegiao.viewHolderMessoRegiao>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderMessoRegiao {
        val binding = ItemMessorregiaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolderMessoRegiao(binding)
    }
    override fun onBindViewHolder(holder: viewHolderMessoRegiao, position: Int) {
        val respostaMessoRegiao = respostaMessoRegiao[position]
        holder.bind(respostaMessoRegiao, viewModelFragmentDadosAreaDeAtuacao)
    }
    override fun getItemCount(): Int {
           return respostaMessoRegiao.size
    }
    class  viewHolderMessoRegiao(private val binding: ItemMessorregiaoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(respostaMessoRegiao: RespostaMessoRegiao,viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao){
            binding.textoSelecionarMessoRegiao.text = respostaMessoRegiao.Mesorregiao_Nome
            binding.textoSelecionarMessoRegiao.setOnClickListener {
               val messoRegiaoDados = MessoRegiao(respostaMessoRegiao.Mesorregiao_id, respostaMessoRegiao.Mesorregiao_Nome)
               if(!viewModelFragmentDadosAreaDeAtuacao.confereMessoRegiao(messoRegiaoDados)){
                   viewModelFragmentDadosAreaDeAtuacao.adicionaNaListaMesorregiao(messoRegiaoDados)
                   binding.imgCheck.isVisible = true
                   binding.textoSelecionarMessoRegiao.setTextColor(binding.root.context.getColor(R.color.blue500))
               }else{
                   viewModelFragmentDadosAreaDeAtuacao.removeDaListaMesorregiao(messoRegiaoDados)
                   binding.imgCheck.isVisible = false
                   binding.textoSelecionarMessoRegiao.setTextColor(binding.root.context.getColor(R.color.gray600))
               }
            }
        }
    }

}