package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.content.Context
import android.widget.EditText
import androidx.core.app.NotificationCompat.getColor
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

fun MaskedEditText.validaFocus(isFcous: Boolean, context: Context) {
    if (!isFcous) {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)

    } else {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
        this.setTextColor(context.getColor(R.color.black))
    }
}