package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Window
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro.AdapterCidades
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro.AdapterMessoRegiao
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro.AdapterUF
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.DialogConfig
import br.com.visaogrupo.tudofarmarep.databinding.DialogCidadesBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogMessorrigiaoBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogUfBinding

class DialogDadosAreaDeAtuacao(private val context: Context,
                               private val viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao,
                               private val lifecycleOwner: LifecycleOwner

) {
     var adapterCidades : AdapterCidades? = null
     var adapterMessoRegiao : AdapterMessoRegiao? = null

    fun dialogCidades(){

         val binding = DialogCidadesBinding.inflate(LayoutInflater.from(context))
         val dialogCidades = Dialog(context).apply {
             requestWindowFeature(Window.FEATURE_NO_TITLE)
             setContentView(DialogCidadesBinding.inflate(LayoutInflater.from(context)).root)
         }
        DialogConfig.configuraDialog(dialogCidades, context)
        dialogCidades.setContentView(binding.root)
        binding.fecharAreaDeAtuacao.setOnClickListener {
            dialogCidades.dismiss()
        }
        binding.btnSelecionar.setOnClickListener {
            dialogCidades.dismiss()
        }
        viewModelFragmentDadosAreaDeAtuacao.listaCidadesBusca.observe(lifecycleOwner){
            if (adapterCidades != null && it != null){
                adapterCidades?.listaCidades  = it
                adapterCidades?.notifyDataSetChanged()
            }


        }

        binding.inputBuscaMessoRegiao.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               val cidade = s.toString()
                viewModelFragmentDadosAreaDeAtuacao.buscaCidades(cidade)

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.carregandoMessoRegiao.isVisible = true
        viewModelFragmentDadosAreaDeAtuacao.listaCidadesObs.observe(lifecycleOwner){ listaRespostaCidades ->
            binding.carregandoMessoRegiao.isVisible = false
            if (listaRespostaCidades != null){
                binding.recyclerAreaDeAtuacao.layoutManager = LinearLayoutManager(context)
                adapterCidades =  AdapterCidades( listaRespostaCidades,
                    viewModelFragmentDadosAreaDeAtuacao, context)
                binding.recyclerAreaDeAtuacao.adapter =adapterCidades
            }
        }

    }
    fun dialogMessoRegiao(uf:String, isCadastro: Boolean = true){
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
        binding.btnSelecionar.setOnClickListener {
            dialogMesorregiao.dismiss()
        }
        if (isCadastro){
            viewModelFragmentDadosAreaDeAtuacao.buscaDadosAreaDeAtuacaoMesorregiao(uf, false)

        }
        binding.carregandoMessoRegiao.isVisible = true

        viewModelFragmentDadosAreaDeAtuacao.listaMessoRegiaoBusca.observe(lifecycleOwner){
            if (adapterMessoRegiao != null && it != null){
                adapterMessoRegiao?.respostaMessoRegiao  = it
                adapterMessoRegiao?.notifyDataSetChanged()
            }
        }
        binding.inputBuscaMessoRegiao.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val mesoRegiao = s.toString()
                viewModelFragmentDadosAreaDeAtuacao.buscaMessoRegiao(mesoRegiao)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        viewModelFragmentDadosAreaDeAtuacao.listaMesorregiao.observe(lifecycleOwner){ listaRespostaMessoRegiao ->
            binding.carregandoMessoRegiao.isVisible = false
            if (listaRespostaMessoRegiao != null){
                binding.recyclerAreaDeAtuacao.layoutManager = LinearLayoutManager(context)
                adapterMessoRegiao =  AdapterMessoRegiao( listaRespostaMessoRegiao,
                    viewModelFragmentDadosAreaDeAtuacao, context)
                binding.recyclerAreaDeAtuacao.adapter = adapterMessoRegiao
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