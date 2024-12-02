package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro.AdapterCidades.ViewHolderCidades
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelDadosBancarios
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancaria
import br.com.visaogrupo.tudofarmarep.databinding.ItemCidadeBinding
import br.com.visaogrupo.tudofarmarep.databinding.ItemInstituicaoBinding

class AdapterInstituicaoBancaria(
     listaInstituicoes:ArrayList< RespostaInstituicaoBancaria?>,
    private val context: Context,
    private val viewModel: ViewModelDadosBancarios ,
    private val instituicaoSelecionada :String,
    private val dialoag: Dialog
) : RecyclerView.Adapter<AdapterInstituicaoBancaria.ViewHolderInstituicaoBancaria>() {
    var listaInstituicoes = listaInstituicoes
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInstituicaoBancaria {
        val binding = ItemInstituicaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderInstituicaoBancaria(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderInstituicaoBancaria, position: Int) {
        val instituicao = listaInstituicoes[position]
        holder.bind(instituicao, viewModel, context, instituicaoSelecionada, dialoag)
    }

    override fun getItemCount(): Int {
        return listaInstituicoes.size
    }

    // ViewHolder interno
    class ViewHolderInstituicaoBancaria(private val binding: ItemInstituicaoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            instituicao: RespostaInstituicaoBancaria?,
            viewModel: ViewModelDadosBancarios,
            context: Context,
            instituicaoSelecionada: String,
            dialoag: Dialog
        ) {
            binding.textoSelecionarInstituicao.text = instituicao?.Descricao ?:""
            if(instituicaoSelecionada == instituicao?.Descricao){
                binding.imgCheck.isVisible = true
                binding.textoSelecionarInstituicao.setTextColor(context.getColor(R.color.blue500))
            }else{
                binding.imgCheck.isVisible = false
                binding.textoSelecionarInstituicao.setTextColor(context.getColor(R.color.gray600))
            }
            binding.root.setOnClickListener {
                viewModel.alterarTextoInstituicao(instituicao!!.Descricao)
                dialoag.dismiss()
            }
        }
    }
}
