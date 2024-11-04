package br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout

class Progress {
    companion object {
        fun LinearLayout.animateProgressBarHorizontal(toWeight: Float, duration: Long) {
            val animator = ValueAnimator.ofFloat(0f, toWeight)
            animator.duration = duration
            animator.interpolator = LinearInterpolator()
            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                val params = this.layoutParams as LinearLayout.LayoutParams
                params.weight = animatedValue
                this.layoutParams = params
            }
            animator.start()
        }

    }
}