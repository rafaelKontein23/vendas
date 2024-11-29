package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Lojas

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskLinksProdutoDetalhe
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosItens
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProdutoDetalheDialogFragment (val  produto: Produtos,val atualizaValores: AtualizaInfosItens,val loja:Lojas,val carrinho:Boolean = false,val razaoSocial: String): BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_produto_detalhe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        val fecharDialog = dialog?.findViewById<ImageView>(R.id.fecharDialog)
        val imgProduto = dialog?.findViewById<ImageView>(R.id.imgProduto)
        val nomeProdutos = dialog?.findViewById<TextView>(R.id.nomeProdutos)
        val  barra = dialog?.findViewById<TextView>(R.id.barra)
        val valorDe = dialog?.findViewById<TextView>(R.id.valorDe)
        val valorPor = dialog?.findViewById<TextView>(R.id.valorPor)
        val valorDesconto = dialog?.findViewById<TextView>(R.id.valorDesconto)
        val valorRealComissao = dialog?.findViewById<TextView>(R.id.valorRealComissao)
        val valorPorcentagem = dialog?.findViewById<TextView>(R.id.valorPorcentagem)
        val valorComiisao = dialog?.findViewById<TextView>(R.id.valorComiisao)
        val totalValor = dialog?.findViewById<TextView>(R.id.totalValor)
        val botaoMenos = dialog?.findViewById<TextView>(R.id.botaoMenos)
        val botaoMais = dialog?.findViewById<TextView>(R.id.botaoMais)
        val inputQuantidade = dialog?.findViewById<EditText>(R.id.inputQuantidade)
        val visualizarComissao = dialog?.findViewById<ImageView>(R.id.visualizarComissao)
        val continuar = dialog?.findViewById<TextView>(R.id.continuar)
        val containViewLoiu = dialog?.findViewById<ConstraintLayout>(R.id.containViewLoiu)
        val infosComiisao = dialog?.findViewById<ConstraintLayout>(R.id.InfosComiisao)
        val tituloComiisao = dialog?.findViewById<TextView>(R.id.tituloComiisao)
        val tituloFicha =dialog?. findViewById<TextView>(R.id.tituloFicha)
        val tituloSite = dialog?.findViewById<TextView>(R.id.tituloSite)
        val tituloMidias = dialog?.findViewById<TextView>(R.id.tituloMidias)
        val tituloBulas =dialog?. findViewById<TextView>(R.id.tituloBulas)
        val constarinLinks = dialog?.findViewById<ConstraintLayout>(R.id.constarinLinks)
        val siteIcon = dialog?.findViewById<ImageView>(R.id.siteIcon)
        val mediaIcon =dialog?. findViewById<ImageView>(R.id.mediaIcon)
        val bulasIcon = dialog?.findViewById<ImageView>(R.id.bulasIcon)
        val fichaIcon = dialog?.findViewById<ImageView>(R.id.fichaIcon)
        val valorPorSemDesconto = dialog?.findViewById<TextView>(R.id.valorPorSemDesconto)
        val modalValores = dialog?.findViewById<ConstraintLayout>(R.id.modalValores)
        val stTextUnitario =  dialog?.findViewById<TextView>(R.id.stTextUnitario)
        val stTextTotal =  dialog?.findViewById<TextView>(R.id.stTextTotal)




        val placeholder = R.drawable.padrao
        val urlImage = "https://"+ produto.Imagem
        if(produto.quantidadeAdicionada > 0){
            inputQuantidade?.setText(produto.quantidadeAdicionada.toString())
        }
        CoroutineScope(Dispatchers.Main).launch {
            if (isAdded){
                Glide.with(requireContext())
                    .load(urlImage).apply(RequestOptions().placeholder(placeholder)).into(imgProduto!!)
            }

        }
        containViewLoiu?.setOnClickListener {
            if (infosComiisao!!.isVisible){
                infosComiisao.isVisible = false
                tituloComiisao!!.text = "Loiu"
                visualizarComissao!!.setImageResource(R.drawable.sem_visu)
            }else{
                infosComiisao.isVisible = true
                tituloComiisao!!.text = "Loiu - Comissão"
                visualizarComissao!!.setImageResource(R.drawable.visualiza)

            }
        }

        fecharDialog?.setOnClickListener {
            if (carrinho ){
                atualizaValores.atualizaItensCarrinho()
            }else{
                atualizaValores.atualizaTopo(produto.lojaID,produto,true)

            }
            dialog?.dismiss()
        }
        inputQuantidade!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(inputQuantidade.isFocused){


                    var quantidade = s.toString()

                    if (quantidade.isEmpty() || quantidade.isBlank()) {
                        quantidade = "0"
                    }

                    quantidade = quantidade.replace(".", "")

                    val (quantidadeItem, valorTotal, comissao) = somarProdutos(quantidade.toInt(), produto, true)
                    CoroutineScope(Dispatchers.IO).launch {
                        if (isAdded) {
                            produto.quantidadeAdicionada = quantidade.toInt()
                            produto.valorProdutoTotal = valorTotal
                            inserirNoCarrinho(requireContext(), produto, loja, razaoSocial)

                        }
                        val valorTotalLoja = confereValorTotal(produto.cnpj, loja, requireContext())


                        if(valorTotalLoja > loja.ValorMaximo){
                            produto.quantidadeAdicionada =0
                            produto.valorProdutoTotal = 0.0
                            inserirNoCarrinho(requireContext(), produto, loja, razaoSocial)
                            MainScope().launch {
                                inputQuantidade.isEnabled = false
                                inputQuantidade.setText("")
                                inputQuantidade.setHint("0")
                                totalValor!!.text = "R$ 0,00"
                                stTextTotal!!.text = "+ST: R$ 0,00"
                                Alertas.alertaErro(requireContext(),
                                    "O valor máximo de compra para esta loja é de  ${FormatarTexto().formatarParaMoeda(loja.ValorMaximo)}",
                                    "Ops!" ){
                                    inputQuantidade.isEnabled = true
                                }

                            }

                        }else{
                            val formataTexto = FormatarTexto()
                            val valorTotalFormatado = formataTexto.formatarParaMoeda(valorTotal)
                            val valorComissaoFormatado = formataTexto.formatarParaMoeda(comissao)


                            MainScope().launch {
                                totalValor!!.text = valorTotalFormatado
                                valorComiisao!!.text = valorComissaoFormatado
                                atualizavalores(valorPorSemDesconto!!,
                                    modalValores!!,
                                    produto,
                                    valorPor!!,
                                    valorDe!!,
                                    valorDesconto!!,
                                    valorPorcentagem!!,
                                    valorRealComissao!!, stTextUnitario!!, stTextTotal!!)
                            }

                        }
                    }
                }


            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        continuar!!.setOnClickListener {
            dialog?.dismiss()
            CoroutineScope(Dispatchers.IO).launch {

                if (isAdded) {
                    inserirNoCarrinho(requireContext(), produto, loja, razaoSocial)
                }
                MainScope().launch {
                    if (carrinho ){
                        atualizaValores.atualizaItensCarrinho()
                    }else{
                        atualizaValores.atualizaTopo(produto.lojaID,produto,true)

                    }
                }
            }


        }

        botaoMais!!.setOnClickListener {
            var valorInput = inputQuantidade.text.toString()
            if(valorInput.isEmpty()){
                valorInput = "0"
            }
            val valorInputInt = valorInput.toInt().toString()
            val (quantidade, valorTotal, comissao) = somarProdutos(valorInputInt.toInt(), produto)
            if (isAdded) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (isAdded){
                        produto.valorProdutoTotal  = valorTotal
                        produto.quantidadeAdicionada = quantidade
                        inserirNoCarrinho(requireContext(), produto, loja, razaoSocial)

                    }
                    val valorTotalLoja = confereValorTotal(produto.cnpj, loja, requireContext())

                    if(valorTotalLoja > loja.ValorMaximo){
                        produto.quantidadeAdicionada =0
                        produto.valorProdutoTotal = 0.0
                        inserirNoCarrinho(requireContext(), produto, loja, razaoSocial)
                        MainScope().launch {
                            Alertas.alertaErro(requireContext(),
                                "O valor máximo de compra para esta loja é de  ${FormatarTexto().formatarParaMoeda(loja.ValorMaximo)}",
                                "Ops!" ){

                            }
                            totalValor!!.text = "R$ 0,00"
                            stTextTotal!!.text = "+ST: R$ 0,00"
                        }

                    }else{
                        MainScope().launch {
                            val formatarTexto = FormatarTexto()
                            val valorTotalFormatado = formatarTexto.formatarParaMoeda(valorTotal)
                            val valorComissaoFormatado = formatarTexto.formatarParaMoeda(comissao)
                            totalValor!!.setText(valorTotalFormatado)
                            inputQuantidade.setText(quantidade.toString())
                            valorComiisao!!.text = "${valorComissaoFormatado}"
                            atualizavalores(valorPorSemDesconto!!,
                                modalValores!!,
                                produto,
                                valorPor!!,
                                valorDe!!,
                                valorDesconto!!,
                                valorPorcentagem!!,
                                valorRealComissao!!, stTextUnitario!!, stTextTotal!!)
                        }

                    }
                }
            }
        }

        botaoMenos!!.setOnClickListener {
            var valorInput = inputQuantidade.text.toString()
            if(valorInput.isEmpty()){
                valorInput = "0"
            }

            if(valorInput.toInt() != 0){

                val (quantidade, valorTotal, comissao) = subTrairProdutos(valorInput.toInt(), produto)
                CoroutineScope(Dispatchers.IO).launch {
                    if (isAdded) {
                        produto.valorProdutoTotal = valorTotal
                        produto.quantidadeAdicionada = quantidade

                        inserirNoCarrinho(requireContext(), produto, loja, razaoSocial)
                    }
                    MainScope().launch {
                        val formatarTexto = FormatarTexto()
                        val valorTotalFormatado = formatarTexto.formatarParaMoeda(valorTotal)
                        val valorComissaoFormatado = formatarTexto.formatarParaMoeda(comissao)
                        totalValor!!.setText(valorTotalFormatado)
                        inputQuantidade.setText(quantidade.toString())
                        valorComiisao!!.text = "${valorComissaoFormatado}"
                    }
                }
            }else{
                totalValor!!.setText("R$ 0,00")
                valorComiisao!!.setText("R$ 0,00")
                inputQuantidade.setText("0")
                produto.quantidadeAdicionada = 0
                produto.idProgressiva = 0
                CoroutineScope(Dispatchers.IO).launch {
                    if (isAdded) {
                        inserirNoCarrinho(requireContext(), produto, loja, razaoSocial)
                    }
                }
            }
            atualizavalores(valorPorSemDesconto!!,
                modalValores!!,
                produto,
                valorPor!!,
                valorDe!!,
                valorDesconto!!,
                valorPorcentagem!!,
                valorRealComissao!!, stTextUnitario!!, stTextTotal!!)

        }
        nomeProdutos!!.text = produto.Nome
        barra!!.text = produto.Barra


        atualizavalores(valorPorSemDesconto!!,
            modalValores!!,
            produto,
            valorPor!!,
            valorDe!!,
            valorDesconto!!,
            valorPorcentagem!!,
            valorRealComissao!!, stTextUnitario!!, stTextTotal!!)

        if(produto.quantidadeAdicionada > 0){
            inputQuantidade.setText(produto.quantidadeAdicionada.toString())
            val formatarTexto = FormatarTexto()
            val (quantidade, valorTotal, comissao) = somarProdutos(produto.quantidadeAdicionada, produto, isOuvinte = true)
            val valorTotalFormatado = formatarTexto.formatarParaMoeda(valorTotal)
            val valorComissaoFormatado = formatarTexto.formatarParaMoeda(comissao)
            valorComiisao!!.text = valorComissaoFormatado

            totalValor!!.setText(valorTotalFormatado)


        }
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = true
        CoroutineScope(Dispatchers.IO).launch {
            val taskLinksProdutos = TaskLinksProdutoDetalhe()
            val linkProduto =  taskLinksProdutos.taskLinkProdutos(produto.Barra,loja.marcaID)
            if (linkProduto == null){
                MainScope().launch {
                    if(isAdded){
                        constarinLinks?.isVisible = false

                    }

                }
            }else{

                MainScope().launch {
                    if(isAdded){
                        constarinLinks?.isVisible = true

                        if(linkProduto?.Bula == "null"){
                            bulasIcon?.isVisible = false
                            tituloBulas?.isVisible = false
                        }
                        if(linkProduto?.Ficha == "null"){
                            fichaIcon?.isVisible = false
                            tituloFicha?.isVisible = false
                        }
                        if(linkProduto?.Midia == "null"){
                            mediaIcon?.isVisible = false
                            tituloMidias?.isVisible = false
                        }
                        if(linkProduto?.Site == "null"){
                            siteIcon?.isVisible = false
                            tituloSite?.isVisible = false
                        }
                        clickLink(tituloBulas!!, linkProduto.Bula.toString(), requireContext())
                        clickLink(tituloFicha!!, linkProduto.Ficha.toString(), requireContext())
                        clickLink(tituloMidias!!, linkProduto.Midia.toString(), requireContext())
                        clickLink(tituloSite!!, linkProduto.Site.toString(), requireContext())
                        clickLinkImge(mediaIcon!!, linkProduto.Midia.toString(), requireContext())
                        clickLinkImge(fichaIcon!!, linkProduto.Ficha.toString(), requireContext())
                        clickLinkImge(bulasIcon!!, linkProduto.Bula.toString(), requireContext())
                        clickLinkImge(siteIcon!!, linkProduto.Site.toString(), requireContext())
                    }

                }
            }
        }
    }
    fun confereValorTotal(cnpj: String, loja: Lojas, context: Context):Double{
        val daoHelper = DAOHelper(context).writableDatabase
        val daoTBCarrinho = DAOCarrinho()
        val (valorTotal, valorTotalSt) = daoTBCarrinho.buscaValorTotal(daoHelper,loja,cnpj)
        return valorTotal
    }
    fun somarProdutos(valorCampo:Int, produto: Produtos, isOuvinte:Boolean = false):Triple<Int, Double,Double>{

        var somaQuantidade = valorCampo

        if (!isOuvinte){
            somaQuantidade += 1
        }

        for(progressiva in produto.listaProgressiva ){
            if(somaQuantidade == 0 ){
                produto.progressiva = progressiva
                produto.idProgressiva = progressiva.id
                break
            }
            if(somaQuantidade >= progressiva.quantidadePedido){
                produto.progressiva = progressiva
                produto.idProgressiva = progressiva.id

            }
        }
        val valorTotal = produto.progressiva?.valorUnitarioDesconto!! * somaQuantidade


        var resultadoComissao = (produto.progressiva?.valorUnitarioDesconto!! * produto.progressiva?.comissao!!) / 100.0
        resultadoComissao = (resultadoComissao * 100 / 100) * somaQuantidade
        return Triple(somaQuantidade, valorTotal, resultadoComissao)

    }
    fun subTrairProdutos(valorCampo:Int, produto: Produtos):Triple<Int, Double, Double>{
        val subQuantidade = valorCampo - 1
        for(progressiva in produto.listaProgressiva ){
            if(subQuantidade >= progressiva.quantidadePedido){
                produto.progressiva = progressiva
                produto.idProgressiva = progressiva.id
            }
        }
        val valorTotal = produto.progressiva?.valorUnitarioDesconto!! * subQuantidade

        var resultadoComissao = (produto.progressiva?.valorUnitarioDesconto!! * produto.progressiva?.comissao!!) / 100.0
        resultadoComissao =  (resultadoComissao * 100 / 100)  * subQuantidade

        return Triple(subQuantidade, valorTotal, resultadoComissao)
    }
    suspend fun inserirNoCarrinho(context: Context, produto: Produtos, loja: Lojas, razaoSocial:String ){
            val daoHelper = DAOHelper(context).writableDatabase
            val daoTBCarrinho = DAOCarrinho()

            daoTBCarrinho.buscarItens(daoHelper,produto, loja, razaoSocial)


    }
    fun atualizavalores(valorPorSemDesconto:TextView,
                        modalValores:ConstraintLayout,
                        produto: Produtos,
                        valorPor:TextView,
                        valorDe:TextView,
                        valorDesconto:TextView,
                        valorPorcentagem:TextView,
                        valorRealComissao:TextView,
                        stTextUnitario:TextView,
                        stTextTotal:TextView){
        val progressiva = if (produto.progressiva == null) produto.listaProgressiva[0] else produto.progressiva
        if (progressiva?.stUnitario!! > 0.0){
            val valorTotalSt = if(produto.quantidadeAdicionada >0 ) progressiva.stUnitario * produto.quantidadeAdicionada else progressiva.stUnitario
            stTextUnitario.text = "+ST: ${FormatarTexto().formatarParaMoeda(progressiva?.stUnitario!!)}"
            stTextTotal.text = "+ ST: ${FormatarTexto().formatarParaMoeda(valorTotalSt)}"
            stTextUnitario.isVisible = true
            stTextTotal.isVisible = true
        }else{
            stTextUnitario.isVisible = false
            stTextTotal.isVisible = false
        }
        val formatarTexto = FormatarTexto()
        if (progressiva!!.desconto ==  0.0){
            val valorUnitarioPorFormatado = formatarTexto.formatarParaMoeda(progressiva.valorUnitarioDesconto)
            valorPorSemDesconto!!.isVisible = true
            valorPorSemDesconto.setText(valorUnitarioPorFormatado)
            modalValores!!.isVisible = false
        }else{
            val valorUnitarioFormatado = formatarTexto.formatarParaMoeda(progressiva!!.valorUnitario)
            val valorUnitarioPorFormatado = formatarTexto.formatarParaMoeda(progressiva.valorUnitarioDesconto)


            valorDe!!.text = "${valorUnitarioFormatado}"
            valorPor!!.text = "${valorUnitarioPorFormatado}"
            valorDesconto!!.text = "${progressiva.desconto.toString().replace(".",",")}%"

        }
        val resultadoComissao = (progressiva.valorUnitarioDesconto * progressiva.comissao) / 100.0
        val resultadoComissaoFormatado = formatarTexto.formatarParaMoeda(resultadoComissao)
        valorPorcentagem!!.text = "${progressiva.comissao.toString().replace(".",",")}%"
        valorRealComissao!!.text = "${resultadoComissaoFormatado}"
    }
}
fun clickLink(textView: TextView, link:String, context: Context){
    textView.setOnClickListener {
        var linkFormat = link
        if (!link.contains("https://")){
            linkFormat = "https://"+ link

        }
        if (linkFormat.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkFormat))
            if (intent.resolveActivity(context.packageManager) != null) {
                context. startActivity(intent)
            } else {
                Toast.makeText(context, "Nenhum aplicativo pode abrir essa URL", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "URL está vazia!", Toast.LENGTH_SHORT).show()
        }

    }
}
fun clickLinkImge(imge: ImageView, link:String, context: Context){
    imge.setOnClickListener {
        var linkFormat = link
        if (!link.contains("https://")){
            linkFormat = "https://"+ link

        }
        if (linkFormat.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkFormat))
            if (intent.resolveActivity(context.packageManager) != null) {
                context. startActivity(intent)
            } else {
                Toast.makeText(context, "Nenhum aplicativo pode abrir essa URL", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "URL está vazia!", Toast.LENGTH_SHORT).show()
        }


    }
}