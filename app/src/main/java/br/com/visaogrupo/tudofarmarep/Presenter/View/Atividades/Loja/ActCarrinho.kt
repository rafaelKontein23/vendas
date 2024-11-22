package br.com.visaogrupo.tudofarmarep.Views.Activitys

import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterCarrinhoProdutos
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterEscolhesOperadores
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterFormaDePagamento
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterLooping
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskOperadorXLoja
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosItens
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosOperador
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.IVoltaLoja
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Controlers.ControlerCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOCotacao
import br.com.visaogrupo.tudofarmarep.DAO.DAOFormaPagamento
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.Objetos.Cotacao
import br.com.visaogrupo.tudofarmarep.Objetos.CotacaoCarrinho
import br.com.visaogrupo.tudofarmarep.Objetos.Empresa
import br.com.visaogrupo.tudofarmarep.Objetos.FormaPagamento
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.OperadorLogistico
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.R
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class ActCarrinho : AppCompatActivity(), AtualizaInfosItens, AtualizaInfosOperador, IVoltaLoja {
    lateinit var voltarCarrinho: ImageView
    lateinit var cnpjSelecionado:TextView
    lateinit var pedido:TextView
    lateinit var nomeLoja:TextView
    lateinit var recyProdutosCarrinho: RecyclerView
    lateinit var recyclerOpl:RecyclerView
    lateinit var textOperadorMax:TextView
    lateinit var recyFormaDePagmento:RecyclerView
    lateinit var edtObservaocao:EditText
    lateinit var numeroPedidoEdit:EditText
    lateinit var fundo:LinearLayout
    lateinit var barraProgressoMinimo:LinearLayout
    lateinit var textvalorMinimo:TextView
    lateinit var textValorFaltante:TextView
    lateinit var textunidadeTotal:TextView
    lateinit var textvalorTotalCarrinho:TextView
    lateinit var btnFinalizarPedido:TextView
    lateinit var valorFalta:ConstraintLayout
    lateinit var quantidadeCaractereObservacao:TextView
    lateinit var quantidadeCaracterenumeroPedido:TextView
    lateinit var formaDePagamentoTitulo:TextView
    lateinit var continuarComprando :TextView
    lateinit var oplTitulo:TextView
    lateinit var  enderecoCNPJ:TextView
    var  valorotalComissao = 0.0

    lateinit var telefone:TextView
    lateinit var recyclerOplloopin:RecyclerView
    lateinit var loopTitulo :TextView
    lateinit var  visualizarComissao :ImageView
    lateinit var valorComiisao :TextView
    lateinit var  stTextTotal:TextView
    lateinit var itemDescricaoLopping:TextView
    lateinit var loja:Lojas
    var cotacaoCarrinhoAux: CotacaoCarrinho? = null
    lateinit var constrainCarregando :ConstraintLayout
    lateinit var empresa:Empresa
    var adapterLoop :AdapterLooping? =null
    var listaProdutos = ArrayList<Produtos>()
    var listaOplSelecionados = ArrayList<OperadorLogistico>()
    var listaOplSelecionadosPricipal = ArrayList<OperadorLogistico>()
    var adapterEscolhesOperadores :AdapterEscolhesOperadores? = null
    var cotacaoCarrinho:CotacaoCarrinho? = null
    var valorTotalLoja = 0.0
    var listaFormaDePagamentoSelecionada = ArrayList<FormaPagamento>()
    lateinit var scrollViewCarrinho : NestedScrollView
    var valorotalSTCarrio = 0.0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_act_carrinho)
        voltarCarrinho = findViewById(R.id.voltarCarrinho)
        cnpjSelecionado = findViewById(R.id.cnpjSelecionado)
        pedido = findViewById(R.id.pedido)
        nomeLoja = findViewById(R.id.nomeLoja)
        stTextTotal = findViewById(R.id.stTextTotal)
        continuarComprando = findViewById(R.id.continuarComprando)
        recyProdutosCarrinho = findViewById(R.id.recyProdutosCarrinho)
        recyclerOpl = findViewById(R.id.recyclerOpl)
        textOperadorMax = findViewById(R.id.textOperadorMax)
        recyFormaDePagmento = findViewById(R.id.recyFormaDePagmento)
        edtObservaocao = findViewById(R.id.edtObservaocao)
        numeroPedidoEdit = findViewById(R.id.numeroPedidoEdit)
        fundo = findViewById(R.id.fundo)
        barraProgressoMinimo = findViewById(R.id.barraProgressoMinimo)
        textvalorMinimo = findViewById(R.id.textvalorMinimo)
        textValorFaltante = findViewById(R.id.textValorFaltante)
        constrainCarregando = findViewById(R.id.constrainCarregando)
        textunidadeTotal = findViewById(R.id.textunidadeTotal)
        textvalorTotalCarrinho = findViewById(R.id.textvalorTotalCarrinho)
        btnFinalizarPedido = findViewById(R.id.btnFinalizarPedido)
        recyclerOplloopin = findViewById(R.id.recyclerOplloopin)
        valorFalta = findViewById(R.id.valorFalta)
        scrollViewCarrinho = findViewById(R.id.scrollView3)
        quantidadeCaractereObservacao = findViewById(R.id.quantidadeCaractereObservacao)
        quantidadeCaracterenumeroPedido = findViewById(R.id.quantidadeCaracterenumeroPedido)
        enderecoCNPJ = findViewById(R.id.enderecoCNPJ)
        telefone = findViewById(R.id.telefone)
        formaDePagamentoTitulo = findViewById(R.id.formaDePagamentoTitulo)
        visualizarComissao = findViewById(R.id.visualizarComissao)
        valorComiisao = findViewById(R.id.valorComiisao)
        oplTitulo = findViewById(R.id.oplTitulo)
        loopTitulo = findViewById(R.id.loopTitulo)
        itemDescricaoLopping = findViewById(R.id.itemDescricaoLopping)

        MainScope().launch {
            cnpjSelecionado.text = FormatarTexto().formatCNPJ(empresa.cnpj)
        }
        continuarComprando.setOnClickListener {
            val intent = Intent()
            intent.putExtra("loja",loja)
            setResult(RESULT_OK, intent)
            finish()
        }
        val lojaJson = intent.getStringExtra("loja")
        val empresaJson = intent.getStringExtra("empresa")
        loja = Gson().fromJson(lojaJson, Lojas::class.java)
        empresa = Gson().fromJson(empresaJson, Empresa::class.java)
        enderecoCNPJ.text = "${empresa.endereco} - ${empresa.cidade} - ${empresa.uf}"
        loopTitulo.isVisible = loja.QtdeMaxOperador != 1
        itemDescricaoLopping.isVisible = loja.QtdeMaxOperador != 1
        recyclerOplloopin.isVisible = loja.QtdeMaxOperador != 1
        if(loja.carrinhoID != 0){
            val daoHelper = DAOHelper(applicationContext).writableDatabase
            val daoCotacao = DAOCotacao()
            cotacaoCarrinho = daoCotacao.selectCotacao(daoHelper,loja.carrinhoID)
            if(cotacaoCarrinho != null){
                cotacaoCarrinhoAux = cotacaoCarrinho

            }

        }

        enderecoCNPJ.setOnClickListener {
            val url = "https://www.google.com/maps/search/?api=1&query=${enderecoCNPJ.text}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        cnpjSelecionado.setOnClickListener {
            if (enderecoCNPJ.isVisible){
                enderecoCNPJ.isVisible = false
                telefone.isVisible = false
            }else{
                enderecoCNPJ.isVisible = true
                telefone.isVisible = true
            }
        }
        visualizarComissao?.setOnClickListener {
            val valorComissao = valorComiisao.text.toString()
            if (valorComissao == "-"){
                visualizarComissao!!.setImageResource(R.drawable.visualiza)
                valorComiisao.text = FormatarTexto().formatarParaMoeda(valorotalComissao)
            }else{
                visualizarComissao!!.setImageResource(R.drawable.sem_visu)
                valorComiisao.text = "-"

            }
        }


        nomeLoja.text = loja.Nome
        textOperadorMax.text = "Escolha até ${loja.QtdeMaxOperador} operador(es)"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        voltarCarrinho.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()

        }

        btnFinalizarPedido.setOnClickListener {
            if (valorTotalLoja < loja.ValorMinimo){
                Toast.makeText(applicationContext, "Valor mínimo do pedido não alcançado", Toast.LENGTH_SHORT).show()
            }else{
                var formaPagamentoSelecionado=  false
                var operadorSelecionado = false
                if(listaFormaDePagamentoSelecionada.size == 0){
                    formaPagamentoSelecionado = true
                }
                if(listaOplSelecionados.size == 0){
                    operadorSelecionado = true
                }
                if(operadorSelecionado){
                    scrollViewCarrinho.smoothScrollTo(0,oplTitulo.top)
                    Toast.makeText(applicationContext, "Selecione pelo menos um operador", Toast.LENGTH_SHORT).show()
                }else if(formaPagamentoSelecionado){
                    scrollViewCarrinho.smoothScrollTo(0,formaDePagamentoTitulo.top)
                    Toast.makeText(applicationContext, "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show()

                }else  if( valorTotalLoja > loja.ValorMaximo) {
                    Alertas.alertaErro(this@ActCarrinho,
                        "O valor máximo de compra para  esta loja é de  ${FormatarTexto().formatarParaMoeda(loja.ValorMaximo)}",
                        "Ops!" ){

                    }
                }else{
                    Alertas.alertaErro(this@ActCarrinho,
                        "Deseja enviar o pedido?",
                        "Loiu informa", "Não", "Sim" ){
                        val controlerCarrinho = ControlerCarrinho()
                        constrainCarregando.isVisible = true
                        controlerCarrinho.enviaPedido(
                            listaProdutos,
                            edtObservaocao.text.toString(),
                            numeroPedidoEdit.text.toString(),
                            listaOplSelecionados,
                            listaFormaDePagamentoSelecionada[0].formaPagamentoMarcas_ID.toString(),
                            loja,
                            empresa,this@ActCarrinho, this@ActCarrinho, constrainCarregando,
                            valorotalSTCarrio

                        )
                        if(loja.carrinhoID != 0){
                            CoroutineScope(Dispatchers.IO).launch {
                                val daoHelper = DAOHelper(applicationContext).writableDatabase
                                val daoCotacao = DAOCotacao()
                                daoCotacao.deleteCotacao(daoHelper,loja.carrinhoID)
                            }
                        }
                    }
                }
            }
        }

        edtObservaocao.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val quantidaCaractere = s.toString().length
                quantidadeCaractereObservacao.text = "${quantidaCaractere}/255"
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        numeroPedidoEdit.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 val quantidaCaractere = s.toString().length
                quantidadeCaracterenumeroPedido.text = "${quantidaCaractere}/10"
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        buscaInfosCarrinho(empresa,loja)
        buscaOperadorFormaPagamento()

    }
    fun calcularValorMinimo(valorTotal:Double, loja: Lojas){
        textvalorMinimo.text ="Pedido Mínimo ${FormatarTexto().formatarParaMoeda(loja.ValorMinimo)}"
        if(valorTotal > loja.ValorMinimo){
            valorFalta.isVisible = false
            animarBarraProgress(100f)
            barraProgressoMinimo.backgroundTintList = null
            val colorTint = ContextCompat.getColor(applicationContext, R.color.success600)
            barraProgressoMinimo.backgroundTintList =  ColorStateList.valueOf(colorTint)
        }else{
            val valorFaltante = loja.ValorMinimo - valorTotal
            val formataTexto = FormatarTexto()
            val valorFaltanteFormatado = formataTexto.formatarParaMoeda(valorFaltante)
            textValorFaltante.text = valorFaltanteFormatado
            valorFalta.isVisible = true
            val porcentagem = (valorTotal/loja.ValorMinimo)*100

            animarBarraProgress(porcentagem.toFloat())
            barraProgressoMinimo.backgroundTintList = null

            if(porcentagem < 30){
                val colorTint = ContextCompat.getColor(applicationContext, R.color.danger500)
                barraProgressoMinimo.backgroundTintList =  ColorStateList.valueOf(colorTint)
            }else if(porcentagem >31 && porcentagem < 90){
                val colorTint = ContextCompat.getColor(applicationContext, R.color.warning500)
                barraProgressoMinimo.backgroundTintList =  ColorStateList.valueOf(colorTint)
            }else{
                val colorTint = ContextCompat.getColor(applicationContext, R.color.warnin600)
                barraProgressoMinimo.backgroundTintList =  ColorStateList.valueOf(colorTint)
            }
        }
    }
    fun buscaInfosCarrinho(empresa:Empresa, loja: Lojas){
        CoroutineScope(Dispatchers.IO).launch {
            val daoHelper = DAOHelper(applicationContext).writableDatabase
            val DAOCarrinho = DAOCarrinho()
            val resultado = if (empresa.cnpj.isEmpty()) "11985401000131" else empresa.cnpj

             listaProdutos = DAOCarrinho.buscarItensCarrinho(daoHelper,loja, resultado)
            if (listaProdutos.isEmpty()){
                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()

            }
            val tarefaValortotal = launch {
                var valortotalSomado = 0.0
                var quantidadeTotal = 0
                valorotalComissao = 0.0
                for (produto in listaProdutos){
                    valortotalSomado += produto.valorProdutoTotal
                    quantidadeTotal += produto.quantidadeAdicionada

                    if(produto.progressiva != null){
                        val valoTotaStUnit = produto.progressiva!!.stUnitario * produto.quantidadeAdicionada
                        valorotalComissao += produto.progressiva!!.comissaoTotal
                        valorotalSTCarrio += valoTotaStUnit
                    }

                }
                runOnUiThread {
                    val formataTexto = FormatarTexto()
                    valorTotalLoja = valortotalSomado
                    calcularValorMinimo(valortotalSomado,loja)
                    val valorSomadoFormatado = formataTexto.formatarParaMoeda(valortotalSomado)
                    textvalorTotalCarrinho.text  =valorSomadoFormatado
                    valorComiisao.text = formataTexto.formatarParaMoeda(valorotalComissao)
                    textunidadeTotal.text = "${quantidadeTotal} uni."
                    if (valorotalSTCarrio > 0){
                        stTextTotal.text = "+ ST: ${formataTexto.formatarParaMoeda(valorotalSTCarrio)}"
                    }else{
                        stTextTotal.isVisible = false
                    }
                }
            }
            tarefaValortotal.join()
            runOnUiThread {
                val linearLayoutManager = LinearLayoutManager(applicationContext)
                val adapterCarrinhoProdutos = AdapterCarrinhoProdutos(listaProdutos, this@ActCarrinho,loja, empresa.razaoSocial)
                recyProdutosCarrinho.layoutManager = linearLayoutManager
                recyProdutosCarrinho.adapter = adapterCarrinhoProdutos
                recyProdutosCarrinho.setHasFixedSize(true)
            }

        }
    }
    fun buscaOperadorFormaPagamento(){
        CoroutineScope(Dispatchers.IO).launch {
            val tarefaOperador = launch {
                val TaskOperadorXLoja = TaskOperadorXLoja()
                val listaOperador = TaskOperadorXLoja.buscaOperadorxLoja(loja.Loja_ID,empresa.uf)

                if (cotacaoCarrinho != null) {
                    val operadoresLogisticos = listOf(
                        cotacaoCarrinho!!.operadorLogistico1,
                        cotacaoCarrinho!!.operadorLogistico2,
                        cotacaoCarrinho!!.operadorLogistico3,
                        cotacaoCarrinho!!.operadorLogistico4,
                        cotacaoCarrinho!!.operadorLogistico5
                    )

                    for (operadorId in operadoresLogisticos) {
                        listaOperador.find { it.OperadorLogistico_ID == operadorId }?.let { cotacaoEncontrada ->
                            listaOplSelecionados.add(cotacaoEncontrada)
                        }
                    }

                }
                if(!listaOplSelecionados.isEmpty()){
                    val operador = listaOplSelecionados.first()
                    if(loja.operadorIDSelecionado != operador.OperadorLogistico_Grupo_ID){
                        listaOplSelecionados.clear()
                    }
                }
                val listaOepradorSelecionado =  listaOperador.filter { it.OperadorLogistico_Grupo_ID == loja.operadorIDSelecionado } as ArrayList<OperadorLogistico>

                val listaLoop = listaOperador.filter { it.OperadorLogistico_Grupo_ID != loja.operadorIDSelecionado } as ArrayList<OperadorLogistico>
                runOnUiThread {
                    val linearLayoutManager = LinearLayoutManager(applicationContext)
                    val linearLayoutManagerLoop = LinearLayoutManager(applicationContext)

                    adapterLoop = AdapterLooping(listaLoop,loja,listaOplSelecionados, this@ActCarrinho)

                     adapterEscolhesOperadores = AdapterEscolhesOperadores(listaOepradorSelecionado,
                        loja,
                        listaOperadoresSelecionados = listaOplSelecionados,
                        this@ActCarrinho,
                        listaOplSelecionadosPricipal,cotacaoCarrinho)
                    recyclerOplloopin.layoutManager = linearLayoutManager
                    recyclerOpl.layoutManager = linearLayoutManagerLoop
                    recyclerOpl.adapter = adapterEscolhesOperadores
                    recyclerOplloopin.adapter = adapterLoop



                }
            }
            val tarefaFormaDePagamento = launch {
                val daoHelper = DAOHelper(applicationContext).writableDatabase
                val daoFormaPagamento = DAOFormaPagamento()
                val listaFormaPagamento = daoFormaPagamento.buscarFormaPagamento(daoHelper, loja.Loja_ID)
                runOnUiThread {
                    val linearLayoutManager = LinearLayoutManager(applicationContext)
                    val adapterFormaDePagamento = AdapterFormaDePagamento(listaFormaPagamento,
                        listaFormaDePagamentoSelecionada, cotacaoCarrinho)
                    recyFormaDePagmento.layoutManager = linearLayoutManager
                    recyFormaDePagmento.adapter = adapterFormaDePagamento
                    recyFormaDePagmento.setHasFixedSize(true)
                }

            }
            tarefaFormaDePagamento.join()
            tarefaOperador.join()
        }
    }

    fun animarBarraProgress(porcentagem:Float){
        val animator = ValueAnimator.ofFloat(10f, porcentagem)
        animator.duration = 2000

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            val layoutParams = barraProgressoMinimo.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = animatedValue
            barraProgressoMinimo.layoutParams = layoutParams
        }

        animator.start()
    }

    override fun atualizaTopo(lojaId: Int, produtos: Produtos?, paraLoppin: Boolean) {
        TODO("Not yet implemented")
    }



    override fun atualizaItensCarrinho() {
        valorotalSTCarrio = 0.0
        buscaInfosCarrinho(empresa,loja)
    }

    override fun atualizaTExtoOperador(atualizAdapter: Boolean, atualizAdapterOpl: Boolean) {
        val oplsSelecionados = loja.QtdeMaxOperador - listaOplSelecionados.size

        if(oplsSelecionados > 0){
            textOperadorMax.text = "Escolha até ${oplsSelecionados} operadore(s)"
        }else if(listaOplSelecionados.size == 0){
            textOperadorMax.text = "Escolha até ${loja.QtdeMaxOperador} operadore(s)"

        }else if(listaOplSelecionados.size  == loja.QtdeMaxOperador){
            textOperadorMax.text = "Maximo de operador selecionado atingido"
        }
        if (atualizAdapter){
            if (listaOplSelecionadosPricipal.isEmpty()){
                listaOplSelecionados.clear()
                textOperadorMax.text = "Escolha até ${loja.QtdeMaxOperador} operadore(s)"


            }
            adapterLoop?.selecionouOplPrincipal = listaOplSelecionadosPricipal.isNotEmpty()
            adapterLoop?.notifyDataSetChanged()

        }
        if(atualizAdapterOpl){
             adapterEscolhesOperadores?.notifyDataSetChanged()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()

    }

    override fun voltaLoja() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

}