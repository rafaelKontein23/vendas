package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaMesResumo
import br.com.visaogrupo.tudofarmarep.Carga.ultis.RecuperasDatas
import br.com.visaogrupo.tudofarmarep.Controlers.ControlerFragmentGraficos
import br.com.visaogrupo.tudofarmarep.Objetos.GraficoMarca
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogSeledorData
import br.com.visaogrupo.tudofarmarep.R
import com.github.mikephil.charting.charts.PieChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FragmentGraficosHome : Fragment(), AtualizaMesResumo {

    private lateinit var pieChart: PieChart
    private lateinit var graficoTextPedidos: TextView

     var listaGraficos: ArrayList<GraficoMarca> = arrayListOf()

    companion object {
        private const val ARG_LISTA_GRAFICOS = "lista_graficos"

        // Método para instanciar o fragmento com argumentos
        fun newInstance(listaGraficos: ArrayList<GraficoMarca>): FragmentGraficosHome {
            return FragmentGraficosHome().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_LISTA_GRAFICOS, listaGraficos)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera os argumentos passados
        arguments?.let {
            listaGraficos = it.getParcelableArrayList(ARG_LISTA_GRAFICOS) ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graficos_home, container, false)
        pieChart = view.findViewById(R.id.pieChart)
        graficoTextPedidos = view.findViewById(R.id.graficoTextPedidos)

        // Configura o texto inicial e o clique no seletor de datas
        graficoTextPedidos.text = "Pedidos de ${RecuperasDatas.recuperaMesAtual()} de ${RecuperasDatas.recuperaAnoAtual()}"
        graficoTextPedidos.setOnClickListener {
            val dialogSeledorData = DialogSeledorData()
            dialogSeledorData.SeletorData(requireContext(), this)
        }

        // Controlador para carregar o gráfico inicial
        val controlerFragmentGraficos = ControlerFragmentGraficos()
        controlerFragmentGraficos.carregaGrafico(pieChart, listaGraficos)

        return view
    }

    override fun atualizaMesResumo(data: String, mesExtenso: String, ano: String) {
        val controlerFragmentGraficos = ControlerFragmentGraficos()
        CoroutineScope(Dispatchers.IO).launch {
            // Busca os dados para o gráfico
            val listaResumoMesGrafico = controlerFragmentGraficos.buscaResumoMesGrafico(data, requireContext())
            MainScope().launch {
                // Atualiza o gráfico na UI thread
                controlerFragmentGraficos.carregaGrafico(pieChart, listaResumoMesGrafico)
                graficoTextPedidos.text = "Pedidos de $mesExtenso de $ano"
            }
        }
    }
}
