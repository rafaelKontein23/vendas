package br.com.visaogrupo.tudofarmarep.Views.dialogs

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterFiltroTitulo
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskFiltroProdutos
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaFiltroAtribudos
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaTotalProdutos
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.DAO.DAOProdutos
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DialogFiltros:AtualizaTotalProdutos {
    lateinit var contatorDeProdutos:TextView
    lateinit var  limparFilros :TextView
    var atualizaCount = true
    var totalProdutoInicial = 0
    val listaAtriBudos = ArrayList<Int>()


    fun dialogFiltro(context: Context, loja:Lojas, atualizaFiltroAtribudos: AtualizaFiltroAtribudos,
                     TotalDeProdutosAtuais:Int, operadorSelcionado :Int){
        val dialogFiltroProdutos = Dialog(context)
        dialogFiltroProdutos.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFiltroProdutos.setContentView(R.layout.dialog_filtros_produtos)
        CoroutineScope(Dispatchers.IO).launch {
            val daoHelper = DAOHelper(context).writableDatabase
            val daoProdutos = DAOProdutos()
            val listaBarras = daoProdutos.filtraProdudos(daoHelper,loja.listaAtrinutos, operadorSelcionado, true, loja.Loja_ID)
            totalProdutoInicial = listaBarras.size
        }


        dialogFiltroProdutos.show()
        dialogFiltroProdutos.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogFiltroProdutos.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogFiltroProdutos.window!!.attributes.windowAnimations = R.style.animacaoDialog
        dialogFiltroProdutos.window!!.setGravity(Gravity.BOTTOM)
        val fecharFiltro = dialogFiltroProdutos.findViewById<ImageView>(R.id.fecharFiltro)
        val recyPrincipalFiltro = dialogFiltroProdutos.findViewById<RecyclerView>(R.id.recyPrincipalFiltro)
        val filtrarBotao = dialogFiltroProdutos.findViewById<TextView>(R.id.FiltrarBotao)
        limparFilros = dialogFiltroProdutos.findViewById<TextView>(R.id.limparFilros)
        val nomeLoja = dialogFiltroProdutos.findViewById<TextView>(R.id.nomeLoja)
        val progressBarFiltroLoja = dialogFiltroProdutos.findViewById<ProgressBar>(R.id.progressBarFiltroLoja)
         contatorDeProdutos = dialogFiltroProdutos.findViewById<TextView>(R.id.contatorDeProdutos)
        nomeLoja.text = "Filtros -${loja.Nome}"
        fecharFiltro.setOnClickListener {
            dialogFiltroProdutos.dismiss()
        }

        limparFilros.isVisible = !loja.listaAtrinutos.isEmpty()
        limparFilros.setOnClickListener {
            loja.listaAtrinutos.clear()
            dialogFiltroProdutos.dismiss()
            
            atualizaFiltroAtribudos.atualizaFiltroAtribudos(loja, loja.Loja_ID)
        }

        filtrarBotao.setOnClickListener {
            dialogFiltroProdutos.dismiss()
            atualizaFiltroAtribudos.atualizaFiltroAtribudos(loja, loja.Loja_ID)
        }



        CoroutineScope(Dispatchers.IO).launch {
             progressBarFiltroLoja.isVisible = true
             val taskFiltroProdutos = TaskFiltroProdutos()
             val listaFiltro =taskFiltroProdutos.taskFiltroProdutos(loja.marcaID, lojaId = loja.Loja_ID)
             MainScope().launch {
                 val linearLayoutManager = LinearLayoutManager(context)
                 val adapterFiltroTitulo = AdapterFiltroTitulo(listaFiltro, operadorSelcionado,
                     this@DialogFiltros, loja)
                 progressBarFiltroLoja.isVisible = false
                 recyPrincipalFiltro.layoutManager = linearLayoutManager
                 recyPrincipalFiltro.adapter = adapterFiltroTitulo

             }
         }
    }

    override fun atualizaTotalProdutos(total: Int, loja: Lojas) {
        CoroutineScope(Dispatchers.Main).launch {

            if(loja.listaAtrinutos.size == 0 ){
                contatorDeProdutos.text = "${totalProdutoInicial} Produtos"

            }else if (loja.listaAtrinutos.size > 0 && total == 0){
                contatorDeProdutos.text = "0 Produtos"

            }else{
                contatorDeProdutos.text = "${total} Produtos"
            }
            limparFilros.isVisible = !loja.listaAtrinutos.isEmpty()
        }
    }
}