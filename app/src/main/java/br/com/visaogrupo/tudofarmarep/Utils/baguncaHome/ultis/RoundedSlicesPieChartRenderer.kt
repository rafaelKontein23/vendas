import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import br.com.visaogrupo.tudofarmarep.Objetos.GraficoMarca
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.utils.Utils
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class RoundedSlicesPieChartRenderer(
    chart: PieChart?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?,
    val  listaCorFundo: List<Int>, val listagraficoMaca:ArrayList<GraficoMarca>
) : PieChartRenderer(chart, animator, viewPortHandler) {

    init {
        chart?.setDrawRoundedSlices(true)
    }

    override fun drawDataSet(c: Canvas?, dataSet: IPieDataSet) {
        var angle = 0f
        val rotationAngle = mChart.rotationAngle
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY
        val circleBox = mChart.circleBox
        val entryCount = dataSet.entryCount
        val drawAngles = mChart.drawAngles
        val center = mChart.centerCircleBox
        val radius = mChart.radius
        val drawInnerArc = mChart.isDrawHoleEnabled && !mChart.isDrawSlicesUnderHoleEnabled
        val userInnerRadius = if (drawInnerArc) radius * (mChart.holeRadius / 100f) else 0f
        val roundedRadius = 14f
        val roundedCircleBox = RectF()
        mChart.holeRadius = 88f

        var visibleAngleCount = 0
        for (j in 0 until entryCount) {
            // draw only if the value is greater than zero
            if (abs(dataSet.getEntryForIndex(j).y) > Utils.FLOAT_EPSILON) {
                visibleAngleCount++
            }
        }
        val sliceSpace = 35f

        val pathBuffer = Path()
        val mInnerRectBuffer = RectF()
        for (j in 0 until entryCount) {
            val sliceAngle = drawAngles[j]
            var innerRadius = userInnerRadius
            val e = dataSet.getEntryForIndex(j)

            // draw only if the value is greater than zero
            if (abs(e.y) <= Utils.FLOAT_EPSILON) {
                angle += sliceAngle * phaseX
                continue
            }

            // Don't draw if it's highlighted, unless the chart uses rounded slices
            if (mChart.needsHighlight(j) && !drawInnerArc) {
                angle += sliceAngle * phaseX
                continue
            }
            val accountForSliceSpacing = sliceSpace > 0f && sliceAngle <= 180f
            mRenderPaint.color = dataSet.getColor(j)
            val sliceSpaceAngleOuter =
                if (visibleAngleCount == 1) 0f else sliceSpace / (Utils.FDEG2RAD * radius)
            val startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2f) * phaseY
            var sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY
            if (sweepAngleOuter < 0f) {
                sweepAngleOuter = 0f
            }
            pathBuffer.reset()
            val arcStartPointX =
                center.x + radius * cos(startAngleOuter * Utils.FDEG2RAD).toFloat()
            val arcStartPointY =
                center.y + radius * sin(startAngleOuter * Utils.FDEG2RAD).toFloat()
            if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                // Android is doing "mod 360"
                pathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW)
            } else {
                if (drawInnerArc) {
                    val x =
                        center.x + (radius - roundedRadius) * cos(startAngleOuter * Utils.FDEG2RAD)
                    val y =
                        center.y + (radius - roundedRadius) * sin(startAngleOuter * Utils.FDEG2RAD)
                    roundedCircleBox[x - roundedRadius, y - roundedRadius, x + roundedRadius] =
                        y + roundedRadius
                    pathBuffer.arcTo(roundedCircleBox, startAngleOuter - 180, 180F)
                }
                pathBuffer.arcTo(
                    circleBox,
                    startAngleOuter,
                    sweepAngleOuter
                )
            }

            mInnerRectBuffer[center.x - innerRadius, center.y - innerRadius, center.x + innerRadius] =
                center.y + innerRadius
            if (drawInnerArc && (innerRadius > 0f || accountForSliceSpacing)) {
                if (accountForSliceSpacing) {
                    var minSpacedRadius = calculateMinimumRadiusForSpacedSlice(
                        center, radius,
                        sliceAngle * phaseY,
                        arcStartPointX, arcStartPointY,
                        startAngleOuter,
                        sweepAngleOuter
                    )
                    if (minSpacedRadius < 0f) minSpacedRadius = -minSpacedRadius
                    innerRadius = innerRadius.coerceAtLeast(minSpacedRadius)
                }
                val sliceSpaceAngleInner =
                    if (visibleAngleCount == 1 || innerRadius == 0f) 0f else sliceSpace / (Utils.FDEG2RAD * innerRadius)
                val startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2f) * phaseY
                var sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY
                if (sweepAngleInner < 0f) {
                    sweepAngleInner = 0f
                }
                val endAngleInner = startAngleInner + sweepAngleInner
                if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                    // Android is doing "mod 360"
                    pathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW)
                } else {
                    val x =
                        center.x + (radius - roundedRadius) * cos(endAngleInner * Utils.FDEG2RAD)
                    val y =
                        center.y + (radius - roundedRadius) * sin(endAngleInner * Utils.FDEG2RAD)
                    roundedCircleBox[x - roundedRadius, y - roundedRadius, x + roundedRadius] =
                        y + roundedRadius
                    pathBuffer.arcTo(roundedCircleBox, endAngleInner, 180F)
                    pathBuffer.arcTo(mInnerRectBuffer, endAngleInner, -sweepAngleInner)
                }
            } else {
                if (sweepAngleOuter % 360f > Utils.FLOAT_EPSILON) {
                    if (accountForSliceSpacing) {
                        val angleMiddle = startAngleOuter + sweepAngleOuter / 2f
                        val sliceSpaceOffset = calculateMinimumRadiusForSpacedSlice(
                            center,
                            radius,
                            sliceAngle * phaseY,
                            arcStartPointX,
                            arcStartPointY,
                            startAngleOuter,
                            sweepAngleOuter
                        )
                        val arcEndPointX = center.x +
                                sliceSpaceOffset * cos(angleMiddle * Utils.FDEG2RAD)
                        val arcEndPointY = center.y +
                                sliceSpaceOffset * sin(angleMiddle * Utils.FDEG2RAD)
                        pathBuffer.lineTo(
                            arcEndPointX,
                            arcEndPointY
                        )
                    } else {
                        pathBuffer.lineTo(
                            center.x,
                            center.y
                        )
                    }
                }
            }
            pathBuffer.close()
            mBitmapCanvas.drawPath(pathBuffer, mRenderPaint)
            angle += sliceAngle * phaseX
        }
        MPPointF.recycleInstance(center)
    }
    override fun drawValues(c: Canvas) {
        super.drawValues(c)

        val center = mChart.centerCircleBox
        val radius = mChart.radius
        var rotationAngle = mChart.rotationAngle
        val drawAngles = mChart.drawAngles
        val absoluteAngles = mChart.absoluteAngles
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val roundedRadius = (radius - radius * mChart.holeRadius / 100f) / 2f
        val holeRadiusPercent = mChart.holeRadius / 100f
        var labelRadiusOffset = radius / 10f * 3.6f

        if (mChart.isDrawHoleEnabled) {
            labelRadiusOffset = (radius - radius * holeRadiusPercent) / 2f
            if (!mChart.isDrawSlicesUnderHoleEnabled && mChart.isDrawRoundedSlicesEnabled) {
                rotationAngle += roundedRadius * 360 / (Math.PI * 2 * radius).toFloat()
            }
        }

        val labelRadius = radius - labelRadiusOffset
        val dataSets = mChart.data.dataSets
        var angle: Float
        var xIndex = 0

        c.save()
        for (i in dataSets.indices) {
            val dataSet = dataSets[i]
            val sliceSpace = getSliceSpace(dataSet)
            for (j in 0 until dataSet.entryCount) {
                angle = if (xIndex == 0) 0f else absoluteAngles[xIndex - 1] * phaseX
                val sliceAngle = drawAngles[xIndex]
                val sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius)
                angle += (sliceAngle - sliceSpaceMiddleAngle / 2f) / 2f

                if (dataSet.valueLineColor != ColorTemplate.COLOR_NONE) {
                    val transformedAngle = rotationAngle + angle * phaseY
                    val sliceXBase = cos(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()
                    val sliceYBase = sin(transformedAngle * Utils.FDEG2RAD.toDouble()).toFloat()

                    // Calcula a posição fora do círculo
                    val valueLinePart1OffsetPercentage = 1.2f // Um pouco além do raio para ficar fora do círculo
                    val line1Radius = radius * valueLinePart1OffsetPercentage

                    val px = line1Radius * sliceXBase + center.x
                    val py = line1Radius * sliceYBase + center.y

                    val backgroundColor = listaCorFundo.get(j)
                    val backgroundPaint = Paint().apply {
                        color = backgroundColor
                        style = Paint.Style.FILL
                    }
                    val textPaint = Paint().apply {
                        color = dataSet.getColor(j)
                        textSize = 30f
                        textAlign = Paint.Align.CENTER
                    }
                    val text =  if(listagraficoMaca.isEmpty()) "" else listagraficoMaca[j].pedidoRealizadosOriginal.toString()

                    val textBounds = Rect()
                    textPaint.getTextBounds(text, 0, text.length, textBounds)

                    // Calcula o retângulo para o fundo arredondado, posicionado fora do círculo
                    val backgroundRect = RectF(
                        px - textBounds.width() / 2 - 20,
                        py - textBounds.height() / 2 - 20,
                        px + textBounds.width() / 2 + 20,
                        py + textBounds.height() / 2 + 20
                    )
                    c.drawRoundRect(backgroundRect, 10f, 10f, backgroundPaint) // Desenha o fundo com bordas arredondadas

                    // Desenha o texto da porcentagem fora do círculo
                    c.drawText(text, px, py + textBounds.height() / 2, textPaint)
                }

                xIndex++
            }
        }
        MPPointF.recycleInstance(center)
        c.restore()
    }

}