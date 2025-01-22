package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home.ActHome
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActToken
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.databinding.DialogWhatsBinding

class DialogWhats {


    fun dialogWhats(context: Context, vireModel:ViewModelActToken,lifecycleOwner: LifecycleOwner){
        val dialogWhats = Dialog(context)
        val binding = DialogWhatsBinding.inflate(LayoutInflater.from(context))

        dialogWhats.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogWhats.setContentView(binding.root)
        dialogWhats.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogWhats.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogWhats.window?.attributes?.windowAnimations = R.style.animacaoDialog
        dialogWhats.window?.setGravity(Gravity.BOTTOM)
        dialogWhats.show()
        binding.setaVoltarImg.setOnClickListener {
            dialogWhats.dismiss()
        }
        binding.naoTenhoAcessoWhats.setOnClickListener {
            dialogWhats.dismiss()
        }
        binding.btnContinuar.setOnClickListener {
            vireModel.buscaInformacoesLogin()
        }
        vireModel.login.observe(lifecycleOwner){

            if(it != null){
                vireModel.salvarDadosUsuario(it.Representante_ID,it.Nome?: "",it.Hash ?: "",it.FotoPerfil ?: "", it.UF ?:"" )

                if(it.Representante_ID != 0){
                  context.  startActivity(Intent(context, ActHome::class.java))

                }else{
                    if (it.Status_Cod == 88){
                        vireModel.mudaStatusCadastro()
                        context. startActivity(Intent(context, ActCabecalho::class.java))

                    }else{
                        Alertas.alertaErro(context,it.Mensagem,context.getString(R.string.tituloErro)){
                        }
                    }
                }
            }else{
                Alertas.alertaErro(context,context.getString(R.string.erroPadrao),context.getString(R.string.tituloErro)){
                }
            }
        }

    }
}