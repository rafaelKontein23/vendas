package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import br.com.visaogrupo.tudofarmarep.R

class Dialogs (val context: Context) {
     fun dialogTrocaAmbiente() {
          val dialogversion = Dialog(context)
          dialogversion.requestWindowFeature(Window.FEATURE_NO_TITLE)
          dialogversion.setContentView(R.layout.dialog_senha_ambiente)

          dialogversion.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
          dialogversion.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          dialogversion.window!!.attributes.windowAnimations = R.style.animacaoDialog
          dialogversion.window!!.setGravity(Gravity.BOTTOM)
          dialogversion.show()


     }
}