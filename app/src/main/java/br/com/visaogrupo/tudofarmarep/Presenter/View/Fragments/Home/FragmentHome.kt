package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterBanners
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterCotacao
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterFragmentItens
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterPedidoPendentes
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaCargaProgresso
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaMesResumo
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProgress
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.RecuperasDatas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.VerificaInternet
import br.com.visaogrupo.tudofarmarep.Controlers.ControlerFragmentHome
import br.com.visaogrupo.tudofarmarep.Objetos.Banners
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoAbertos
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogSeledorData
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.util.Timer
import java.util.TimerTask

private lateinit var  carroselBaner:ViewPager2;
private lateinit var  carrosselResumoGanhos:ViewPager2;
private lateinit var  bolinhaPrimiraResumo:ConstraintLayout;
private lateinit var  bolinhaSegundaResumo:ConstraintLayout;
private lateinit var viewPagerGraficos:ViewPager2
private lateinit var bolinhaPrimiraGrafico:ConstraintLayout
private lateinit var bolinhaSegundaGrafico:ConstraintLayout

private lateinit var graficoText:TextView
private lateinit var verMaisPedidoPendentes:TextView
private lateinit var recyclerViewPedidosPendentes:RecyclerView
private lateinit var indicatorLayout:LinearLayout
private lateinit var imgVisualizarResulmo:ImageView
private lateinit var fragmentHome:FrameLayout
private lateinit var pedidoText:TextView
private lateinit var containerPedidosPendentes:ConstraintLayout
lateinit var contrainsBaners:ConstraintLayout
lateinit var  adapterPedidoPendentes :AdapterPedidoPendentes
var isVerMaisPedidoPendentes = false
lateinit  var fragmentResumoPedidosAtendidos :FragmentResumoPedidosAtendidos
lateinit var fragmentResumoComissao : FragmentResumoComissao
lateinit var constraisCotacao :ConstraintLayout
lateinit var recyCotacao:RecyclerView
lateinit var fragmentGraficosHome : FragmentGraficosHome


var reprsentanteID = 0
private var listaBanners:ArrayList<Banners> = ArrayList()

private var currentPage = 0
var tempo:Timer? = null
var contextHome: Context? = null
private var autoScrollTask: TimerTask? = null
var contextThis: FragmentHome? =null
private var inciaLoja = 12



class FragmentHome : Fragment(), AtualizaMesResumo{

    private var atualizaCargaProgresso: AtualizaCargaProgresso? = null
    private var atualizaProgres: AtualizaProgress? = null
    companion object {
        var visualizarRsulmo = false

        fun newInstance(
            atualizaCargaProgresso: AtualizaCargaProgresso,
            atualizaProgress: AtualizaProgress
        ): FragmentHome {
            return FragmentHome().apply {
                this.atualizaCargaProgresso = atualizaCargaProgresso
                this.atualizaProgres = atualizaProgress
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        carroselBaner = view.findViewById(R.id.CarroselBaner)
        carrosselResumoGanhos = view.findViewById(R.id.carrosselResumoGanhos)
        bolinhaPrimiraResumo = view.findViewById(R.id.bolinhaPrimiraResumo)
        bolinhaSegundaResumo = view.findViewById(R.id.bolinhaSegundaResumo)
        viewPagerGraficos = view.findViewById(R.id.viewPagerGraficos)
        bolinhaPrimiraGrafico = view.findViewById(R.id.bolinhaPrimiraGrafico)
        bolinhaSegundaGrafico = view.findViewById(R.id.bolinhaSegundaGrafico)
        graficoText = view.findViewById(R.id.graficoText)
        verMaisPedidoPendentes = view.findViewById(R.id.verMaisPedidoPendentes)
        recyclerViewPedidosPendentes = view.findViewById(R.id.RecyclerViewPedidosPendentes)
        indicatorLayout = view.findViewById(R.id.indicatorLayout)
        imgVisualizarResulmo = view.findViewById(R.id.imgVisualizarResulmo)
        containerPedidosPendentes = view.findViewById(R.id.containerPedidosPendentes)
        contrainsBaners = view.findViewById(R.id.contrainsBaners)
        constraisCotacao = view.findViewById(R.id.constraisCotacao)
        recyCotacao = view.findViewById(R.id.recyCotacao)

        fragmentHome = view.findViewById(R.id.fragmentHome)
        pedidoText  = view.findViewById(R.id.pedidoText)
        contextHome = requireContext()
        contextThis = this


        if(isAdded){
            try {
                if (contextThis != null && contextHome != null && atualizaCargaProgresso != null) {
                    buscaInfosIniciais(contextThis!!)
                } else {
                    Log.e("erro", "Contexto é null")
                }
            } catch (e: Exception) {
                Log.e("erro", e.message.toString())
            }

        }


        val prefs = PreferenceManager.getDefaultSharedPreferences(contextHome!!)
         reprsentanteID = prefs.getInt("reprsentante_id", 0)
        pedidoText.setOnClickListener {
           val dialogData = DialogSeledorData()
            dialogData.SeletorData(contextHome!!, contextThis!!)

        }
        return view
    }
    private fun setupIndicator(context: Context) {
        val indicators = arrayOfNulls<ImageView>(listaBanners.size)
        indicatorLayout.removeAllViews()

        val layoutParams = LinearLayout.LayoutParams(
            20, 20
        )

        layoutParams.setMargins(8, 0, 8, 0)
        layoutParams.gravity = Gravity.CENTER
        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i]!!.setImageResource(R.drawable.indicador_deselecionado)
            indicators[i]!!.layoutParams = layoutParams
            indicatorLayout.addView(indicators[i])
        }
    }

    private fun setCurrentPageIndicator(position: Int) {
        currentPage = position
        val childCount: Int = indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorLayout.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageResource(R.drawable.indicador_selecionado)
            } else {
                imageView.setImageResource(R.drawable.indicador_deselecionado)
            }
        }
    }
    class AutoScrollTask : TimerTask() {
        override fun run() {
            CoroutineScope(Dispatchers.Main).launch {

                if (currentPage == listaBanners.size - 1) {
                    currentPage = 0
                } else {
                    currentPage++
                }
                carroselBaner.setCurrentItem(currentPage, true)
            }
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        if (tempo != null) {
            tempo!!.cancel()

        }
    }
    fun onBackPressed() {
        activity?.onBackPressed()
        if (tempo != null) {
            tempo!!.cancel()
            tempo = null
        }
    }

    fun buscaInfosIniciais(context: FragmentHome){
        CoroutineScope(Dispatchers.IO).launch {
            MainScope().launch {
                atualizaProgres?.escondeProgress(true)

            }

            val controlerFragmentHome = ControlerFragmentHome()


            if (!VerificaInternet.isOnline(contextHome!!)){
                withContext(Dispatchers.Main){
                    atualizaProgres?.escondeProgress(false)
                    Alertas.alertaErro(contextHome!!, "Verifique sua conexão com a internet e tente novamente.", "Ops!"){
                        val  intent = Intent(contextHome!!, MainActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }else{
                val listaHome =  controlerFragmentHome.buscaInfosIniciais(contextHome!!)
                val tarefaCarga = launch {
                    controlerFragmentHome.verificaAtualizacaoCarga(contextHome!!, atualizaCargaProgresso!!)
                }
                tarefaCarga.join()

                listaBanners = listaHome.banners

                MainScope().launch {
                    atualizaProgres?.escondeProgress(false)
                    if (!listaBanners.isEmpty()){
                        val adapterBanners = AdapterBanners(listaBanners)
                        carroselBaner.adapter = adapterBanners
                        tempo?.cancel()
                        tempo = null
                        autoScrollTask?.cancel()
                        autoScrollTask = null
                        tempo = Timer()
                        autoScrollTask = AutoScrollTask()
                        tempo?.schedule(autoScrollTask, 5000, 5000)


                        setupIndicator(contextHome!!)
                        setCurrentPageIndicator(currentPage)
                    }else{
                        contrainsBaners.isVisible = false
                    }

                    if (listaHome.listaCotacao.isEmpty()){
                        constraisCotacao.isVisible = false
                    }else{
                        val adapterCotacao = AdapterCotacao(listaHome.listaCotacao, representanteId =  reprsentanteID, atualizaProgres!!,requireActivity(), inciaLoja)
                        recyCotacao.adapter = adapterCotacao
                        recyCotacao.layoutManager = LinearLayoutManager(contextHome!!)
                    }
                    if (isAdded && !isDetached && view != null && activity != null) {
                        val adapterFragmentItens = AdapterFragmentItens(childFragmentManager, viewLifecycleOwner.lifecycle)

                        fragmentResumoPedidosAtendidos = FragmentResumoPedidosAtendidos.newInstance(listaHome.resumoMes)
                        fragmentResumoComissao = FragmentResumoComissao.newInstance(listaHome.resumoMes)

                        adapterFragmentItens.addFragment(fragmentResumoPedidosAtendidos)
                        adapterFragmentItens.addFragment(fragmentResumoComissao)

                        carrosselResumoGanhos.adapter = adapterFragmentItens


                        val adapterGraficos = AdapterFragmentItens(childFragmentManager, viewLifecycleOwner.lifecycle)
                        fragmentGraficosHome= FragmentGraficosHome.newInstance(listaHome.graficosHome)
                        fragmentGraficosHome.listaGraficos = listaHome.graficosHome
                        adapterGraficos.addFragment(fragmentGraficosHome)
                        viewPagerGraficos.adapter = adapterGraficos
                    }

                    //  adapterGraficos.addFragment(FragmentGraficoHomeComissao())

                    var  listaPedidoInicial = ArrayList<CarrinhoAbertos>()
                    if (listaHome.pedidosPendentes.isEmpty()){
                        containerPedidosPendentes.isVisible = false
                    }else{
                        if (listaHome.pedidosPendentes.size >5 ){
                            listaPedidoInicial .addAll(listaHome.pedidosPendentes.subList(0,5) )
                            verMaisPedidoPendentes.isVisible = true
                            adapterPedidoPendentes = AdapterPedidoPendentes(listaPedidoInicial, requireActivity(), inciaLoja)
                            val linearLayoutManager = LinearLayoutManager(contextHome!!)
                            recyclerViewPedidosPendentes.layoutManager = linearLayoutManager
                            recyclerViewPedidosPendentes.adapter = adapterPedidoPendentes
                        }else{
                            verMaisPedidoPendentes.isVisible = false
                            adapterPedidoPendentes = AdapterPedidoPendentes(listaHome.pedidosPendentes, requireActivity(), inciaLoja)
                            val linearLayoutManager = LinearLayoutManager(contextHome!!)
                            recyclerViewPedidosPendentes.layoutManager = linearLayoutManager
                            recyclerViewPedidosPendentes.adapter = adapterPedidoPendentes
                        }

                        verMaisPedidoPendentes.setOnClickListener {
                            if (!isVerMaisPedidoPendentes){
                                verMaisPedidoPendentes.text = "Ver Menos"
                                adapterPedidoPendentes.listaPedidoPendentes = listaHome.pedidosPendentes
                                adapterPedidoPendentes.notifyDataSetChanged()
                            }else{
                                verMaisPedidoPendentes.text = "Ver Mais"
                                adapterPedidoPendentes.listaPedidoPendentes = listaPedidoInicial
                                adapterPedidoPendentes.notifyDataSetChanged()
                            }
                            isVerMaisPedidoPendentes = !isVerMaisPedidoPendentes

                        }
                    }

                    pedidoText.text = "Resumo de ${RecuperasDatas.recuperaMesAtual()} de ${RecuperasDatas.recuperaAnoAtual()}"

                    carroselBaner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            if (isAdded){
                                setCurrentPageIndicator(position)

                            }
                        }
                    })

                    carrosselResumoGanhos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            if (isAdded){
                                bolinhaPrimiraResumo.backgroundTintList = null
                                bolinhaSegundaResumo.backgroundTintList = null
                                if (position == 0) {
                                    bolinhaPrimiraResumo.backgroundTintList = resources.getColorStateList(R.color.blue300)
                                    bolinhaSegundaResumo.backgroundTintList = resources.getColorStateList(R.color.gray200)
                                } else {
                                    bolinhaPrimiraResumo.backgroundTintList = resources.getColorStateList(R.color.gray200)
                                    bolinhaSegundaResumo.backgroundTintList = resources.getColorStateList(R.color.blue300)
                                }
                            }

                        }
                    })

                    viewPagerGraficos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            if (isAdded){
                                bolinhaPrimiraGrafico.backgroundTintList = null
                                bolinhaSegundaGrafico.backgroundTintList = null
                                if (position == 0) {
                                    bolinhaPrimiraGrafico.backgroundTintList = resources.getColorStateList(R.color.blue300)
                                    bolinhaSegundaGrafico.backgroundTintList = resources.getColorStateList(R.color.gray200)
                                } else {
                                    bolinhaPrimiraGrafico.backgroundTintList = resources.getColorStateList(R.color.gray200)
                                    bolinhaSegundaGrafico.backgroundTintList = resources.getColorStateList(R.color.blue300)
                                }
                            }

                        }
                    })

                    imgVisualizarResulmo.setOnClickListener {
                        if (isAdded){
                            if (visualizarRsulmo) {
                                visualizarRsulmo = false
                                fragmentResumoComissao.atualizaVisualizacaoDeGanhos(visualizarRsulmo)
                                fragmentResumoPedidosAtendidos.atualizaVisualizacaoDeGanhos(visualizarRsulmo)
                                imgVisualizarResulmo.setImageResource(R.drawable.sem_visu)

                            }else{
                                visualizarRsulmo = true
                                fragmentResumoComissao.atualizaVisualizacaoDeGanhos(true)
                                fragmentResumoPedidosAtendidos.atualizaVisualizacaoDeGanhos(true)
                                imgVisualizarResulmo.setImageResource(R.drawable.visualiza)
                            }
                        }

                    }
                }
            }
        }
    }



    override  fun atualizaMesResumo(dataSelecionada: String, mes:String, ano:String) {
        CoroutineScope(Dispatchers.IO).launch{
            val contoler = ControlerFragmentHome()

            val listaMesSlecionado = contoler.buscaResumoMes(dataSelecionada,contextHome!!)
            MainScope().launch {
                fragmentResumoPedidosAtendidos.listaResumoPedidosAtendidos = listaMesSlecionado
                fragmentResumoComissao.listaResumoPedidosAtendidos = listaMesSlecionado
                fragmentResumoPedidosAtendidos.atualizaVisualizacaoDeGanhos(FragmentHome.visualizarRsulmo)
                fragmentResumoComissao.atualizaVisualizacaoDeGanhos(FragmentHome.visualizarRsulmo)
                pedidoText.text = "Resumo de $mes $ano"
                Toast.makeText(contextHome!!, "Dados atualizados", Toast.LENGTH_SHORT).show()
            }
        }
    }
}