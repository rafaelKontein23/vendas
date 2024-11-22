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
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Objetos.ResumoMes
import br.com.visaogrupo.tudofarmarep.R
import java.text.NumberFormat
import java.util.Locale

class FragmentResumoComissao(listaResumoPedidosAtendidos: ArrayList<ResumoMes>) : Fragment(), AtualizaVisualizacaoDeGanhos {

    var comissaoDisponivel : TextView? = null
    var taxaFaturamento: TextView? = null
    var listaResumoPedidosAtendidos = listaResumoPedidosAtendidos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resumo_comissao, container, false)
        comissaoDisponivel = view.findViewById(R.id.comissaoDisponivel)
        taxaFaturamento = view.findViewById(R.id.taxaFaturamento)
        atulizaValores(FragmentHome.visualizarRsulmo)

        return view
    }

    override fun atualizaVisualizacaoDeGanhos(verGanhos: Boolean) {
        if(comissaoDisponivel != null ){
           atulizaValores(verGanhos)
        }

    }
    fun atulizaValores(verGanhos: Boolean){
        if (verGanhos ){
            if ( listaResumoPedidosAtendidos.size == 0){
                comissaoDisponivel!!.text = "R$ 0,00"
                taxaFaturamento!!.text = "0 %"
            }else{
                comissaoDisponivel!!.text = FormatarTexto().formatarParaMoeda( listaResumoPedidosAtendidos[0].ComissaoDisponivel)
                taxaFaturamento!!.text = "${listaResumoPedidosAtendidos[0].TaxaFaturamento}%"


            }

        }else{
            comissaoDisponivel!!.text = "R$-"
            taxaFaturamento!!.text = "-%"
        }
    }



}