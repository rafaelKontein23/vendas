package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.AdapterMessoRegiao
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.AdapterUF
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.DialogConfig
import br.com.visaogrupo.tudofarmarep.databinding.DialogMessorrigiaoBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogUfBinding

class DialogDadosAreaDeAtuacao(private val context: Context,
                               private val viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao,
                               private val lifecycleOwner: LifecycleOwner

) {
    
    fun dialogMessoRegiao(uf:String){
        val binding = DialogMessorrigiaoBinding.inflate(LayoutInflater.from(context))
        val dialogMesorregiao = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(DialogMessorrigiaoBinding.inflate(LayoutInflater.from(context)).root)
        }
        DialogConfig.configuraDialog(dialogMesorregiao, context)
        dialogMesorregiao.setContentView(binding.root)


        binding.fecharAreaDeAtuacao.setOnClickListener {
            dialogMesorregiao.dismiss()
        }
        viewModelFragmentDadosAreaDeAtuacao.buscaDadosAreaDeAtuacaoMesorregiao(uf)
        binding.carregandoMessoRegiao.isVisible = true

        viewModelFragmentDadosAreaDeAtuacao.listaMesorregiao.observe(lifecycleOwner){ listaRespostaMessoRegiao ->
            binding.carregandoMessoRegiao.isVisible = false
            if (listaRespostaMessoRegiao == null){
                Alertas.alertaErro(context, context.getString(R.string.erroInternet), context.getString(R.string.tituloErro)){
                    dialogMesorregiao.dismiss()
                }
            }else{
                binding.recyclerAreaDeAtuacao.layoutManager = LinearLayoutManager(context)
                binding.recyclerAreaDeAtuacao.adapter = AdapterMessoRegiao( listaRespostaMessoRegiao,
                    viewModelFragmentDadosAreaDeAtuacao)
            }
        }

    }
    fun  dialogUF(listaUF: List<String>){
        val dialogUF = Dialog(context)
        val binding = DialogUfBinding.inflate(LayoutInflater.from(context))
        dialogUF.setContentView(binding.root)
        DialogConfig.configuraDialog(dialogUF, context)

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