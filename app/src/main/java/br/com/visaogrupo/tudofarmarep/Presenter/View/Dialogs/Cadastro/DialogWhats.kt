package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActToken
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraTelefone
import br.com.visaogrupo.tudofarmarep.databinding.DialogWhatsBinding


class DialogWhats {


    fun dialogWhats(context: Context, vireModel:ViewModelActToken,lifecycleOwner: LifecycleOwner, numero:String){
        val dialogWhats = Dialog(context)
        val binding = DialogWhatsBinding.inflate(LayoutInflater.from(context))

        dialogWhats.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogWhats.setContentView(binding.root)
        dialogWhats.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogWhats.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogWhats.window?.attributes?.windowAnimations = R.style.animacaoDialog
        dialogWhats.window?.setGravity(Gravity.BOTTOM)
        dialogWhats.setCancelable(false)
        dialogWhats.show()

        val numeroFormatado: String = numero.aplicarMascaraTelefone()
        val textoInicial = "Enviamos uma mensagem de confirmação para o seu WhatsApp "
        val textoFinal = ". Acesse e confirme para continuar o seu acesso."
        val textoCompleto = textoInicial + numeroFormatado + textoFinal

        val start = textoInicial.length
        val end = start + numeroFormatado.length

        val spannableString = SpannableString(textoCompleto)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        binding.descricaoCelular.text = spannableString

        binding.irparaWhats.setOnClickListener {
            val urlBase = "https://wa.me/$"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(urlBase)
            }
            context.startActivity(intent)

        }


        binding.setaVoltarImg.setOnClickListener {
            dialogWhats.dismiss()
        }
        binding.naoTenhoAcessoWhats.setOnClickListener {
            vireModel.solicitaToken(numero,0)
            Alertas.alertaErro(binding.textView.context, binding.textView.context.getString(R.string.seuTokenseraEnviadoPorSms),binding.textView.context.getString(R.string.loiuInforma)){

            }
            dialogWhats.dismiss()

        }
        binding.btnContinuar.setOnClickListener {
            dialogWhats.dismiss()
        }
    }
}