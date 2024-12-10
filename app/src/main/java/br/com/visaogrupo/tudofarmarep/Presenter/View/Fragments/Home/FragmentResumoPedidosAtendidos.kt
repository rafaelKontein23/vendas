package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import ResumoMes
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaVisualizacaoDeGanhos
import br.com.visaogrupo.tudofarmarep.R


class FragmentResumoPedidosAtendidos : Fragment(), AtualizaVisualizacaoDeGanhos {

    private lateinit var cnpjsAtendidos: TextView
    private lateinit var pedidosRealizados: TextView
    var listaResumoPedidosAtendidos: ArrayList<ResumoMes> = ArrayList()

    companion object {
        private const val ARG_LISTA_RESUMO = "ARG_LISTA_RESUMO"

        fun newInstance(listaResumoPedidosAtendidos: ArrayList<ResumoMes>): FragmentResumoPedidosAtendidos {
            return FragmentResumoPedidosAtendidos().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_LISTA_RESUMO, listaResumoPedidosAtendidos)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupera os argumentos enviados pelo método newInstance
        arguments?.let {
            listaResumoPedidosAtendidos = it.getParcelableArrayList(ARG_LISTA_RESUMO) ?: ArrayList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resumo_pedidos_atendidos, container, false)
        cnpjsAtendidos = view.findViewById(R.id.cnpjsAtendidos)
        pedidosRealizados = view.findViewById(R.id.pedidosRealizados)
        return view
    }

    override fun atualizaVisualizacaoDeGanhos(verGanhos: Boolean) {
        if (::cnpjsAtendidos.isInitialized) {
            if (verGanhos) {
                if (listaResumoPedidosAtendidos.isEmpty()) {
                    cnpjsAtendidos.text = "0"
                    pedidosRealizados.text = "0"
                } else {
                    cnpjsAtendidos.text = listaResumoPedidosAtendidos[0].QtdeCNPJ.toString()
                    pedidosRealizados.text = listaResumoPedidosAtendidos[0].PedidosRealizados.toString()
                }
            } else {
                cnpjsAtendidos.text = "-"
                pedidosRealizados.text = "-"
            }
        }

    }
}
