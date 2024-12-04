package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelContratoAceite
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.DialogConfig
import br.com.visaogrupo.tudofarmarep.databinding.DialogAssinarContratoBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogCidadesBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogPoliticaPrivacidadeBinding

class DialogContrato(val context: Context) {

    fun dialogContratoPolitica(titulo: String, texto: String) {
        val binding = DialogPoliticaPrivacidadeBinding.inflate(LayoutInflater.from(context))

        val dialogPolitica = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(DialogCidadesBinding.inflate(LayoutInflater.from(context)).root)
        }
        DialogConfig.configuraDialog(dialogPolitica, context)
        dialogPolitica.setContentView(binding.root)
        binding.textoDialog.setText(texto)
        binding.tituloDialog.setText(titulo)
        binding.fechar.setOnClickListener {
            dialogPolitica.dismiss()
        }
    }

    fun dialogAssina(viewModelContratoAceite: ViewModelContratoAceite) {
        val binding = DialogAssinarContratoBinding.inflate(LayoutInflater.from(context))

        val dialogAssina = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
        }


        dialogAssina.window?.let { window ->
            val layoutParams = window.attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
            window.attributes = layoutParams
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes.windowAnimations = R.style.animacaoDialog
            window.setGravity(Gravity.CENTER)

        }
        FormularioCadastro.savedBitmap?.let { bitmap ->
            binding.drawingView.hasDrawing = true
            binding.drawingView .setDrawingBitmap(bitmap)
        }
        binding.fecharModal.setOnClickListener {
            dialogAssina.dismiss()
        }
        dialogAssina.setOnCancelListener {
            (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        binding.limparAssinatura.setOnClickListener {
            FormularioCadastro.cadastro.isAssinaContrato = false
            FormularioCadastro.base64Assinatura = ""

            binding.drawingView.clear()
        }
        dialogAssina.setOnDismissListener {
            (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        binding.btnConfirma.setOnClickListener {
            val base64Assinatura = binding.drawingView.getDrawingAsBase64()
            if (!binding.drawingView.hasDrawing) {
                Toast.makeText(context, "Por favor, assine antes de confirmar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FormularioCadastro.base64Assinatura = base64Assinatura.toString()
            (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            val bitmap = binding.drawingView.getDrawingBitmap()
            FormularioCadastro.savedBitmap = bitmap

            dialogAssina.dismiss()
            viewModelContratoAceite.assinaturaContrato()

        }
        dialogAssina.show()
    }


}