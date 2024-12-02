package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Window
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home.AdapterInstituicaoBancaria
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancaria
import br.com.visaogrupo.tudofarmarep.Utils.Views.DialogConfig
import br.com.visaogrupo.tudofarmarep.databinding.DialogInstituicaoBancariaBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogMessorrigiaoBinding

class DialogInstituicao {


    fun dialogInstituicao(context: Context,  listaInstituicao: ArrayList< RespostaInstituicaoBancaria?>, viewModelDadosBancarios: ViewModelDadosBancarios, instituicaoBancaria: String, viewLifecycleOwner: LifecycleOwner) {
        val binding = DialogInstituicaoBancariaBinding.inflate(LayoutInflater.from(context))
        val dialogInstituicao = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(DialogInstituicaoBancariaBinding.inflate(LayoutInflater.from(context)).root)
        }
        DialogConfig.configuraDialog(dialogInstituicao, context)
        dialogInstituicao.setContentView(binding.root)
        val adapter = AdapterInstituicaoBancaria( listaInstituicao, context,viewModelDadosBancarios, instituicaoBancaria, dialogInstituicao )
        binding.recyclerInstituicao.layoutManager = LinearLayoutManager(context)
        binding.recyclerInstituicao.adapter = adapter

        viewModelDadosBancarios.listaBancoPesquisa.observe(viewLifecycleOwner){
            if(binding.inputBuscaInstituicao.text.toString().isEmpty()){
                adapter.listaInstituicoes = listaInstituicao

            }else{
                adapter.listaInstituicoes = it
            }
            adapter.notifyDataSetChanged()

        }
         binding.fecharIntituicaoBancaria.setOnClickListener {
             dialogInstituicao.dismiss()

         }
        binding.inputBuscaInstituicao.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val texto = s.toString()
                viewModelDadosBancarios.pesquisaInstituicao(texto)

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }
}