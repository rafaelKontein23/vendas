package br.com.visaogrupo.tudofarmarep

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterBanners
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterFiltrosLojas
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterRecyclerLojas
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskConsultaLoja
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskBanners
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaFiltroAtribudos
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfoTopo
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaListaFiltro
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProdutosOperadores
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosItens
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.InterfaceRemoverProtudosView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.InterfaceScrolaLista
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.TrocaInfoTopo
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOCotacao
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.DAO.DAOLojas
import br.com.visaogrupo.tudofarmarep.DAO.DAOProdutos
import br.com.visaogrupo.tudofarmarep.DAO.DAOProgressiva
import br.com.visaogrupo.tudofarmarep.Objetos.Banners
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.Objetos.Cotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Empresa
import br.com.visaogrupo.tudofarmarep.Objetos.LojaComparador
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Views.Activitys.ActCarrinho

import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogFiltros
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogFiltrosLojas
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogOpls
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask


class ActLojaPadrao : AppCompatActivity(), InterfaceRemoverProtudosView, TrocaInfoTopo, InterfaceScrolaLista,
    AtualizaListaFiltro,AtualizaProdutosOperadores, AtualizaFiltroAtribudos,
    AtualizaInfosItens, AtualizaInfoTopo {

    private lateinit var voltarLoja: ImageView
    private lateinit var busca: ImageView
    private lateinit var cnpjSelecionado: TextView
    private lateinit var contrainInfos: ConstraintLayout
    private lateinit var razaoSocial: TextView
    private lateinit var enderecoCNPJ: TextView
    private lateinit var telefone: TextView
    private lateinit var celular: TextView
    private lateinit var recyLojas: RecyclerView
    private lateinit var contrainsItens: ConstraintLayout
    private lateinit var filtroLojasRecy: RecyclerView
    lateinit var constrainsTopFixo: ConstraintLayout


    var  lojasAux: ArrayList<Pair<Int, Any>> = ArrayList()
    var  lojasPrimeiraFiltros : ArrayList<Pair<Int, Any>> = ArrayList()
    var  lojasFiltroCombinado : ArrayList<Pair<Int, Any>> = ArrayList()
    var  lojasFiltroLista: ArrayList<Lojas> = ArrayList()
    var listaComparador= ArrayList<LojaComparador>()
    var  listaCarrinho: ArrayList<CarrinhoItemCotacao>  = ArrayList()
    var  cotacao:Cotacao? = null

    lateinit var buscaItens : EditText
    lateinit var limparBusca : ImageView
    lateinit var adapterRecyclerLojas:AdapterRecyclerLojas
    lateinit var constrainCarrinho: ConstraintLayout
    lateinit var constrainFiltros: ConstraintLayout
    lateinit var constrainOpls: ConstraintLayout
    lateinit var abrirProdutudos: ImageView
    lateinit var tituloTortal: TextView
    lateinit var totalValor: TextView
    lateinit var nomeLoja: TextView
    lateinit var imgMarca: ImageView
    lateinit var contrainsProdtudos: ConstraintLayout
    lateinit var setaFiltroEsquerda: ImageView
    lateinit var setaFiltroDireita: ImageView
    lateinit var  contrainsTopo: ConstraintLayout
    lateinit var  lojaSelecionada: Lojas
    lateinit var nomeOperadorSelecionado: TextView
    var listaAuxOperadores: ArrayList<Pair<Int, Any>> = ArrayList()
    var adapterFiltro : AdapterFiltrosLojas? = null
    var  cnpjStr: String = ""
    private lateinit var  carregandoLoja:ProgressBar
    lateinit var  filtroContainer: ConstraintLayout
    lateinit var quantidaeFiltro: TextView
    var  empresa : Empresa=  Empresa("","","", "DF")
    var endereco = ""
    private var listaBanners:ArrayList<Banners> = ArrayList()
    var operadorCotacao  = 0
    var reprsentanteID = 0
    var nomeOperador = ""
    lateinit var stTextTotal:TextView

    @RequiresApi(Build.VERSION_CODES.N)
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_loja_padrao2)

        voltarLoja   = findViewById(R.id.voltarLoja)
        busca       = findViewById(R.id.busca)
        cnpjSelecionado = findViewById(R.id.cnpjSelecionado)
        razaoSocial = findViewById(R.id.razaoSocial)
        enderecoCNPJ = findViewById(R.id.enderecoCNPJ)
        telefone = findViewById(R.id.telefone)

        celular = findViewById(R.id.celular)
        recyLojas = findViewById(R.id.recyLojas)
        filtroLojasRecy = findViewById(R.id.filtroLojasRecy)
        carregandoLoja = findViewById(R.id.carregandoLoja)
        contrainInfos = findViewById(R.id.contrainInfos)
        setaFiltroDireita = findViewById(R.id.setaFiltroDireito)
        contrainsItens = findViewById(R.id.contrainsItens)
        buscaItens = findViewById(R.id.buscaItens)
        limparBusca = findViewById(R.id.limparBusca)
        constrainsTopFixo = findViewById(R.id.constrainsTopFixo)
        nomeLoja = findViewById(R.id.nomeLoja)
        imgMarca = findViewById(R.id.imgMarca)
        totalValor =findViewById(R.id.totalValor)
        tituloTortal= findViewById(R.id.tituloTortal)
        abrirProdutudos = findViewById(R.id.abrirProdutudos)
        constrainOpls = findViewById(R.id.constrainOpls)
        constrainFiltros = findViewById(R.id.constrainFiltros)
        constrainCarrinho = findViewById(R.id.constrainCarrinho)
        contrainsProdtudos = findViewById(R.id.contrains)
        setaFiltroEsquerda = findViewById(R.id.setaFiltroEsquerda)
        contrainsTopo = findViewById(R.id.contrainsTopo)
        nomeOperadorSelecionado = findViewById(R.id.nomeOperadorSelecionado)
        filtroContainer = findViewById(R.id.filtroContainer)
        quantidaeFiltro = findViewById(R.id.quantidaeFiltro)
        stTextTotal = findViewById(R.id.stTextTotal)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this@ActLojaPadrao)
        reprsentanteID = prefs.getInt("reprsentante_id", 0)
        val bundle = intent.getBundleExtra("cnpjSelecionadoBundle")
        val bundlecotacao= intent.getBundleExtra("cotacaoBundle")
        val  bundleCarrinho = intent.getBundleExtra("listaCarrinhoBundle")
        val  bundleNomeOperador = intent.getBundleExtra("NomeOperadorBundle")
        nomeOperador = bundleNomeOperador?.getString("NomeOperadorBundle") ?: ""
        val cnpj = bundle?.getSerializable("cnpjSelecionado") as Cnpj
        val bundleLOperadorCotacao = intent.getBundleExtra("operadorCotacaoBundle")
        operadorCotacao = bundleLOperadorCotacao?.getInt("operadorCotacao") ?: 0
        cotacao = bundlecotacao?.getSerializable("cotacao") as Cotacao?

        listaCarrinho = bundleCarrinho?.getSerializable("listaCarrinho") as ArrayList<CarrinhoItemCotacao>




        if (cnpj != null) {
            try {
                endereco =  cnpj.Endereco + "," + cnpj.Numero +
                        ", " + cnpj.Cidade + "/" + cnpj.UF + " - "+ cnpj.bairro
                cnpjStr = cnpj.CNPJ
                empresa = Empresa(cnpjStr, cnpj.RazaoSocial, endereco, cnpj.UF, cnpj.Cidade, cnpj.Numero, isCarteira = cnpj.carteira)

            } catch (e: JSONException) {
                e.printStackTrace()

            }
        }
        constrainCarrinho.setOnClickListener {
            val daoHelper = DAOHelper(applicationContext).writableDatabase
            val daoCarrinho = DAOCarrinho()
            val isCarrinho = daoCarrinho.verificaCarrinho(daoHelper, lojaSelecionada.Loja_ID, cnpjStr, lojaSelecionada)
            daoHelper.close()
            if(!isCarrinho){
                Toast.makeText(applicationContext, "Carrinho Vazio", Toast.LENGTH_SHORT).show()
            }else if(lojaSelecionada.valorTotal > lojaSelecionada.ValorMaximo){
                    Alertas.alertaErro(this,
                        "O valor máximo de compra para esta loja é de  ${FormatarTexto().formatarParaMoeda(lojaSelecionada.ValorMaximo)}",
                        "Ops!" ){

                    }
            }else{
                MainScope().launch {
                    constrainsTopFixo.isVisible = false
                    filtroLojasRecy.isVisible = false
                    setaFiltroEsquerda.isVisible = false
                    setaFiltroDireita.isVisible = false

                }
                val intent = Intent(this, ActCarrinho::class.java)
                val lojaJson = Gson().toJson(lojaSelecionada)
                val empresaJson = Gson().toJson(empresa)
                intent.putExtra("loja", lojaJson)
                intent.putExtra("empresa", empresaJson)
                startActivityForResult(intent,1)

            }

        }
        setaFiltroEsquerda.setOnClickListener {

            val layoutManager = filtroLojasRecy.layoutManager as? LinearLayoutManager
            val currentPosition = layoutManager?.findFirstVisibleItemPosition() ?: return@setOnClickListener

            if (currentPosition > 0) {
                val previousPosition = currentPosition - 1
                filtroLojasRecy.scrollToPosition(previousPosition)
            }
        }
        setaFiltroDireita.setOnClickListener {

            val layoutManager = filtroLojasRecy.layoutManager as? LinearLayoutManager
            val currentPosition = layoutManager?.findLastVisibleItemPosition() ?: return@setOnClickListener

            val itemCount = filtroLojasRecy.adapter?.itemCount ?: return@setOnClickListener
            if (currentPosition < itemCount - 1) {
                val nextPosition = currentPosition + 1
                filtroLojasRecy.scrollToPosition(nextPosition)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(left = systemBars.left, top = systemBars.top, right = systemBars.right, bottom = systemBars.bottom)
            insets
        }

        recyLojas.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                 if (dy <= 0) {
                    if (firstVisibleItemPosition == 0) {
                        constrainsTopFixo.isVisible = false
                        filtroLojasRecy.isVisible = false
                        setaFiltroEsquerda.isVisible = false
                        setaFiltroDireita.isVisible = false

                    }
                }else{
                     val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                     val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                     val viewAtTop = layoutManager.findViewByPosition(firstVisibleItemPosition)

                     if (viewAtTop != null) {
                         when (recyclerView.adapter?.getItemViewType(firstVisibleItemPosition)) {
                             0, 1 -> {
                                 // Ambos os casos compartilham o mesmo comportamento
                                 Log.d("RecyclerView", "Celula Loja ou Produtos está no topo")
                                 if(recyLojas.isVisible){
                                     constrainsTopFixo.isVisible = true
                                     filtroLojasRecy.isVisible = true
                                     setaFiltroEsquerda.isVisible = true
                                     setaFiltroDireita.isVisible = true

                                     mudaInfosLojaTopo()

                                 }

                             }
                             2 -> {
                                 constrainsTopFixo.isVisible = false
                                 filtroLojasRecy.isVisible = false
                                 setaFiltroEsquerda.isVisible = false
                                 setaFiltroDireita.isVisible = false

                                 Log.d("RecyclerView", "Celula Banner está no topo")
                             }
                         }
                     }

                }
            }
        })

        constrainFiltros.setOnClickListener {

                val dialogFiltros = DialogFiltros()
                val listaFiltro = adapterRecyclerLojas.listaLojas.filter { it.second is Produtos  && (it.second as Produtos).lojaID == lojaSelecionada.Loja_ID  }
                dialogFiltros.dialogFiltro(this@ActLojaPadrao, lojaSelecionada, this, listaFiltro.size, lojaSelecionada.operadorIDSelecionado)

        }
        voltarLoja.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        contrainsTopo.setOnClickListener {
            if( lojaSelecionada.ProdutosAbertos){
                abrirProdutudos.rotation = 0f
                lojaSelecionada.ProdutosAbertos = false
                this.removeItens(lojaSelecionada.Loja_ID)
                recyLojas.scrollToPosition(0)
                constrainsTopFixo.isVisible = false

            }else{
                abrirProdutudos.rotation = 180f
                lojaSelecionada.ProdutosAbertos = true
                val position = adapterRecyclerLojas.listaLojas.indexOfFirst { it.second is Lojas && (it.second as Lojas).Loja_ID == lojaSelecionada.Loja_ID }

                this.addItensItens(lojaSelecionada, position)
            }

        }

        constrainOpls.setOnClickListener {
            if(buscaItens.isVisible){
                Toast.makeText(applicationContext, "Não é possível trocar o operador com a busca aberta.", Toast.LENGTH_SHORT).show()
            }else{
                val DAOProgressiva = DAOProgressiva()
                val listaOpls = DAOProgressiva.buscaopls(lojaSelecionada.Loja_ID, applicationContext)
                if(listaOpls.size == 1){
                    Toast.makeText(applicationContext, "Há apenas um operador logistico", Toast.LENGTH_SHORT).show()
                }else{
                    val dialogOpls = DialogOpls()
                    dialogOpls.dialogOperadors(this, lojaSelecionada, this, listaOpls)
                }
            }
        }

        buscaItens.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                buscaItens.hint = ""
            }
        })

        busca.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                buscaItens.isVisible = true
                buscaItens.isEnabled= true
                limparBusca.isVisible = true
                adapterRecyclerLojas.filtroBusca = true
                adapterRecyclerLojas.buscaAberta = true
                adapterRecyclerLojas.notifyDataSetChanged()
            }
        })

        limparBusca.setOnClickListener {
            buscaItens.isVisible = false
            buscaItens.isEnabled= false
            limparBusca.isVisible = false
            buscaItens.setText("")
            buscaItens.hint = "buscar..."
            for (loja in lojasPrimeiraFiltros){
                if (loja.second is Lojas ){
                    (loja.second as Lojas ).ProdutosAbertos = false
                }
            }
            adapterRecyclerLojas.filtroBusca = false
            adapterRecyclerLojas.buscaAberta = false
            adapterRecyclerLojas.listaLojas.clear()
            adapterRecyclerLojas.listaLojas.addAll( lojasPrimeiraFiltros)
            adapterRecyclerLojas.notifyDataSetChanged()
        }

        buscaItens.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtro = s.toString()
                if (filtro.isEmpty()){
                    if(adapterRecyclerLojas != null){
                        adapterRecyclerLojas.listaLojas.clear()
                        adapterRecyclerLojas.listaLojas.addAll( lojasAux)
                        adapterRecyclerLojas.notifyDataSetChanged()
                    }

                }else{
                    val filteredList = lojasAux.filter { pair ->
                        if (pair.second is Produtos) {
                            (pair.second as Produtos).Nome.contains(filtro, ignoreCase = true) ||
                                    (pair.second as Produtos).Barra.contains(filtro, ignoreCase = true)
                        } else {
                            false
                        }
                    }.sortedBy { pair -> (pair.second as Produtos).lojaID }

                    val combinedList = combinarLojasEProdutos(lojasPrimeiraFiltros, filteredList)
                    CoroutineScope(Dispatchers.IO).launch {
                        lojasFiltroCombinado.clear()
                        lojasFiltroCombinado.addAll(combinedList)
                    }
                    adapterRecyclerLojas.listaLojas.clear()
                    adapterRecyclerLojas.listaLojas.add(0, Pair(3, "Lojas Disponíveis"))
                    adapterRecyclerLojas.listaLojas.addAll(combinedList)
                    adapterRecyclerLojas.notifyDataSetChanged()

                }
            }
        })
        CoroutineScope(Dispatchers.IO).launch{
            val tarefaBuscaLoja = async {
                val  taskConsultaLoja = TaskConsultaLoja()
                listaComparador = taskConsultaLoja.buscaLojaDisponivel(cnpjStr, reprsentanteID)
            }
            val tarefaBuscaProdutos = async {
                val taskBanners = TaskBanners()
                listaBanners = taskBanners.buscaBanners(reprsentanteID, cnpjStr)

            }
            tarefaBuscaProdutos.await()
            tarefaBuscaLoja.await()

            if(!listaComparador.isEmpty()){
                MainScope().launch {
                    lojasAux = loadLojas(listaComparador, listaCarrinho)
                    if(lojasAux.isEmpty()){
                        Alertas.alertaErro(this@ActLojaPadrao, "Nenhuma loja disponível", "Ops!"){
                            finish()
                        }
                    }else{
                        setupRecyclerView()
                        if (!listaCarrinho.isEmpty()) {
                            val lojaEncontrada = lojasPrimeiraFiltros.find {
                                (it.second) is Lojas && (it.second as Lojas).Loja_ID == listaCarrinho.firstOrNull()?.lojaId
                            }

                            if (lojaEncontrada != null) {
                                lojaSelecionada = lojaEncontrada.second as Lojas
                            }else{
                                lojaSelecionada = lojasAux.firstOrNull()?.second as Lojas
                            }
                            if (lojaEncontrada == null){
                                 Alertas.alertaErro(this@ActLojaPadrao, "Essa loja esta indisponível", "Ops!"){

                                 }
                            }else{
                                if(lojaSelecionada == null){
                                    listaCarrinho.clear()
                                    lojaSelecionada = lojasAux[0].second as Lojas
                                }else{
                                    lojaSelecionada.ProdutosAbertos = true

                                    for (carrinhoItem  in listaCarrinho) {
                                        val protudoItem =
                                            lojasAux.find { it.second is Produtos && (it.second as Produtos).Barra == carrinhoItem.barra }
                                        if (protudoItem != null) {
                                            (protudoItem.second as Produtos).quantidadeAdicionada = carrinhoItem.quantidade
                                            (protudoItem.second as Produtos).valorProdutoTotal = alterarItem(carrinhoItem.quantidade, (protudoItem.second as Produtos))
                                            inserirNoCarrinho(this@ActLojaPadrao,   (protudoItem.second as Produtos), lojaSelecionada, cotacao!!)
                                            withContext(Dispatchers.Main) {
                                                atualizaInfoTopo(lojaSelecionada.Loja_ID)
                                                atualizaTopo(lojaSelecionada.Loja_ID,   (protudoItem.second as Produtos))
                                            }
                                        }
                                    }
                                }
                                addItensItens(lojaSelecionada, 0)
                            }



                        }else{
                            lojaSelecionada = lojasAux.firstOrNull()?.second as Lojas
                        }

                        lojasAux.add( 0,Pair(3, "Lojas Disponíveis"))
                        mudaInfosLojaTopo()
                    }

                }
            }else{
                MainScope().launch {
                    carregandoLoja.isVisible = false
                   Alertas.alertaErro(this@ActLojaPadrao, "Nenhuma loja disponível", "ops!"){
                        finish()
                    }
                }
            }
        }
    }

    private suspend fun loadLojas(listaLojaComparador: ArrayList<LojaComparador>, listaCarrinhoItemCotacao: ArrayList<CarrinhoItemCotacao>): ArrayList<Pair<Int, Any>> {
        return withContext(Dispatchers.IO) {
            val daoLojas = DAOLojas(applicationContext)
            val daoHelper = DAOHelper(applicationContext)
            val db = daoHelper.writableDatabase
            daoLojas.select(db, lojasFiltroLista, lojasPrimeiraFiltros,cnpjStr, listaLojaComparador, operadorCotacao = operadorCotacao, listaCarrinhoItemCotacao, nomeOperador )

        }
    }
    suspend fun inserirNoCarrinho(context: Context, produto: Produtos,loja: Lojas, cotacao: Cotacao){
        val daoHelper = DAOHelper(context).writableDatabase
        val daoTBCarrinho = DAOCarrinho()
        if (cotacao != null){
            val daoCotacao = DAOCotacao()
            daoCotacao.insereCotacao(daoHelper, cotacao)
        }

        daoTBCarrinho.buscarItens(daoHelper,produto,loja, empresa.razaoSocial)

    }
    private fun setupRecyclerView() {
        adapterRecyclerLojas = AdapterRecyclerLojas( applicationContext,
            this,
            this,
            this,
             this, this,this,
            empresa,
            listaBanners,
            this,
            lojasPrimeiraFiltros,
            endereco = endereco,
            this,this, listaCarrinho)
        lojasPrimeiraFiltros.add( 0,Pair(3, "Lojas Disponíveis"))
        adapterRecyclerLojas.listaLojas.clear()
        adapterRecyclerLojas.listaLojas.addAll(lojasPrimeiraFiltros)
        recyLojas.layoutManager = LinearLayoutManager(this)
        recyLojas.adapter = adapterRecyclerLojas
         adapterFiltro = AdapterFiltrosLojas(lojasFiltroLista, this)

        filtroLojasRecy.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        filtroLojasRecy.adapter = adapterFiltro
    }

    override fun removeItens(lojaID: Int) {
        removerProdutosEspecificos(lojaID)
    }

    override fun addItensItens(loja: Lojas, position: Int) {
        val positionLoja = adapterRecyclerLojas.listaLojas.indexOfFirst { it.second is Lojas && (it.second as Lojas).Loja_ID == loja.Loja_ID }

        inserirProdutosEspecificos(loja, positionLoja)
        if(position == 0){
            MainScope().launch {
                scrollToLoja(loja)
            }

        }
    }

    fun removerProdutosEspecificos(lojaID: Int) {
        val itemsToRemove = adapterRecyclerLojas.listaLojas.filter { pair ->
            pair.second is Produtos && (pair.second as Produtos).lojaID == lojaID
        }
        adapterRecyclerLojas.listaLojas.removeAll(itemsToRemove)
        adapterRecyclerLojas.notifyDataSetChanged()
        CoroutineScope(Dispatchers.IO).launch {
            val hasProduto = adapterRecyclerLojas.listaLojas.any { it.second is Produtos }

            MainScope().launch {

                if(!hasProduto){
                    constrainsTopFixo.isVisible = false

                }
            }
        }

    }

    fun inserirProdutosEspecificos(loja:Lojas, position: Int) {
        var produtosFiltrados : List<Pair<Int, Any>>?
        if(!adapterRecyclerLojas.filtroBusca){
            produtosFiltrados = lojasAux.filter { pair ->
                pair.second is Produtos && (pair.second as Produtos).lojaID == loja.Loja_ID
            }
        }else{
            produtosFiltrados = lojasFiltroCombinado.filter { pair ->
                pair.second is Produtos && (pair.second as Produtos).lojaID == loja.Loja_ID
            }
            if (produtosFiltrados.isEmpty()){
                produtosFiltrados = lojasAux.filter { pair ->
                    pair.second is Produtos && (pair.second as Produtos).lojaID == loja.Loja_ID
                }
            }
        }


        adapterRecyclerLojas.listaLojas.addAll(position+1, produtosFiltrados)
        adapterRecyclerLojas.notifyDataSetChanged()
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
    fun combinarLojasEProdutos(lojas: List<Pair<Int, Any>>, produtos: List<Pair<Int, Any>>): ArrayList<Pair<Int, Any>> {
        val combinedList = ArrayList<Pair<Int, Any>>()

        for (loja in lojas) {
            if (loja.second is Lojas) {
                (loja.second as Lojas).ProdutosAbertos = true
                combinedList.add(loja)
            }

            if(loja.second is Lojas){
                val produtosDaLoja = produtos.filter { pair ->
                    pair.second is Produtos && (pair.second as Produtos).lojaID ==( loja.second as Lojas).Loja_ID
                }.sortedBy { pair -> (pair.second as Produtos).Nome }
                combinedList.addAll(produtosDaLoja)
            }



        }

        return combinedList
    }


    override fun trocaInforTopoProduto( lojaId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if(lojaId != lojaSelecionada.Loja_ID){
                for (i in lojasPrimeiraFiltros){
                    if(i.second is  Lojas){
                        if((i.second as Lojas).Loja_ID == lojaId && i.second is Lojas){
                            lojaSelecionada = (i.second as Lojas)
                            break
                        }
                    }

                }
                MainScope().launch {
                    mudaInfosLojaTopo()
                }
            }
        }
    }

    override fun scrollToLoja(lojas: Lojas) {
        CoroutineScope(Dispatchers.IO).launch {
            var position = 0
            var lojaPosition:Lojas? = null
            for ((positionLoja, loja) in adapterRecyclerLojas.listaLojas.withIndex()){
                if (loja.second is Lojas && (loja.second as Lojas).Loja_ID == lojas.Loja_ID){
                    position = positionLoja
                    lojaPosition = loja.second as Lojas
                    lojaSelecionada = lojaPosition
                    break
                }
            }
            withContext(Dispatchers.Main) {
                (recyLojas.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position, 0)
                filtroLojasRecy.isVisible = true
                constrainsTopFixo.isVisible = true
                setaFiltroDireita.isVisible = true
                setaFiltroEsquerda.isVisible = true


                lojaSelecionada = lojas
                if (!lojaPosition!!.ProdutosAbertos) {
                    lojaPosition.ProdutosAbertos = true

                    addItensItens(lojaPosition, position)
                    mudaInfosLojaTopo()
                }
            }
        }
    }

    class CustomSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        // Retorna o tempo mínimo necessário para tornar a rolagem instantânea
        override fun calculateTimeForScrolling(dx: Int): Int {
            return 500  // 0 ms para rolar instantaneamente
        }

        // Ainda permite ajuste de velocidade, mas como o tempo é zero, não afeta o scroller
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 0f
        }
    }

    override fun atualizaListaLojas(listaFiltroLojas: ArrayList<Pair<Int, Any>>) {
        var nomeFilto = ""
        var  listaLojasFiltro = ArrayList<Lojas>()
        if (listaFiltroLojas.isEmpty()){
            for (i in lojasPrimeiraFiltros){
                if (i.second is Lojas){
                    listaLojasFiltro.add((i.second as Lojas))
                }
            }
        }else{
            for (i in listaFiltroLojas){
                if (i.second is Lojas){
                    listaLojasFiltro.add((i.second as Lojas))
                }
            }
        }



        if (listaFiltroLojas.isEmpty()){
            adapterRecyclerLojas.listaLojas.clear()
            adapterRecyclerLojas.listaLojas.addAll(lojasPrimeiraFiltros)

            adapterRecyclerLojas.notifyDataSetChanged()
        }else{

            adapterRecyclerLojas.listaLojas.clear()
            adapterRecyclerLojas.listaLojas.add(0, Pair(3, "Lojas Disponíveis"))
            adapterRecyclerLojas.listaLojas.addAll(listaFiltroLojas)
            adapterRecyclerLojas.notifyDataSetChanged()
        }
        adapterFiltro?.listaLojasFiltros = listaLojasFiltro
        adapterFiltro?.notifyDataSetChanged()
        if (listaFiltroLojas.size ==1){
            nomeFilto = (listaFiltroLojas[0].second as Lojas).Nome
        }else{
            for(loja in listaFiltroLojas){
                nomeFilto += (loja.second as Lojas).Nome + ", "
            }
        }

        if(listaFiltroLojas.isEmpty()){
            adapterRecyclerLojas.nomeFiltros =  "Todas Lojas"

        }else{
            if(nomeFilto.length >= 15){
                MainScope().launch {
                    val nomeFormat = nomeFilto.substring(0, 15) + "..."
                    adapterRecyclerLojas.nomeFiltros = nomeFormat
                }

            }else{
                adapterRecyclerLojas.nomeFiltros = nomeFilto
            }
        }


    }
    override fun atualizaProdutosOperadores(operadorID: Int, lojaId: Int, operadorNome: String, marcaId: Int, valorMaximo: Double) {
        lifecycleScope.launch {
            buscaItensOpls(lojaId, operadorID, operadorNome, marcaId, valorMaximo)
        }
    }

    suspend fun removeItensParaFiltro(lojaID: Int, removeItensGeral: Boolean = false) {
        try {
            val itemsToRemove = adapterRecyclerLojas.listaLojas.filter { pair ->
                pair.second is Produtos && (pair.second as Produtos).lojaID == lojaID
            }

            withContext(Dispatchers.Default) {
                if (removeItensGeral) {
                    lojasAux.removeAll(itemsToRemove)
                }
                listaAuxOperadores.clear()
                listaAuxOperadores.addAll(adapterRecyclerLojas.listaLojas)
                listaAuxOperadores.removeAll(itemsToRemove)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun buscaItensOpls(lojaID: Int, operadorLogistico: Int, operadorNome: String, marcaId: Int, valorMaximo:Double) {
        lifecycleScope.launch(Dispatchers.IO) {
            val daoHelper = DAOHelper(applicationContext).writableDatabase
            val daoProduto = DAOProdutos()
            val daoCarrinho = DAOCarrinho()

            try {
                val listaProdutosOpls = daoProduto.buscaProdutosOPLs(daoHelper, lojaID, operadorLogistico, cnpjStr, marcaId, operadorNome,valorMaximo)

                val prosition = adapterRecyclerLojas.listaLojas.indexOfFirst { loja ->
                    loja.second is Lojas && (loja.second as Lojas).Loja_ID == lojaID
                }


                lojasAux.removeIf {
                    it.second is Produtos && (it.second as Produtos).lojaID == lojaID
                }
                lojasAux.addAll(listaProdutosOpls)

                if (prosition != -1) {
                    withContext(Dispatchers.Main) {
                        val produtosAntigos = adapterRecyclerLojas.listaLojas.filter { pair ->
                            pair.second is Produtos && (pair.second as Produtos).lojaID == lojaID
                        }

                        val startPosition = prosition + 1
                        val oldSize = produtosAntigos.size

                        // Removendo os produtos antigos
                        if (oldSize > 0) {
                            adapterRecyclerLojas.listaLojas.removeAll(produtosAntigos)
                            adapterRecyclerLojas.notifyItemRangeRemoved(startPosition, oldSize)
                        }

                        // Atualizar informações da loja selecionada
                        val lojaSelecionada = adapterRecyclerLojas.listaLojas[prosition].second as Lojas

                        lojaSelecionada.operadorIDSelecionado = operadorLogistico
                        lojaSelecionada.nomeOperadorSelecionado = operadorNome



                        val (valorTotal, totalSt) = daoCarrinho.buscaValorTotal(daoHelper, lojaSelecionada, cnpjStr)
                        lojaSelecionada.valorTotal = valorTotal
                        lojaSelecionada.totalSt = totalSt

                        // Adicionar novos produtos
                        adapterRecyclerLojas.listaLojas.addAll(startPosition, listaProdutosOpls)
                        adapterRecyclerLojas.notifyItemRangeInserted(startPosition, listaProdutosOpls.size)

                        // Atualizar o item específico do RecyclerView para refletir as mudanças
                        adapterRecyclerLojas.notifyItemChanged(prosition)

                        // Atualizações de UI
                        mudaInfosLojaTopo()
                        nomeOperadorSelecionado.text = operadorNome.lowercase()
                        Toast.makeText(applicationContext, "Produtos Filtrados", Toast.LENGTH_SHORT).show()

                        // Scroll para a posição da loja
                        recyLojas.scrollToPosition(prosition)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                daoHelper.close()
            }
        }
    }





    fun buscaItensFiltros(loja: Lojas, lojaID: Int) {
        val daoHelper = DAOHelper(applicationContext).writableDatabase
        val daoProdutos = DAOProdutos()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listaBarras = daoProdutos.filtraProdudos(daoHelper, loja.listaAtrinutos, lojaSelecionada.operadorIDSelecionado, lojaID = lojaID)
                val listaFiltroProdutos =  lojasAux
                    .filter {
                        it.second is Produtos &&
                                (it.second as Produtos).lojaID == lojaID &&
                                (it.second as Produtos).Barra in listaBarras
                    }
                    .distinctBy { (it.second as Produtos).Barra }
                listaFiltroProdutos.forEach {
                    if(it.second is Produtos){
                        (it.second as Produtos).valorMaximo = loja.ValorMaximo

                    }
                }

                val position = adapterRecyclerLojas.listaLojas.indexOfFirst {
                    it.second is Lojas && (it.second as Lojas).Loja_ID == lojaID
                }

                if (position != -1) {
                    withContext(Dispatchers.Main) {
                        val produtosAntigos = adapterRecyclerLojas.listaLojas.filter { pair ->
                            pair.second is Produtos && (pair.second as Produtos).lojaID == lojaID
                        }

                        val startPosition = position + 1
                        val oldSize = produtosAntigos.size

                        if (oldSize > 0) {
                            adapterRecyclerLojas.listaLojas.removeAll(produtosAntigos)
                            adapterRecyclerLojas.notifyItemRangeRemoved(startPosition, oldSize)
                        }

                        val lojaSelecionada = adapterRecyclerLojas.listaLojas[position].second as Lojas
                        lojaSelecionada.listaAtrinutos = loja.listaAtrinutos

                        adapterRecyclerLojas.listaLojas.addAll(startPosition, listaFiltroProdutos)
                        adapterRecyclerLojas.notifyItemRangeInserted(startPosition, listaFiltroProdutos.size)

                        recyLojas.scrollToPosition(position)
                        Toast.makeText(applicationContext, "Produtos Filtrados", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                daoHelper.close()
            }
        }
    }


    override fun atualizaFiltroAtribudos(loja: Lojas, LojaId: Int) {

        if(loja.listaAtrinutos.size > 0){
            CoroutineScope(Dispatchers.IO).launch {
                removeItensParaFiltro(LojaId)
                buscaItensFiltros(loja, lojaID = LojaId)
            }


        }else{
            lifecycleScope.launch {
                buscaItensOpls(LojaId, loja.operadorIDSelecionado, loja.nomeOperadorSelecionado, loja.marcaID, loja.ValorMaximo)
            }
        }
        MainScope().launch {
            mudaInfosLojaTopo()
        }
    }
     fun mudaInfosLojaTopo(){
         if (!isFinishing && !isDestroyed && lojaSelecionada != null) {
             if (lojaSelecionada.Nome.length >= 25){
                 val nomeLojaFormatado = lojaSelecionada.Nome.substring(0, 23) + "..."
                 nomeLoja.text = nomeLojaFormatado
             }else{
                 nomeLoja.text = lojaSelecionada.Nome
             }
             if (lojaSelecionada.totalSt > 0){
                 stTextTotal.isVisible = true
                 stTextTotal.text = "+ ST: ${FormatarTexto().formatarParaMoeda(lojaSelecionada.totalSt)}"
             }else{
                 stTextTotal.isVisible = false
             }
             if(lojaSelecionada.listaAtrinutos.size >0){
                 filtroContainer.isVisible = true
                 quantidaeFiltro.text = lojaSelecionada.listaAtrinutos.size.toString()
             }else{
                 filtroContainer.isVisible = false
             }
             constrainFiltros.isVisible = lojaSelecionada.isFiltro

             if(lojaSelecionada.valorTotal > 0){
                 val formataTexto = FormatarTexto()
                 val valorToralFormatador = formataTexto.formatarParaMoeda(lojaSelecionada.valorTotal)
                 tituloTortal.isVisible = true
                 totalValor.isVisible = true
                 totalValor.text = valorToralFormatador
             }else{
                 tituloTortal.isVisible = false
                 totalValor.isVisible = false
             }
             contrainsTopo.isVisible = true
             Glide.with(this@ActLojaPadrao).load(URLs.urlImagensLoja+"/"+lojaSelecionada.imagem).into(imgMarca)
             nomeOperadorSelecionado.text = if (lojaSelecionada.nomeOperadorSelecionado.isEmpty()) "Selecione" else lojaSelecionada.nomeOperadorSelecionado.toLowerCase()
             if(!recyLojas.isVisible){
                 carregandoLoja.isVisible = true
             }else{
                 carregandoLoja.isVisible = false

             }

         }
     }
    override  fun atualizaTopo(lojaId: Int, produtos: Produtos?, paraLopping: Boolean) {

            val daoHelper = DAOHelper(applicationContext).writableDatabase
            val daoCarrinho = DAOCarrinho()
            val loja = lojasPrimeiraFiltros.find { it.second is Lojas && (it.second as Lojas).Loja_ID == lojaId } as Pair<Int, Lojas>
            if(loja.second != lojaSelecionada){
                lojaSelecionada = loja.second
            }
            val  (valorTotal, valorTotalSt) = daoCarrinho.buscaValorTotal(daoHelper,loja.second, cnpjStr)
            for ((position,lojaProduto) in adapterRecyclerLojas.listaLojas.withIndex()){
                if(lojaProduto.second is Lojas && (lojaProduto.second as Lojas).Loja_ID == lojaId ){
                    lojaSelecionada.valorTotal = valorTotal
                    lojaSelecionada.totalSt = valorTotalSt
                    MainScope().launch {
                            adapterRecyclerLojas.notifyItemChanged(position)
                    }
                    if(!paraLopping){
                        break
                    }
                }
                if (paraLopping){
                    if(lojaProduto.second is Produtos && (lojaProduto.second as Produtos).lojaID == lojaId && (lojaProduto.second as Produtos).Barra == produtos?.Barra ){
                        (lojaProduto.second as Produtos).valorProdutoTotal = produtos!!.valorProdutoTotal
                        MainScope().launch {
                            adapterRecyclerLojas.notifyItemChanged(position)
                        }
                        break
                    }
                }


            }
            MainScope().launch {
                if(lojaSelecionada.valorTotal > 0){
                    val formataTexto = FormatarTexto()
                    val valorToralFormatador = formataTexto.formatarParaMoeda(lojaSelecionada.valorTotal)
                    tituloTortal.isVisible = true
                    totalValor.isVisible = true
                    totalValor.text = valorToralFormatador
                    nomeLoja.text = lojaSelecionada.Nome
                    nomeOperadorSelecionado.text = lojaSelecionada.nomeOperadorSelecionado.toLowerCase()
                     if(lojaSelecionada.totalSt > 0){
                         stTextTotal.text = "+ ST :${FormatarTexto().formatarParaMoeda(lojaSelecionada.totalSt)}"
                         stTextTotal.isVisible = true

                     }else{
                         stTextTotal.isVisible = false
                     }
                    try {
                        Glide.with(this@ActLojaPadrao).load(URLs.urlImagensLoja+"/"+lojaSelecionada.imagem).into(imgMarca)

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else{
                    tituloTortal.isVisible = false
                    totalValor.isVisible = false
                }

            }
    }

    override fun atualizaItensCarrinho() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch{
                MainScope().launch {
                    carregandoLoja.isVisible = true
                    constrainsTopFixo.isVisible = false
                    recyLojas.isVisible = false
                    filtroLojasRecy.isVisible = false
                    setaFiltroEsquerda.isVisible = false
                    setaFiltroDireita.isVisible = false

                }
                val tarefaBuscaLoja = async {
                    val  taskConsultaLoja = TaskConsultaLoja()
                    listaComparador.clear()
                    lojasAux.clear()
                    lojasPrimeiraFiltros.clear()
                    lojasFiltroCombinado.clear()
                    lojasFiltroLista.clear()
                    listaComparador = taskConsultaLoja.buscaLojaDisponivel(cnpjStr, reprsentanteID)
                }
                tarefaBuscaLoja.await()

               MainScope().launch {
                    lojasAux = loadLojas(listaComparador, listaCarrinho)

                    setupRecyclerView()
                    lojaSelecionada =  lojasAux.first { it.second is Lojas }.second as Lojas
                   if (lojaSelecionada != null){
                       mudaInfosLojaTopo()

                   }
                    recyLojas.isVisible = true
                    carregandoLoja.isVisible = false
                   if (data != null) {
                       val loja = data!!.getSerializableExtra("loja") as? Lojas
                       if (loja != null) {
                           scrollToLoja(loja)
                       }
                   }

               }
            }

        }
    }

    override fun atualizaInfoTopo(lojaid: Int) {
        val loja = lojasPrimeiraFiltros.find {
            it.second is Lojas && (it.second as Lojas).Loja_ID == lojaid
        } as? Pair<Int, Lojas>
        if(loja != null){
            lojaSelecionada = loja.second

            CoroutineScope(Dispatchers.Main).launch {
                if (lojaSelecionada.ProdutosAbertos){
                    mudaInfosLojaTopo()

                }
            }
        }else{
            contrainsTopo.isVisible = false
            constrainsTopFixo.isVisible = false
        }

    }
}
