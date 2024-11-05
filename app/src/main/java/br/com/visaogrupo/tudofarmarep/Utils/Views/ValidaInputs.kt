package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.content.Context
import android.widget.Spinner
import android.widget.TextView
import br.com.visaogrupo.tudofarmarep.R
import com.vicmikhailau.maskededittext.MaskedEditText
// Inputs com Mascaras
fun MaskedEditText.validaError(isError: Boolean, context: Context) {
    if (isError) {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
        this.setTextColor(context.getColor(R.color.danger500))
    } else {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
        this.setTextColor(context.getColor(R.color.black))
    }

}

fun MaskedEditText.isFocus( context: Context) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
        } else {
            this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
            this.setTextColor(context.getColor(R.color.black))
        }
    }
}

fun Spinner.validaError(isError: Boolean, context: Context, textoSpinner: TextView) {
    if (isError) {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
        textoSpinner.setTextColor(context.getColor(R.color.danger500))
    }else{
        this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
        textoSpinner.setTextColor(context.getColor(R.color.gray400))

    }
}



