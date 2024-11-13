package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.databinding.ItemMessorregiaoBinding

class AdapterMessoRegiao(val respostaMessoRegiao: List<RespostaMessoRegiao>,
                         val viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao,
                         val context: Context
    ) : RecyclerView.Adapter<AdapterMessoRegiao.viewHolderMessoRegiao>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderMessoRegiao {
        val binding = ItemMessorregiaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolderMessoRegiao(binding)
    }
    override fun onBindViewHolder(holder: viewHolderMessoRegiao, position: Int) {
        val respostaMessoRegiao = respostaMessoRegiao[position]
        holder.bind(respostaMessoRegiao, viewModelFragmentDadosAreaDeAtuacao,
            context)
    }
    override fun getItemCount(): Int {
           return respostaMessoRegiao.size
    }
    class  viewHolderMessoRegiao(private val binding: ItemMessorregiaoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(respostaMessoRegiao: RespostaMessoRegiao,
                 viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao,
                 context: Context){
            binding.textoSelecionarMessoRegiao.text = respostaMessoRegiao.Mesorregiao_Nome

            if(viewModelFragmentDadosAreaDeAtuacao.confereMessoRegiao(respostaMessoRegiao)){
                binding.imgCheck.isVisible = true
                binding.textoSelecionarMessoRegiao.setTextColor(context.getColor(R.color.blue500))
            }else{
                binding.imgCheck.isVisible = false
                binding.textoSelecionarMessoRegiao.setTextColor(context.getColor(R.color.gray600))
            }

            binding.textoSelecionarMessoRegiao.setOnClickListener {
                if (respostaMessoRegiao.Mesorregiao_Nome == "Todos"){
                    if (binding.textoSelecionarMessoRegiao.tag == 1){
                        binding.textoSelecionarMessoRegiao.tag = 0
                        binding.imgCheck.isVisible = false
                        binding.textoSelecionarMessoRegiao.setTextColor(context.getColor(R.color.gray600))
                        viewModelFragmentDadosAreaDeAtuacao.removeDaListaTodasMesorregiao()

                    }else{
                        binding.textoSelecionarMessoRegiao.tag = 1
                        binding.imgCheck.isVisible = true
                        binding.textoSelecionarMessoRegiao.setTextColor(context.getColor(R.color.blue500))
                        viewModelFragmentDadosAreaDeAtuacao.adicionaNaListaTodasMesorregiao()

                    }

                }else{
                    if(!viewModelFragmentDadosAreaDeAtuacao.confereMessoRegiao(respostaMessoRegiao)){
                        viewModelFragmentDadosAreaDeAtuacao.adicionaNaListaMesorregiao(respostaMessoRegiao)
                        binding.imgCheck.isVisible = true
                        binding.textoSelecionarMessoRegiao.setTextColor(context.getColor(R.color.blue500))
                    }else{
                        viewModelFragmentDadosAreaDeAtuacao.removeDaListaMesorregiao(respostaMessoRegiao)
                        binding.imgCheck.isVisible = false
                        binding.textoSelecionarMessoRegiao.setTextColor(context.getColor(R.color.gray600))
                    }
                }

            }
        }
    }
}
