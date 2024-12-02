package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaCargaProgresso
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProgress
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaWebView
import br.com.visaogrupo.tudofarmarep.Controlers.ControllerActHome
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home.interfaces.AtualizaFragmentHome
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogCnpjs
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogMenuLateral
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogSuccesoCarteiraImportada
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosAreaDeAtuacaoFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosPessoaisFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.DadosBancariosFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.FragmentHome
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.FragmentsWebView
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.Factory.ViewModelActHomeFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.ViewModelActHome
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.SuporteTelefoneReposytory
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.TaskConstroiHash
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.Enuns.EnumMenu
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Requests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ActHome : AppCompatActivity(), AtualizaCargaProgresso, AtualizaProgress, AtualizaWebView, AtualizaFragmentHome {
    private lateinit var pdv :ImageView
    private lateinit var remotoLinear:LinearLayout
    private lateinit var menu:ImageView
    private lateinit var homeLinear:LinearLayout
    private lateinit var containerfragments : FragmentContainerView
    private lateinit var vendaLinear:LinearLayout
    private lateinit var viewModelActHome:ViewModelActHome
    private val fragmentHome by lazy {
        FragmentHome.newInstance(this, this)
    }
    private lateinit var duvidas:ImageView
    lateinit var  contrainsCargaInfo:ConstraintLayout
    lateinit var  textoCarga : TextView
    lateinit var imgCarga:ImageView
    private var inciaLoja = 12
    var context: ActHome? = null


    private lateinit var financeiroLinear :LinearLayout
    lateinit var constrainCarregando: ConstraintLayout
    lateinit var tituloTopo:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_act_home)
        pdv = findViewById(R.id.pdv)
        remotoLinear = findViewById(R.id.remotoLinear)
        menu = findViewById(R.id.menu)
        containerfragments = findViewById(R.id.containerfragments)
        vendaLinear = findViewById(R.id.vendaLinear)
        financeiroLinear = findViewById(R.id.financeiroLinear)
        contrainsCargaInfo = findViewById(R.id.contrainsCargaInfo)
        textoCarga = findViewById(R.id.textoCarga)
        imgCarga = findViewById(R.id.imgCarga)
        duvidas = findViewById(R.id.duvidas)
        constrainCarregando = findViewById(R.id.constrainCarregando)
        tituloTopo = findViewById(R.id.tituloTopo)
        homeLinear = findViewById(R.id.homeLinear)
        val factory = ViewModelActHomeFactory(applicationContext)
        textoCarga.isVisible = true
       // viewModelActHome = ViewModelProvider(this, factory)[factory, ViewModelActHome::class.java]

        context = this
        homeLinear.setOnClickListener {
            homeLinear.isEnabled = false
            val fragmentHomeItem = FragmentHome.newInstance(this, this)
            tituloTopo.text = "Home"
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerfragments, fragmentHomeItem)
                .commit()
            homeLinear.isEnabled = true
        }
        supportFragmentManager.
        beginTransaction()
            .add(R.id.containerfragments, fragmentHome).commit()

        pdv.setOnClickListener {
            val dialogcnpj = DialogCnpjs()
            dialogcnpj.dialogCnpjs(this,iniciaLoja = inciaLoja, atividade = this)
        }
        duvidas.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch{
                val suporteTelefoneReposytory = SuporteTelefoneReposytory(context!!)
                val resposta = suporteTelefoneReposytory.buscarNumeroTelefoneSuporte()
                MainScope().launch {
                    if (resposta != null) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            val url = "https://api.whatsapp.com/send?phone=${resposta.ContatoWhatsApp}"
                            intent.data = android.net.Uri.parse(url)
                            startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()

                        }
                    }
                }
            }
        }
        vendaLinear.setOnClickListener {
            val fragmentWebView = FragmentsWebView()

            tituloTopo.text= "Vendas"

            val bundle = Bundle()
            bundle.putString("urlweb", "/dashboard/vendas")
            fragmentWebView.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.containerfragments, fragmentWebView)
                .commit()
        }

        financeiroLinear.setOnClickListener {
            tituloTopo.text= "Financeiro"
            val fragmentWebView = FragmentsWebView()

            val bundle = Bundle()
            bundle.putString("urlweb", "/Dashboard/Comissao")
            fragmentWebView.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.containerfragments, fragmentWebView)
                .commit()
        }

        menu.setOnClickListener {
                val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val cnpj = prefs.getString(ProjetoStrings.cnpjLogin, "-")
                val nome = prefs.getString(ProjetoStrings.nomeCompleto, "-")
                val dialogMenuLateral = DialogMenuLateral()
                dialogMenuLateral.dialogMenu(this@ActHome, this@ActHome, nome!!, cnpj!!, this)
        }
        if(DialogSuccesoCarteiraImportada.abreDialogCarteira){
            DialogSuccesoCarteiraImportada.abreDialogCarteira = false
            val dialogcnpj = DialogCnpjs()
            dialogcnpj.dialogCnpjs(this, iniciaLoja = inciaLoja, atividade = this)
        }
        remotoLinear.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val taskControihash = TaskConstroiHash()
                    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

                    val reprsentanteID = prefs.getInt(ProjetoStrings.reprenteID, 0)
                    val  hash =  taskControihash.constroiHash(reprsentanteID)
                    withContext(Dispatchers.Main) {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.setType("text/plain")
                        val link = "${URLs.urlVendaRometa}${hash}"
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Olá, o link para sua venda remota está disponivel!\n $link"
                        )
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            val chooser = Intent.createChooser(
                                intent,
                                "Compartilhar"
                            )
                            startActivity(chooser)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Nenhum aplicativo de compartilhamento encontrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private var animator: ObjectAnimator? = null // Guardar a referência da animação

    override fun atualizaCargaProgresso(carga: Int) {
        fun pararRotacaoETrocarIcone(novoIcone: Int) {
            animator?.cancel()
            imgCarga.animate().cancel()
            imgCarga.clearAnimation()
            imgCarga.rotation = 0f
            imgCarga.setImageResource(novoIcone)
        }

        MainScope().launch {
            contrainsCargaInfo.isVisible = true
            when (carga) {
                1 -> {
                    imgCarga.setImageResource(R.drawable.atualizando)
                    animator = ObjectAnimator.ofFloat(imgCarga, "rotation", 0f, 360f).apply {
                        duration = 2000
                        interpolator = LinearInterpolator()
                        repeatCount = ObjectAnimator.INFINITE
                        start()
                    }

                    textoCarga.text = "Atualizando Cargas..."
                    textoCarga.setTextColor(resources.getColor(R.color.gray500))
                    contrainsCargaInfo.setBackgroundResource(R.drawable.carga_atualizando)
                }
                2 -> {
                    pararRotacaoETrocarIcone(R.drawable.sucesso)

                    textoCarga.text = "Cargas Atualizadas"
                    textoCarga.setTextColor(resources.getColor(R.color.success600))
                    contrainsCargaInfo.setBackgroundResource(R.drawable.carga_sucesso)
                    delay(3000)
                    contrainsCargaInfo.isVisible = false
                }
                else -> {
                    pararRotacaoETrocarIcone(R.drawable.information)

                    textoCarga.text = "Erro ao Gerar Cargas, toque aqui pra tentar novamente"
                    textoCarga.setTextColor(resources.getColor(R.color.danger600))
                    contrainsCargaInfo.setBackgroundResource(R.drawable.carga_finalizada_erro)

                    textoCarga.setOnClickListener {
                        atualizaCargaProgresso(1)

                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@ActHome)
                        val editor = prefs.edit()
                        editor.putBoolean("fazendoCarga", true)
                        editor.putString("DataCarga", "")
                        editor.apply()
                        val request = Requests()
                        request.corrotinasMarcas(this@ActHome, this@ActHome)
                    }

                }
            }
        }
    }

    override fun escondeProgress(mostraCarregando: Boolean) {
        constrainCarregando.isVisible = mostraCarregando
    }

    override fun atualizaWebView(nome: String, url: String) {
        tituloTopo.text= nome
        val fragmentWebView = FragmentsWebView()

        val bundle = Bundle()
        bundle.putString("urlweb", url)
        fragmentWebView.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerfragments, fragmentWebView)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        homeLinear.isEnabled = false
        val fragmentHomeItem = FragmentHome.newInstance(this, this)
        tituloTopo.text= "Home"
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerfragments, fragmentHomeItem).commit()
        homeLinear.isEnabled = true


    }

    override fun atualizaWebView(nome: String, id: EnumMenu) {
        tituloTopo.text = nome
        when (id) {
            EnumMenu.HOME -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerfragments, fragmentHome)
                    .commit()
            }
            EnumMenu.DADOSCADASTRAIS -> {
                val dadosPessoaisFragment = DadosPessoaisFragment.newInstance(false)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerfragments, dadosPessoaisFragment)
                    .addToBackStack(null)
                    .commit()
            }
            EnumMenu.DADOSBANCARIOS -> {
                val dadosBancariosFragment = DadosBancariosFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerfragments, dadosBancariosFragment)
                    .addToBackStack(null)
                    .commit()
            }
            EnumMenu.DADOSAREATUACAO -> {
                val dadosAreaDeAtuacaoFragment = DadosAreaDeAtuacaoFragment.newInstance(false)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerfragments, dadosAreaDeAtuacaoFragment)
                    .addToBackStack(null)
                    .commit()
            }
            else -> {
                Log.w("Navigation", "Opção de menu desconhecida: $id")
            }
        }

    }
}