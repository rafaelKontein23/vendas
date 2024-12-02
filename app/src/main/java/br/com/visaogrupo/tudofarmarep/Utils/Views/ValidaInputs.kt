package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.ValidarTextos
import com.vicmikhailau.maskededittext.MaskedEditText
// Inputs com Mascaras
fun MaskedEditText.validaError(isError: Boolean, context: Context) {
    if (isError) {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
        this.setTextColor(context.getColor(R.color.danger500))
        mudaCorIconeRed(this, context)
    } else {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
        this.setTextColor(context.getColor(R.color.black))
        mudaCorIconeBlack(this, context)
    }

}

fun MaskedEditText.isFocus( context: Context) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
                this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
                this.setTextColor(context.getColor(R.color.black))
               mudaCorIconeGray(this, context)

        } else {
            this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
            this.setTextColor(context.getColor(R.color.black))
            mudaCorIconeBlack(this, context)

        }
    }
}

fun MaskedEditText.isFocusCPF( context: Context) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            if(this.text.toString().length <14 || !ValidarTextos.isCPF(this.text.toString())){
                this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
                this.setTextColor(context.getColor(R.color.danger500))
                mudaCorIconeRed(this, context)
            }else{
                this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
                this.setTextColor(context.getColor(R.color.black))
                mudaCorIconeGray(this, context)

            }


        } else {
            this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
            this.setTextColor(context.getColor(R.color.black))
            mudaCorIconeBlack(this, context)

        }
    }
}

fun EditText.isFocusEmail( context: Context) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            if(!this.text.toString().contains("@") || !this.text.toString().contains(".com")){
                this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
                this.setTextColor(context.getColor(R.color.danger500))

            }else{
                this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
                this.setTextColor(context.getColor(R.color.black))
            }


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

fun EditText.validaError(isError: Boolean, context: Context) {
    if (isError) {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
        this.setTextColor(context.getColor(R.color.danger500))
        mudaCorIconeRed(this, context)
    } else {
        this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
        this.setTextColor(context.getColor(R.color.black))
        mudaCorIconeBlack(this, context)
    }
}

fun TextView.validaError(isError: Boolean, context: Context) {
    if (isError) {
        this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
        this.setTextColor(context.getColor(R.color.danger500))
    } else {
        this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
        this.setTextColor(context.getColor(R.color.gray500))
    }
}

fun EditText.isFocusEditTextBasicoAgencia(context: Context ) {

    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            if (this.text.toString().length <4 ) {
                this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
                this.setTextColor(context.getColor(R.color.danger500))
                mudaCorIconeRed(this, context)
            } else {
                this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
                this.setTextColor(context.getColor(R.color.black))
                mudaCorIconeBlack(this, context)
            }
        } else {

            this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
            this.setTextColor(context.getColor(R.color.black))
            mudaCorIconeBlack(this, context)


        }
    }
}

fun EditText.isFocusEditTextBasico(context: Context ) {

    this.setOnFocusChangeListener { _, hasFocus ->

        if (!hasFocus) {
            if (this.text.toString().isEmpty()  ) {
                this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
                this.setTextColor(context.getColor(R.color.danger500))
                mudaCorIconeRed(this, context)
            } else {
                this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
                this.setTextColor(context.getColor(R.color.black))
                mudaCorIconeBlack(this, context)
            }
        } else {

            this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
            this.setTextColor(context.getColor(R.color.black))
            mudaCorIconeBlack(this, context)


        }
    }
}
fun EditText.isFocusEditTextBasicoSemErro(context: Context) {

    this.setOnFocusChangeListener { _, hasFocus ->

        if (!hasFocus) {

                this.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)
                this.setTextColor(context.getColor(R.color.black))
            mudaCorIconeGray(this, context)

        } else {

            this.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
            this.setTextColor(context.getColor(R.color.black))
            mudaCorIconeBlack(this, context)


        }
    }
}
fun mudaCorIconeBlack(editText: EditText, context: Context){
    val drawableStart = editText.compoundDrawablesRelative[0]

    if (drawableStart != null) {
        val newColor = ContextCompat.getColor(context, R.color.black)
        drawableStart.setTint(newColor)

        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            editText.compoundDrawablesRelative[1],
            editText.compoundDrawablesRelative[2],
            editText.compoundDrawablesRelative[3]
        )
    }
}



fun mudaCorIconeRed(editText: EditText, context: Context){
    val drawableStart = editText.compoundDrawablesRelative[0]

    if (drawableStart != null) {
        val newColor = ContextCompat.getColor(context, R.color.danger500)
        drawableStart.setTint(newColor)

        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            editText.compoundDrawablesRelative[1],
            editText.compoundDrawablesRelative[2],
            editText.compoundDrawablesRelative[3]
        )
    }
}
fun mudaCorIconeGray(editText: EditText, context: Context){
    val drawableStart = editText.compoundDrawablesRelative[0]

    if (drawableStart != null) {
        val newColor = ContextCompat.getColor(context, R.color.gray400)
        drawableStart.setTint(newColor)

        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            editText.compoundDrawablesRelative[1],
            editText.compoundDrawablesRelative[2],
            editText.compoundDrawablesRelative[3]
        )
    }
}


