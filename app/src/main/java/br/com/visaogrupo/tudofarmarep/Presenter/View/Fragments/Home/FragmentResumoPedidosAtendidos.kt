package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaVisualizacaoDeGanhos
import br.com.visaogrupo.tudofarmarep.Objetos.ResumoMes
import br.com.visaogrupo.tudofarmarep.R


class FragmentResumoPedidosAtendidos (listaResumoPedidosAtendidos: ArrayList<ResumoMes>): Fragment(), AtualizaVisualizacaoDeGanhos {
     private lateinit var cnpjsAtendidos:TextView
     private lateinit var pedidosRealizados:TextView
     var listaResumoPedidosAtendidos = listaResumoPedidosAtendidos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_resumo_pedidos_atendidos, container, false)
        cnpjsAtendidos = view.findViewById(R.id.cnpjsAtendidos)
        pedidosRealizados = view.findViewById(R.id.pedidosRealizados)


        return view
    }

    override fun atualizaVisualizacaoDeGanhos(verGanhos: Boolean) {

            if (verGanhos){
                if ( listaResumoPedidosAtendidos.isEmpty()){
                    cnpjsAtendidos.text = "0"
                    pedidosRealizados.text = "0"
                }else{
                    cnpjsAtendidos.text = listaResumoPedidosAtendidos[0].QtdeCNPJ.toString()
                    pedidosRealizados.text = listaResumoPedidosAtendidos[0].PedidosRealizados.toString()


                }

            }else{
                cnpjsAtendidos.text = "-"
                pedidosRealizados.text = "-"
            }

    }


}