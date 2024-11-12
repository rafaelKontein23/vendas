package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import br.com.visaogrupo.tudofarmarep.R

class DialogConfig {
    companion object {
        fun configuraDialog(dialog: Dialog, context: Context){
            dialog.window?.let { window ->
                val layoutParams = window.attributes?.apply {
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = (context.resources.displayMetrics.heightPixels * 0.95).toInt()
                }
                window.attributes = layoutParams
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes?.windowAnimations = R.style.animacaoDialog
                window.setGravity(Gravity.BOTTOM)
            }
            dialog.show()
        }
    }

}