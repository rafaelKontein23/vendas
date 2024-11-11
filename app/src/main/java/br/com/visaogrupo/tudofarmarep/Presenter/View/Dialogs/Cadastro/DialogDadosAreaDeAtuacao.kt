package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.AdapterUF
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.databinding.DialogAreaDeAtuacaoBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogUfBinding

class DialogDadosAreaDeAtuacao(private val context: Context, private val viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao) {
    
    fun dialogDadosAreaDeAtuacao(){
        val dialogAreaDeAtuacao = Dialog(context)
        val binding = DialogAreaDeAtuacaoBinding.inflate(LayoutInflater.from(context))



        dialogAreaDeAtuacao.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAreaDeAtuacao.setContentView(binding.root)

        dialogAreaDeAtuacao.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogAreaDeAtuacao.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogAreaDeAtuacao.window?.attributes?.windowAnimations = R.style.animacaoDialog
        dialogAreaDeAtuacao.window?.setGravity(Gravity.BOTTOM)
        dialogAreaDeAtuacao.show()
    }
    fun  dialogUF(listaUF: List<String>){
        val dialogUF = Dialog(context)
        val binding = DialogUfBinding.inflate(LayoutInflater.from(context))
        dialogUF.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogUF.setContentView(binding.root)
        dialogUF.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogUF.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogUF.window?.attributes?.windowAnimations = R.style.animacaoDialog
        dialogUF.window?.setGravity(Gravity.BOTTOM)
        dialogUF.show()
        binding.fecharAreaDeAtuacao.setOnClickListener {
            dialogUF.dismiss()
        }

        val adapterUF = AdapterUF(listaUF,
            viewModelFragmentDadosAreaDeAtuacao,
            context,
            dialogUF)
        binding.recyclerUF.adapter = adapterUF
        binding.recyclerUF.layoutManager = LinearLayoutManager(context)

    }
}