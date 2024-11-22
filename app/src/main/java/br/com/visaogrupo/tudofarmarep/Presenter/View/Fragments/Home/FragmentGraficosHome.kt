package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import RoundedSlicesPieChartRenderer
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterInfoGrafico
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaMesResumo
import br.com.visaogrupo.tudofarmarep.Carga.ultis.RecuperasDatas
import br.com.visaogrupo.tudofarmarep.Controlers.ControlerFragmentGraficos
import br.com.visaogrupo.tudofarmarep.Controlers.ControlerFragmentHome
import br.com.visaogrupo.tudofarmarep.Objetos.GraficoMarca
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogSeledorData
import br.com.visaogrupo.tudofarmarep.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class FragmentGraficosHome( listaGraficos: ArrayList<GraficoMarca>) : Fragment(), AtualizaMesResumo {
    var listaGraficos = listaGraficos
    private  lateinit var pieChart:PieChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var graficoTextPedidos:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graficos_home, container, false)
         pieChart = view.findViewById<PieChart>(R.id.pieChart)

         graficoTextPedidos = view.findViewById(R.id.graficoTextPedidos)

        graficoTextPedidos.setOnClickListener {
            val dialogSeledorData = DialogSeledorData()
            dialogSeledorData.SeletorData(requireContext(), this)
        }
       graficoTextPedidos.text = "Pedidos de ${RecuperasDatas.recuperaMesAtual()} de ${RecuperasDatas.recuperaAnoAtual()}"
        val controlerFragmentGraficos = ControlerFragmentGraficos()
        controlerFragmentGraficos.carregaGrafico(pieChart, listaGraficos)

        return view
    }

    override fun atualizaMesResumo(data: String, mesExtenso: String, ano: String) {
        val controlerFragmentGraficos = ControlerFragmentGraficos()
        CoroutineScope(Dispatchers.IO).launch {

            val listaResumoMesGrafico = controlerFragmentGraficos.buscaResumoMesGrafico(data, requireContext())
            MainScope().launch {
                controlerFragmentGraficos.carregaGrafico(pieChart, listaResumoMesGrafico)
                graficoTextPedidos.text = "Pedidos de $mesExtenso de $ano"
            }
        }
    }


}