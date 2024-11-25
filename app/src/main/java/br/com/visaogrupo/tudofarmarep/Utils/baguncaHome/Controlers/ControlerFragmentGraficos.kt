package br.com.visaogrupo.tudofarmarep.Controlers

import RoundedSlicesPieChartRenderer
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.preference.PreferenceManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.core.graphics.ColorUtils
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskGraficoHome
import br.com.visaogrupo.tudofarmarep.Objetos.GraficoMarca
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class ControlerFragmentGraficos {
    fun carregaGrafico(pieChart: PieChart, listaGraficos: ArrayList<GraficoMarca>){
        val entries = ArrayList<PieEntry>()
        val dataSet = PieDataSet(entries, "")
        val listaCorMarcas = mutableListOf<Int>()
        val listaCorOpacidade = mutableListOf<Int>()
        if (listaGraficos.isEmpty()){
            listaCorMarcas.add(Color.parseColor("#E2E8F0"))
            entries.add(PieEntry(450f, ""))
            listaCorOpacidade.add((Color.parseColor("#00000000")))
        }

        var totalPedidos = listaGraficos.sumOf { it -> it.pedidosRealizados }
        val minPorcentagem = 10f
        val ajusteMinimo = 12f

        val totalAjustado = listaGraficos.map { marca ->
            val porcentagem = (marca.pedidosRealizados.toFloat() / totalPedidos) * 100
            marca.pedidoRealizadosOriginal = marca.pedidosRealizados
            if (porcentagem < minPorcentagem) {

                val pedidosAjustados = (ajusteMinimo / 100) * totalPedidos
                marca.copy(pedidosRealizados = pedidosAjustados.toInt())
            } else {
                marca
            }
        }
        for (marca in totalAjustado) {
            val pedidos = marca.pedidosRealizados.toFloat()
            entries.add(PieEntry(pedidos, marca.marca))


            val corMarca = Color.parseColor(marca.corMarca)
            val corComOpacidade = ColorUtils.setAlphaComponent(corMarca, (0.20f * 255).toInt())
            listaCorMarcas.add(corMarca)
            listaCorOpacidade.add(corComOpacidade)
        }


        dataSet.colors = listaCorMarcas
        dataSet.setDrawValues(false)


        dataSet.valueTextSize = 0f

        val data = PieData(dataSet)
        pieChart.data = data
        if(listaGraficos.isEmpty()){
            pieChart.legend.isEnabled = false
        }else{
            pieChart.legend.isEnabled = true

        }
        val centerText = "$totalPedidos\nPedidos"

// Crie um SpannableString a partir do texto
        val spannableString = SpannableString(centerText)

// Aplique estilo ao número de pedidos (totalPedidos)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), 0, totalPedidos.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            RelativeSizeSpan(1.5f), 0, totalPedidos.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

// Aplique estilo à palavra "Pedidos"
        spannableString.setSpan(
            ForegroundColorSpan(0xFF64748B.toInt()), totalPedidos.toString().length + 1, centerText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.NORMAL), 0, totalPedidos.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            RelativeSizeSpan(1f), totalPedidos.toString().length + 1, centerText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        pieChart.centerText = spannableString
        pieChart.setCenterTextSize(20f)
        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 10f
        legend.textSize = 12f
        legend.textColor = Color.BLACK
        legend.xEntrySpace = 10f
        legend.yEntrySpace = 10f
        legend.stackSpace = 10f


        pieChart.setDrawHoleEnabled(true)
        pieChart.holeRadius = 90f
        pieChart.extraRightOffset = 5f
        pieChart.maxAngle = 900f
        pieChart.animateY(1000)
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.isRotationEnabled = false


        pieChart.setExtraOffsets(8f, 12f, 8f, 12f)

        pieChart.setRenderer(
            RoundedSlicesPieChartRenderer(
                pieChart,
                pieChart.animator,
                pieChart.viewPortHandler,
                listaCorOpacidade, listaGraficos
            )
        )
    }


    suspend fun  buscaResumoMesGrafico(datSelecionada:String,context:Context):ArrayList<GraficoMarca>{
        val listaResumoMesGrafico = ArrayList<GraficoMarca>()
        withContext(Dispatchers.IO){
            val tarefaBuscaMes = async {
                val taskResumoMes = TaskGraficoHome()
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val reprsentanteID = prefs.getInt("reprsentante_id", 0)

                val  listaResumoMesTaskGrafico = taskResumoMes.buscaGraficoHome(data = datSelecionada, representanteID = reprsentanteID)
                listaResumoMesGrafico.addAll(listaResumoMesTaskGrafico)
            }

            tarefaBuscaMes.await()

        }
        return listaResumoMesGrafico
    }
}