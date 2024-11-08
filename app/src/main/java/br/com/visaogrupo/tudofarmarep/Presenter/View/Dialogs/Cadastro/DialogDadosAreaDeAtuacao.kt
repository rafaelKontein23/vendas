package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.databinding.DialogAreaDeAtuacaoBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogSenhaAmbienteBinding

class DialogDadosAreaDeAtuacao(private val activity: AppCompatActivity) {
    
    fun dialogDadosAreaDeAtuacao(){
        val dialogAreaDeAtuacao = Dialog(activity)
        val binding = DialogAreaDeAtuacaoBinding.inflate(LayoutInflater.from(activity))



        dialogAreaDeAtuacao.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAreaDeAtuacao.setContentView(binding.root)

        dialogAreaDeAtuacao.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogAreaDeAtuacao.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogAreaDeAtuacao.window?.attributes?.windowAnimations = R.style.animacaoDialog
        dialogAreaDeAtuacao.window?.setGravity(Gravity.BOTTOM)
        dialogAreaDeAtuacao.show()
    }
}