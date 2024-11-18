package br.com.visaogrupo.tudofarmarep.Utils.Views.componentes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import java.io.ByteArrayOutputStream

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private val paths = mutableListOf<Path>()
    private var currentPath: Path? = null
    private var restoredBitmap: Bitmap? = null

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
    }

    fun getDrawingBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas) // Renderiza o conteúdo da view no bitmap
        return bitmap
    }

    fun getDrawingAsBase64(): String {
        val bitmap = getDrawingBitmap()
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun setDrawingBitmap(bitmap: Bitmap) {
        restoredBitmap = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Desenha o bitmap restaurado se existir
        restoredBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        // Desenha os caminhos
        for (path in paths) {
            canvas.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath?.moveTo(x, y)
                paths.add(currentPath!!)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                currentPath = null
            }
        }

        invalidate()
        return true
    }

    fun clear() {
        paths.clear()
        restoredBitmap = null
        invalidate()
    }
}
