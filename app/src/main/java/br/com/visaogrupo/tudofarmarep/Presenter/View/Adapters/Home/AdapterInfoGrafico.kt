package br.com.visaogrupo.tudofarmarep.Adapter

import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import br.com.visaogrupo.tudofarmarep.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

class AdapterInfoGrafico (context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val porcentagemTexto: TextView = findViewById(R.id.porcentagemTexto)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        porcentagemTexto.text = "${e?.y?.toInt()}"
        super.refreshContent(e, highlight)
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        // Desenha a view no local desejado
        canvas.translate(posX - width / 2, posY - height)
        draw(canvas)
    }
}