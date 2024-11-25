package br.com.visaogrupo.tudofarmarep.Adapter

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.com.visaogrupo.tudofarmarep.ActLojaPadrao
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskAdicionarCarteira
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaFiltroAtribudos
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfoTopo
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosItens
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaListaFiltro
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProdutosOperadores
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.InterfaceRemoverProtudosView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.InterfaceScrolaLista
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.TrocaInfoTopo
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.DAO.DAOProgressiva
import br.com.visaogrupo.tudofarmarep.Objetos.Banners
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.Objetos.Empresa
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Objetos.Progressiva
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Loja.ActLojaOnlineWebView
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Views.Activitys.ActCarrinho
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogFiltros
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogFiltrosLojas
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogOpls
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogProdutoDetalhe
import com.bumptech.glide.Glide
import com.google.android.material.transition.Hold
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.util.Timer
import java.util.TimerTask

private var currentPage = 0
var tempo: Timer? = null
var infosAbertas = false
var primeiroProduto = false
var valorTotalMaximo = 0.0

class AdapterRecyclerLojas(

    private val context: Context,
    private val  interfaceRemoverProtudosView: InterfaceRemoverProtudosView,
    private val trocaInfoTopo: TrocaInfoTopo,
    private val atualizaProdutosOperadores: AtualizaProdutosOperadores,
    private val atualizaFiltroAtribudos: AtualizaFiltroAtribudos,
    private val atualizaInfosItens: AtualizaInfosItens,
    private  val interfaceScrolaLista: InterfaceScrolaLista,
    private val empresa: Empresa,
     listaBanners:ArrayList<Banners>,
    private val atualizaFiltroLista: AtualizaListaFiltro,
    private val lojasPrimeiraFiltros: ArrayList<Pair<Int, Any>>,
    val endereco:String,
    val contextAct: Activity,
    val atualizaInfoTopo: AtualizaInfoTopo,
    val listaCarrinho: ArrayList<CarrinhoItemCotacao>



) : RecyclerView.Adapter<AdapterRecyclerLojas.ViewHolder>() {
    var listaLojas: ArrayList<Pair<Int, Any>> = ArrayList()
    var filtroBusca = false
    val  listaBanners:ArrayList<Banners> = listaBanners
    var nomeFiltros = "Todas Lojas"
    var buscaAberta = false
    var topoFixoVisivel = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == 0) {
            LayoutInflater.from(parent.context).inflate(R.layout.celula_loja, parent, false)
        }else if(viewType == 1) {
            LayoutInflater.from(parent.context).inflate(R.layout.celula_produtos_aaz, parent, false)
        }else{
            LayoutInflater.from(parent.context).inflate(R.layout.celula_banner_filtro_loja, parent, false)
        }
        return if (viewType == 0) {
            ViewHolder.LojaViewHolder(view)
        }else if(viewType == 1) {
            ViewHolder.ProdutosViewHolder(view)
        } else {
            ViewHolder.FiltroLoja(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaLojas[position].second
        when (holder) {
            is ViewHolder.LojaViewHolder -> {
                val loja = item as Lojas
                primeiroProduto = true
                holder.constrainFiltros.isVisible = loja.isFiltro
                valorTotalMaximo = loja.ValorMaximo


                if (loja.Nome.length >= 25){
                    val nomeLojaFormatado = loja.Nome.substring(0, 23) + "..."
                    holder.nomeLoja.text = nomeLojaFormatado
                }else{
                    holder.nomeLoja.text = loja.Nome
                }
                holder.constrainCarrinho.setOnClickListener {

                        val daoHelper = DAOHelper(holder.nomeLoja.context).writableDatabase
                        val daoCarrinho = DAOCarrinho()
                        val cnpj = if (empresa.cnpj.isEmpty()) "00000000000000" else empresa.cnpj
                        val isCarrinho = daoCarrinho.verificaCarrinho(daoHelper, loja.Loja_ID,cnpj, loja)
                        daoHelper.close()
                        if( loja.valorTotal > loja.ValorMaximo) {
                            Alertas.alertaErro(holder.nomeLoja.context,
                                "O valor máximo de compra para esta  loja é de  ${FormatarTexto().formatarParaMoeda(loja.ValorMaximo)}",
                                "Ops!" ){

                            }
                        }else if(!isCarrinho){
                            Toast.makeText(holder.nomeLoja.context, "Carrinho Vazio", Toast.LENGTH_SHORT).show()
                        }else{
                            val intent = Intent(holder.constrainFiltros.context, ActCarrinho::class.java)
                            val empresaJson = Gson().toJson(empresa)
                            val lojaJson = Gson().toJson(loja)
                            intent.putExtra("loja", lojaJson.toString())
                            intent.putExtra("empresa", empresaJson.toString())

                            (holder.nomeLoja.context as Activity).startActivityForResult(intent, 1)

                        }




                }
                if (loja.totalSt > 0){
                    holder.stTextTotal.isVisible = true
                    holder.stTextTotal.text = "+ ST: ${FormatarTexto().formatarParaMoeda(loja.totalSt)}"
                }else{
                    holder.stTextTotal.isVisible = false
                }
                Glide.with(context).load(URLs.urlImagensLoja+"/"+loja.imagem).into(holder.imgMarca)
                if (loja.nomeOperadorSelecionado.isEmpty()){
                    holder.nomeOperador.text = "Selecione"
                }else{
                    holder.nomeOperador.text = loja.nomeOperadorSelecionado.toLowerCase()
                }

                if(loja.valorTotal > 0.0 ){
                    val formataTExto = FormatarTexto()
                    val valorTotalFormatado = formataTExto.formatarParaMoeda(loja.valorTotal)
                    holder.totalValor.text = "${valorTotalFormatado}"
                    holder.totalValor.isVisible  = true
                    holder.tituloTortal.isVisible = true
                }else{
                    holder.totalValor.isVisible  = false
                    holder.tituloTortal.isVisible = false
                }
                if(!loja.urlLogin.isEmpty()){
                    holder.abrirProdutudos.rotation = -90f
                    holder.contrainsProdtudos.visibility = View.GONE

                }else{
                    if( loja.ProdutosAbertos){
                        holder.contrainsProdtudos.visibility = View.VISIBLE
                        holder.abrirProdutudos.rotation = 180f


                    }else{
                        holder.contrainsProdtudos.visibility = View.GONE
                        holder.abrirProdutudos.rotation = 0f

                    }
                }


                if (loja.listaAtrinutos.isEmpty() || !loja.ProdutosAbertos){
                    holder.filtroContainer.isVisible = false
                }else{
                    holder.quantidaeFiltro.text = "${loja.listaAtrinutos.size}"
                    holder.filtroContainer.isVisible = true
                }
                holder.constrainOpls.setOnClickListener {
                    if(buscaAberta){
                        Toast.makeText(holder.nomeLoja.context, "Não é possível trocar o operador com a busca aberta.", Toast.LENGTH_SHORT).show()
                    }else{
                        val DAOProgressiva = DAOProgressiva()
                        val listaOpls = DAOProgressiva.buscaopls(loja.Loja_ID, holder.nomeLoja.context)
                        if(listaOpls.size == 1){
                            Toast.makeText(holder.nomeLoja.context, "Há apenas um operador logistico", Toast.LENGTH_SHORT).show()
                        }else{
                            val dialogOpls = DialogOpls()
                            dialogOpls.dialogOperadors(holder.nomeLoja.context, loja, atualizaProdutosOperadores, listaOpls)
                        }
                    }

                }
                holder.constrainFiltros.setOnClickListener {
                    val dialogFiltros = DialogFiltros()
                    val listaFiltro = listaLojas.filter { it.second is Produtos  && (it.second as Produtos).lojaID == loja.Loja_ID  }

                    dialogFiltros.dialogFiltro(holder.nomeLoja.context, loja, atualizaFiltroAtribudos = atualizaFiltroAtribudos,listaFiltro.size, loja.operadorIDSelecionado)
                }
                holder.constraintLayout8.setOnClickListener {
                    if(!loja.urlLogin.isEmpty()){
                        val intent = Intent(holder.nomeLoja.context, ActLojaOnlineWebView::class.java)
                        intent.putExtra("URL_LOGIN", loja.urlLogin)
                        holder.nomeLoja.context.startActivity(intent)
                    }else{
                        if( loja.ProdutosAbertos){
                            holder.contrainsProdtudos.visibility = View.GONE
                            holder.abrirProdutudos.rotation = 0f
                            loja.ProdutosAbertos = false
                            listaLojas[position] = Pair(listaLojas[position].first, loja)
                            interfaceRemoverProtudosView.removeItens(loja.Loja_ID)
                        }else{
                            holder.contrainsProdtudos.visibility = View.VISIBLE
                            holder.abrirProdutudos.rotation = 180f
                            loja.ProdutosAbertos = true

                            interfaceRemoverProtudosView.addItensItens(loja, position)
                            interfaceScrolaLista.scrollToLoja(loja)

                        }
                    }


                }
            }
            is ViewHolder.ProdutosViewHolder -> {

                val produto = item as Produtos
                holder.bind(produto, atualizaInfosItens, listaLojas, atualizaInfoTopo, listaCarrinho, valorTotalMaximo, empresa)
                val isPrimeiroProduto = listaLojas.indexOfFirst {
                    it.second is Produtos && (it.second as Produtos).lojaID == produto.lojaID
                } == position

                if (isPrimeiroProduto) {
                    val params = holder.nomeProdutoAaZ.layoutParams as ViewGroup.MarginLayoutParams
                    params.topMargin = 100
                    holder.nomeProdutoAaZ.layoutParams = params
                } else {
                    val params = holder.nomeProdutoAaZ.layoutParams as ViewGroup.MarginLayoutParams
                    params.topMargin = 30
                    holder.nomeProdutoAaZ.layoutParams = params
                }

                if(holder.stTextTotal.isVisible){
                    primeiroProduto = false

                    val params = holder.view2.layoutParams as ViewGroup.MarginLayoutParams
                    params.topMargin = 60
                    holder.view2.layoutParams = params

                }else{
                    primeiroProduto = true
                    val params = holder.view2.layoutParams as ViewGroup.MarginLayoutParams
                    params.topMargin = 30
                    holder.view2.layoutParams = params

                }
                trocaInfoTopo.trocaInforTopoProduto(produto.lojaID)

                holder.contrainsProdtudos.setOnClickListener {
                    val dialogProdutoDetalhe = DialogProdutoDetalhe()
                    val loja = listaLojas.filter { it.second is Lojas && (it.second as Lojas).Loja_ID == produto.lojaID }[0].second as Lojas
                    dialogProdutoDetalhe.dialogPrudutoDetalhe(holder.nomeProdutoAaZ.context, produto,atualizaInfosItens, loja, razaoSocial =  empresa.razaoSocial)

                }

                holder.lixeira.setOnClickListener {
                    produto.valorProdutoTotal = 0.0
                    produto.quantidadeAdicionada = 0
                    holder.inputAdicionar.setText("")
                    holder.inputAdicionar.setHint("0")
                    holder.totalSomado.text = "R$ 0,00"
                    holder.stTextTotal.text = "+ ST: R$ 0,00"
                    holder.lixeira.isVisible = false
                    CoroutineScope(Dispatchers.IO).launch {
                        val loja = listaLojas.filter { it.second is Lojas && (it.second as Lojas).Loja_ID == produto.lojaID }[0].second as Lojas
                        inserirNoCarrinho(context, produto, loja, empresa)
                        atualizaInfosItens.atualizaTopo(produto.lojaID, produto)
                    }
                }


            }
            is ViewHolder.FiltroLoja -> {

                tempo?.cancel()
                tempo = null

                if (!listaBanners.isEmpty()) {
                    holder.carroselBaner.isVisible = true
                    holder.indicatorLayout.isVisible = false

                    // Verificar se o adapter já está setado
                    if (holder.carroselBaner.adapter == null) {
                        val adapterBanners = AdapterBanners(listaBanners)
                        holder.carroselBaner.adapter = adapterBanners
                    }

                    setupIndicator(context, holder)
                    setCurrentPageIndicator(position = currentPage, holder)

                    // Configurar o Timer para o banner
                    tempo = Timer()
                    tempo!!.schedule(object : TimerTask() {
                        override fun run() {
                            CoroutineScope(Dispatchers.Main).launch {
                                if (currentPage == listaBanners.size - 1) {
                                    currentPage = 0
                                } else {
                                    currentPage++
                                }
                                holder.carroselBaner.setCurrentItem(currentPage, true)
                            }
                        }
                    }, 5000, 5000)

                } else {
                    holder.carroselBaner.isVisible = false
                    holder.indicatorLayout.isVisible = false
                }
                holder.cnpjSelecionado.text = FormatarTexto().formatCNPJ(empresa.cnpj)
                holder.razaoSocial.text = empresa.razaoSocial
                holder.enderecoCNPJ.text = endereco
                holder.filtroLojas.text = "${nomeFiltros}"
                holder. enderecoCNPJ.setOnClickListener {
                    val url = "https://www.google.com/maps/search/?api=1&query=${endereco}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    holder.enderecoCNPJ.context.startActivity(intent)
                }
                holder.adicionarCarteira.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val adiconcarCarteiraCnpj = TaskAdicionarCarteira()
                        var statusCarteira = if(empresa.isCarteira) 0 else 1
                        val preferenciasUtils = PreferenciasUtils(context)
                        val  adicionar  = adiconcarCarteiraCnpj.adicionarCarteira(preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID), empresa.cnpj, statusCarteira)
                        if (adicionar){
                            empresa.isCarteira = !empresa.isCarteira
                            if(statusCarteira == 0){

                                MainScope().launch {
                                    holder.adicionarCarteira.setTextColor(ContextCompat.getColor(context, R.color.white))

                                    holder.imgCarteira.setImageResource(R.drawable.carteira)

                                    holder.adicionarCarteira.background = ContextCompat.getDrawable(context, R.drawable.bordas_100_stroke_1_blue_300)
                                }

                            }else{
                                MainScope().launch {
                                    holder.adicionarCarteira.setTextColor(ContextCompat.getColor(context, R.color.blue500))

                                    holder.imgCarteira.setImageResource(R.drawable.carteira_x)

                                    holder.adicionarCarteira.background = ContextCompat.getDrawable(context, R.drawable.bordas_100_stroke_1_blue500)
                                }

                            }
                        }else{
                            MainScope().launch {
                                Toast.makeText(context, "Erro ao adicionar a carteira", Toast.LENGTH_SHORT).show()

                            }
                        }

                    }
                }
                if(!empresa.isCarteira){
                    MainScope().launch {
                        holder.adicionarCarteira.setTextColor(ContextCompat.getColor(context, R.color.white))

                        holder.imgCarteira.setImageResource(R.drawable.carteira)

                        holder.adicionarCarteira.background = ContextCompat.getDrawable(context, R.drawable.bordas_100_stroke_1_blue_300)
                    }

                }else{
                    MainScope().launch {
                        holder.adicionarCarteira.setTextColor(ContextCompat.getColor(context, R.color.blue500))

                        holder.imgCarteira.setImageResource(R.drawable.carteira_x)

                        holder.adicionarCarteira.background = ContextCompat.getDrawable(context, R.drawable.bordas_100_stroke_1_blue500)
                    }

                }
                holder.cnpjSelecionado.setOnClickListener {
                    if(!infosAbertas){
                        holder.enderecoCNPJ.isVisible = true
                        infosAbertas = true
                    }else{
                        infosAbertas = false
                        holder. enderecoCNPJ.isVisible = false
                    }
                }
                holder.razaoSocial.setOnClickListener {
                    if(!infosAbertas){
                        holder.enderecoCNPJ.isVisible = true
                        infosAbertas = true
                    }else{
                        infosAbertas = false
                        holder. enderecoCNPJ.isVisible = false
                    }
                }

                holder. constrainFiltroLoja.setOnClickListener{
                    MainScope().launch {
                        val dialogFiltroLoja = DialogFiltrosLojas()
                        val listaAux = lojasPrimeiraFiltros.filter { it.second is Lojas }
                        for (i in listaAux){
                                (i.second as Lojas ).checada = false
                        }
                        dialogFiltroLoja.doFilter(contextAct, listaAux as ArrayList<Pair<Int, Any>>, atualizaFiltroLista)
                    }

                }

            }
        }
    }


    override fun getItemCount(): Int = listaLojas.size
    override fun getItemViewType(position: Int): Int {
        return listaLojas[position].first
    }
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class LojaViewHolder(itemView: View) : ViewHolder(itemView) {
            val nomeLoja: TextView = itemView.findViewById(R.id.nomeLoja)
            val imgMarca: ImageView = itemView.findViewById(R.id.imgMarca)
            val totalValor: TextView = itemView.findViewById(R.id.totalValor)
            val tituloTortal: TextView = itemView.findViewById(R.id.tituloTortal)
            val abrirProdutudos: ImageView = itemView.findViewById(R.id.abrirProdutudos)
            val constrainOpls: ConstraintLayout = itemView.findViewById(R.id.constrainOpls)
            val constrainFiltros: ConstraintLayout = itemView.findViewById(R.id.constrainFiltros)
            val stTextTotal = itemView.findViewById<TextView>(R.id.stTextTotal)
            val constrainCarrinho: ConstraintLayout = itemView.findViewById(R.id.constrainCarrinho)
            val contrainsProdtudos = itemView.findViewById<ConstraintLayout>(R.id.contrains)
            val constraintLayout8 = itemView.findViewById<ConstraintLayout>(R.id.constraintLayout8)
            val nomeOperador = itemView.findViewById<TextView>(R.id.nomeOperador)
            val filtroContainer = itemView.findViewById<ConstraintLayout>(R.id.filtroContainer)
            val quantidaeFiltro = itemView.findViewById<TextView>(R.id.quantidaeFiltro)
        }
        class FiltroLoja(itemView: View) : ViewHolder(itemView){
            val cnpjSelecionado = itemView.findViewById<TextView>(R.id.cnpjSelecionado)
            val razaoSocial = itemView.findViewById<TextView>(R.id.razaoSocial)
            val enderecoCNPJ = itemView.findViewById<TextView>(R.id.enderecoCNPJ)
            val constrainFiltroLoja = itemView.findViewById<ConstraintLayout>(R.id.constrainFiltroLoja)
            val indicatorLayout = itemView.findViewById<LinearLayout>(R.id.indicatorLayout)
            val carroselBaner = itemView.findViewById<ViewPager2>(R.id.CarroselBaner)
            val adicionarCarteira = itemView.findViewById<TextView>(R.id.adicionarCarteira)
            val lojasDisponivel = itemView.findViewById<TextView>(R.id.lojasDisponivel)
            val imageView20 = itemView.findViewById<ImageView>(R.id.imageView20)
            val filtroLojas = itemView.findViewById<TextView>(R.id.filtroLojas)
            val imgCarteira = itemView.findViewById<ImageView>(R.id.imgCarteira)

        }
        class ProdutosViewHolder(itemView: View) : ViewHolder(itemView) {
            val nomeProdutoAaZ = itemView.findViewById<TextView>(R.id.nomeProdutoAaZ)
            val valorPF = itemView.findViewById<TextView>(R.id.valorPF)
            val textDeconto = itemView.findViewById<TextView>(R.id.textDeconto)
            val valorComDesconto = itemView.findViewById<TextView>(R.id.valorComDesconto)
            val inputAdicionar = itemView.findViewById<EditText>(R.id.inputAdicionar)
            val contrainsProdtudos = itemView.findViewById<ConstraintLayout>(R.id.contrainsProdtudos)
            val lixeira = itemView.findViewById<TextView>(R.id.lixeira)
            val totalSomado = itemView.findViewById<TextView>(R.id.totalSomado)
            val stTextTotal = itemView.findViewById<TextView>(R.id.stTextTotal)
            val stTextUnitario = itemView.findViewById<TextView>(R.id.stTextUnitario)
            val view2 = itemView.findViewById<View>(R.id.view2)
            private var currentTextWatcher: TextWatcher? = null

            fun bind(produto: Produtos, atualizaInfosItens: AtualizaInfosItens, listaLojas: ArrayList<Pair<Int, Any>>,
                     atualizaInfoTopo: AtualizaInfoTopo, listaCarrinho: ArrayList<CarrinhoItemCotacao>, valorMaximoLoja: Double, empresa: Empresa) {

                val nomeBarra = "${produto.Nome} ${produto.Barra}"
                val spannable = SpannableString(nomeBarra)
                val barraStart = nomeBarra.indexOf(produto.Barra)
                val barraEnd = barraStart + produto.Barra.length
                val blueColor = ContextCompat.getColor(itemView.context, R.color.blue500)


                inputAdicionar.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                        true
                    } else {
                        false
                    }
                }

                if(produto.quantidadeAdicionada >0){
                    inputAdicionar.setText(produto.quantidadeAdicionada.toString())
                    alterarItem(produto.quantidadeAdicionada, produto)

                    lixeira.isVisible = true
                    val valorTotal = alterarItem(produto.quantidadeAdicionada, produto)
                    val formataTExto = FormatarTexto()
                    val valorTotalFormatado = formataTExto.formatarParaMoeda(valorTotal)
                    totalSomado.text = "${valorTotalFormatado}"


                    CoroutineScope(Dispatchers.IO).launch {
                        for(progressiva in produto.listaProgressiva ){
                            if(produto.quantidadeAdicionada >= progressiva.quantidadePedido){
                                produto.progressiva = progressiva
                            }
                        }
                    }
                }else{
                    inputAdicionar.setText("")
                    inputAdicionar.setHint("0")
                    totalSomado.text = "R$ 0,00"
                    lixeira.isVisible = false

                }
                spannable.setSpan(
                    ForegroundColorSpan(blueColor),
                    barraStart,
                    barraEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                nomeProdutoAaZ.text = spannable

                if(produto.quantidadeAdicionada ==0){
                    inputAdicionar.setText("")
                    inputAdicionar.setHint("0")

                }else{
                    inputAdicionar.setText(produto.quantidadeAdicionada.toString())

                }
                lixeira.isVisible = produto.quantidadeAdicionada > 0

                if(produto.isCarrinhoOperdor == 1){
                    nomeProdutoAaZ.setTextColor(ContextCompat.getColor(itemView.context, R.color.danger700))
                    valorComDesconto.setTextColor(ContextCompat.getColor(itemView.context, R.color.danger500))
                    valorComDesconto.text = "Produto indisponivel para o operador selecionado"
                    textDeconto.setText("")
                    valorPF.setText("")
                    contrainsProdtudos.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.danger300_50))
                    lixeira.isVisible = false
                    inputAdicionar.isEnabled = false
                    inputAdicionar.background = ContextCompat.getDrawable(itemView.context, R.drawable.bordas_input_produto_aaz_vermelho)
                    inputAdicionar.setTextColor(ContextCompat.getColor(itemView.context, R.color.danger500))
                    totalSomado.isVisible = false
                    val shakeAnimation = ObjectAnimator.ofFloat(contrainsProdtudos, "translationX", -10f, 10f)
                    shakeAnimation.duration = 100
                    shakeAnimation.repeatCount = 5
                    shakeAnimation.repeatMode = ObjectAnimator.REVERSE
                    shakeAnimation.start()
                    val layoutParam = valorComDesconto.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParam.marginStart = 0
                    valorComDesconto.layoutParams = layoutParam
                    contrainsProdtudos.isEnabled = false

                }else{
                    try {
                        val progressiva = if (produto.progressiva == null) produto.listaProgressiva[0] else produto.progressiva!!
                        valorPF.text = "${FormatarTexto().formatarParaMoeda(progressiva.valorUnitario)}"
                        textDeconto.isVisible = progressiva.desconto > 0.0
                        valorPF.isVisible = progressiva.desconto > 0.0
                        if (progressiva.desconto <= 0.0){
                            val layoutParam = valorComDesconto.layoutParams as ViewGroup.MarginLayoutParams
                            layoutParam.marginStart = 0
                            valorComDesconto.layoutParams = layoutParam
                        }
                        if(progressiva.stUnitario > 0.0){

                            stTextUnitario.setText("+ST: ${FormatarTexto().formatarParaMoeda(progressiva.stUnitario)}")
                            val stTotalMoeda = if (progressiva.stUnitario > 0) progressiva.stUnitario * produto.quantidadeAdicionada else progressiva.stUnitario
                            stTextTotal.setText("+ ST: ${FormatarTexto().formatarParaMoeda(stTotalMoeda)}")
                            stTextTotal.isVisible = true
                            stTextUnitario.isVisible = true

                        }else{
                            stTextTotal.isVisible = false
                            stTextUnitario.isVisible = false
                        }

                        textDeconto.text = "${progressiva.desconto.toString().replace(".",",")}% desc."
                        valorComDesconto.text = " ${FormatarTexto().formatarParaMoeda(progressiva.valorUnitarioDesconto) }"
                        contrainsProdtudos.isEnabled = true
                        nomeProdutoAaZ.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray900))
                        valorPF.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray400))
                        valorComDesconto.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray900))

                        contrainsProdtudos.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                        inputAdicionar.isEnabled = true
                        inputAdicionar.background = ContextCompat.getDrawable(itemView.context, R.drawable.bordas_input_produto_aaz)
                        inputAdicionar.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray800))
                        totalSomado.isVisible = true
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                currentTextWatcher?.let {
                    inputAdicionar.removeTextChangedListener(it)
                }


                currentTextWatcher = object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        var quantidade = s.toString()
                        if (quantidade.isBlank() || quantidade.isEmpty()) {
                            quantidade = "0"
                        }
                        if(quantidade.contains(".")){
                            quantidade = quantidade.replace(".", "")
                            inputAdicionar.setText(quantidade)

                        }
                        if (quantidade.toInt() != produto.quantidadeAdicionada && inputAdicionar.isFocused) {
                            val valorTotal = alterarItem(quantidade.toInt(), produto)
                            val loja = listaLojas.filter { it.second is Lojas && (it.second as Lojas).Loja_ID == produto.lojaID }[0].second as Lojas


                            CoroutineScope(Dispatchers.IO).launch {
                                produto.quantidadeAdicionada = quantidade.toInt()
                                produto.valorProdutoTotal = valorTotal

                                inserirNoCarrinho(itemView.context, produto, listaLojas, empresa)
                                val valorTotalLoja = confereValorTotal(produto.cnpj, loja)

                                if(valorTotalLoja > produto.valorMaximo){
                                    produto.quantidadeAdicionada =0
                                    produto.valorProdutoTotal = 0.0
                                    inserirNoCarrinho(itemView.context, produto, listaLojas, empresa)


                                    MainScope().launch {
                                        Alertas.alertaErro(inputAdicionar.context,
                                            "O valor máximo de compra para esta loja é de  ${FormatarTexto().formatarParaMoeda(valorMaximoLoja)}",
                                            "Ops!" ){
                                        }
                                        totalSomado.text = "R$ 0,00"
                                        inputAdicionar.setText("")
                                        inputAdicionar.setHint("0")
                                        stTextTotal.text = "+ST: R$ 0,00"
                                        lixeira.isVisible =false
                                        atualizaInfosItens.atualizaTopo(produto.lojaID, produto)

                                    }

                                }else{
                                    withContext(Dispatchers.Main) {
                                            val formataTexto = FormatarTexto()
                                            val valorTotalFormatado = formataTexto.formatarParaMoeda(valorTotal)
                                            textDeconto.setText("${produto.progressiva!!.desconto} desc.")
                                            valorComDesconto.text = "${FormatarTexto().formatarParaMoeda(produto.progressiva!!.valorUnitarioDesconto)}"
                                            totalSomado.text = valorTotalFormatado
                                            lixeira.isVisible = quantidade.toInt() > 0
                                            if(produto.progressiva!!.stUnitario > 0.0){
                                                val stTotalMoeda = produto.progressiva!!.stUnitario * produto.quantidadeAdicionada
                                                stTextTotal.text = "+ST: ${formataTexto.formatarParaMoeda(stTotalMoeda)}"
                                            }
                                            atualizaInfosItens.atualizaTopo(produto.lojaID, produto)

                                    }
                                }
                            }

                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                }
                inputAdicionar.addTextChangedListener(currentTextWatcher)
                atualizaInfoTopo.atualizaInfoTopo(produto.lojaID)
            }
            suspend fun inserirNoCarrinho(context: Context, produto: Produtos,listaLojas: ArrayList<Pair<Int, Any>>, empresa: Empresa){
                val daoHelper = DAOHelper(context).writableDatabase
                val daoTBCarrinho = DAOCarrinho()
                val loja = listaLojas.filter { it.second is Lojas && (it.second as Lojas).Loja_ID == produto.lojaID }[0].second as Lojas
                daoTBCarrinho.buscarItens(daoHelper,produto,loja, empresa.razaoSocial)

            }
           suspend fun confereValorTotal(cnpj: String, loja: Lojas):Double{
                val daoHelper = DAOHelper(itemView.context).writableDatabase
                val daoTBCarrinho = DAOCarrinho()
                val (valorTotal,valorTotalSt) = daoTBCarrinho.buscaValorTotal(daoHelper,loja,cnpj)
                return valorTotal
            }
            fun alterarItem(valorCampo:Int, produto: Produtos): Double{
                val subQuantidade = valorCampo
                for(progressiva in produto.listaProgressiva ){
                    if(subQuantidade >= progressiva.quantidadePedido){
                        produto.progressiva = progressiva
                        produto.idProgressiva = progressiva.id
                    }
                }
                if ( produto.progressiva != null ){
                    val valorTotal = produto.progressiva?.valorUnitarioDesconto!! * subQuantidade
                    return valorTotal
                }else{
                    return 0.0
                }



            }
        }

    }


    suspend fun inserirNoCarrinho(context: Context, produto: Produtos, loja: Lojas, empresa: Empresa){
        val daoHelper = DAOHelper(context).writableDatabase
        val daoTBCarrinho = DAOCarrinho()
        daoTBCarrinho.buscarItens(daoHelper,produto,loja, empresa.razaoSocial)

    }
    private fun setupIndicator(context: Context, holder:ViewHolder.FiltroLoja) {
        val indicators = arrayOfNulls<ImageView>(listaBanners.size)
        val layoutParams = LinearLayout.LayoutParams(
            20, 20
        )

        layoutParams.setMargins(8, 0, 8, 0)
        layoutParams.gravity = Gravity.CENTER
        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i]!!.setImageResource(R.drawable.indicador_deselecionado)
            indicators[i]!!.layoutParams = layoutParams
            holder. indicatorLayout.addView(indicators[i])
        }
    }

    private fun setCurrentPageIndicator(position: Int, holder:ViewHolder.FiltroLoja) {
        currentPage = position
        val childCount: Int = holder.indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = holder.indicatorLayout.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageResource(R.drawable.indicador_selecionado)
            } else {
                imageView.setImageResource(R.drawable.indicador_deselecionado)
            }
        }
    }

}
