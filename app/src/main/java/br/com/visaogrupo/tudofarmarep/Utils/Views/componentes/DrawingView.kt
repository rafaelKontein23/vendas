package br.com.visaogrupo.tudofarmarep.Utils.Views.componentes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private val paths = mutableListOf<Path>()
    private var currentPath: Path? = null

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Começa um novo caminho ao tocar
                currentPath = Path()
                currentPath?.moveTo(x, y)
                paths.add(currentPath!!)
            }
            MotionEvent.ACTION_MOVE -> {
                // Adiciona pontos ao caminho enquanto o dedo se move
                currentPath?.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                // Finaliza o caminho quando o dedo é levantado
                currentPath = null
            }
        }

        invalidate()  // Redesenha a view
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (path in paths) {
            canvas.drawPath(path, paint)
        }
    }

    fun clear() {
        paths.clear()
        invalidate()
    }
}
