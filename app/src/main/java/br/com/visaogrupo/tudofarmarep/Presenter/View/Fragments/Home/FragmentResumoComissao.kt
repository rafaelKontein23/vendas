package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import ResumoMes
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaVisualizacaoDeGanhos
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.R

class FragmentResumoComissao : Fragment(), AtualizaVisualizacaoDeGanhos {

    private var comissaoDisponivel: TextView? = null
    private var taxaFaturamento: TextView? = null
     var listaResumoPedidosAtendidos: ArrayList<ResumoMes> = arrayListOf()

    companion object {
        private const val ARG_LISTA_RESUMO = "lista_resumo"

        fun newInstance(listaResumo: ArrayList<ResumoMes>): FragmentResumoComissao {
            return FragmentResumoComissao().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_LISTA_RESUMO, listaResumo)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listaResumoPedidosAtendidos = it.getParcelableArrayList(ARG_LISTA_RESUMO) ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_resumo_comissao, container, false)
        comissaoDisponivel = view.findViewById(R.id.comissaoDisponivel)
        taxaFaturamento = view.findViewById(R.id.taxaFaturamento)
        atualizaValores(FragmentHome.visualizarRsulmo)
        return view
    }

    override fun atualizaVisualizacaoDeGanhos(verGanhos: Boolean) {
        if (comissaoDisponivel != null) {
            atualizaValores(verGanhos)
        }
    }

    private fun atualizaValores(verGanhos: Boolean) {
            if (verGanhos) {
                if (listaResumoPedidosAtendidos.isEmpty()) {
                    comissaoDisponivel?.text = "R$ 0,00"
                    taxaFaturamento?.text = "0 %"
                } else {
                    comissaoDisponivel?.text = FormatarTexto().formatarParaMoeda(listaResumoPedidosAtendidos[0].ComissaoDisponivel)
                    taxaFaturamento?.text = "${listaResumoPedidosAtendidos[0].TaxaFaturamento}%"
                }
            } else {
                comissaoDisponivel?.text = "R$-"
                taxaFaturamento?.text = "-%"
            }
        }
}
