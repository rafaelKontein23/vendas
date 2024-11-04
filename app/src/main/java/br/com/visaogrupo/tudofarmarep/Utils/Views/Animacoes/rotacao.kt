package br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes

import android.animation.ObjectAnimator
import android.view.View

fun View.rotateYView(rotacacaoFloat: Float) {
    val rotationY = ObjectAnimator.ofFloat(this, "rotationY", 0f, rotacacaoFloat)
    rotationY.duration = 0
    rotationY.start()
}