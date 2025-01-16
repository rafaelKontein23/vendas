package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home

import FormularioCadastro
import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaCargaProgresso
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProgress
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogCnpjs
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogMenuLateral
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogSuccesoCarteiraImportada
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosAreaDeAtuacaoFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosPessoaisFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.DadosBancariosFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.FragmentHome
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.FragmentWebViewVendaRemota
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.FragmentsWebView
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.Factory.ViewModelActHomeFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.ViewModelActHome
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.DataUltis
import br.com.visaogrupo.tudofarmarep.Utils.Enuns.EnumMenu
import br.com.visaogrupo.tudofarmarep.Utils.IntentUtils
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Requests
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActHomeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition


import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ActHome : AppCompatActivity(), AtualizaCargaProgresso, AtualizaProgress {

    private lateinit var viewModelActHome:ViewModelActHome
    private val fragmentHome by lazy {
        FragmentHome.newInstance(this, this)
    }
    private var inciaLoja = 12
    var context: ActHome? = null
    private val binding: ActivityActHomeBinding by lazy {
        ActivityActHomeBinding.inflate(layoutInflater)
    }
    private var animator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val factory = ViewModelActHomeFactory(applicationContext)
        viewModelActHome = ViewModelProvider(this, factory)[ViewModelActHome::class.java]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
        binding.textoCarga.isVisible = true
        supportFragmentManager.
        beginTransaction()
            .add(R.id.containerfragments, fragmentHome).commit()

        binding.homeLinear.setOnClickListener {
            inciaHome()
        }
        binding.notificao.setOnClickListener {
            val intent = Intent(this, ActNotificacao::class.java)
            startActivity(intent)
        }

        binding.pdv.setOnClickListener {
            val dialogcnpj = DialogCnpjs()
            dialogcnpj.dialogCnpjs(this,iniciaLoja = inciaLoja, atividade = this)
        }

        viewModelActHome.numeroTelefoneSuporte.observe(this){
            binding.constrainCarregando.isVisible = false

            if (it != null) {
                try {
                    IntentUtils.mandaParaWhatsApp(this@ActHome, it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Alertas.alertaErro(this, getString(R.string.erroSuporteWhats), getString(R.string.tituloErro)){}
                }
            }
        }

        binding.duvidas.setOnClickListener {
            binding.constrainCarregando.isVisible = true
            viewModelActHome.buscarNumeroTelefoneSuporte()
        }

        binding.vendaLinear.setOnClickListener {
            binding.tituloTopo.text= getString(R.string.vendas)
            desMarcaSelecionado(binding.textHome, binding.selecionadoHome, binding.iconeHome)
            marcaSelecionado(binding.textVendas, binding.selecionadovendas, binding.iconeVenda)
            desMarcaSelecionado(binding.textFinanceiro, binding.selecionadoFinanceiro, binding.iconefinanceiro)
            desMarcaSelecionado(binding.textRemoto, binding.selecionadoRemoto, binding.iconeRemoto)
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerfragments, FragmentWebViewVendaRemota()).addToBackStack(null)
                .commit()
        }

        binding.financeiroLinear.setOnClickListener {
            binding.tituloTopo.text= getString(R.string.financeiro)

            desMarcaSelecionado(binding.textHome, binding.selecionadoHome, binding.iconeHome)
            desMarcaSelecionado(binding.textVendas, binding.selecionadovendas, binding.iconeVenda)
            marcaSelecionado(binding.textFinanceiro, binding.selecionadoFinanceiro, binding.iconefinanceiro)
            desMarcaSelecionado(binding.textRemoto, binding.selecionadoRemoto, binding.iconeRemoto)
            binding.tituloTopo.text = this.getString(R.string.relatorios)
            val fragmentWebView = FragmentsWebView()
            val bundle = Bundle()
            val data = DataUltis.obtemDataAtual()
            bundle.putString(ProjetoStrings.urlweb, "${ProjetoStrings.relotorio}/${data}")
            fragmentWebView.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerfragments, fragmentWebView)
                .addToBackStack(null)
                .commit()
        }

        viewModelActHome.informacaoCnpjNome.observe(this){
            val nome = it.second
            val cnpj = it.first
            val dialogMenuLateral = DialogMenuLateral()
            dialogMenuLateral.dialogMenu(this@ActHome, nome, cnpj, viewModelActHome)
        }

        viewModelActHome.nomeId.observe(this){
            binding.tituloTopo.text = it.first
            when (it.second) {
                EnumMenu.HOME -> {
                    inciaHome()
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
                EnumMenu.VENDAS ->{
                    binding.tituloTopo.text= getString(R.string.vendas)
                    desMarcaSelecionado(binding.textHome, binding.selecionadoHome, binding.iconeHome)
                    marcaSelecionado(binding.textVendas, binding.selecionadovendas, binding.iconeVenda)
                    desMarcaSelecionado(binding.textFinanceiro, binding.selecionadoFinanceiro, binding.iconefinanceiro)
                    desMarcaSelecionado(binding.textRemoto, binding.selecionadoRemoto, binding.iconeRemoto)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerfragments, FragmentWebViewVendaRemota()).addToBackStack(null)
                        .commit()
                }

                EnumMenu.DADOSAREATUACAO -> {
                    val dadosAreaDeAtuacaoFragment = DadosAreaDeAtuacaoFragment.newInstance(false)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerfragments, dadosAreaDeAtuacaoFragment)
                        .addToBackStack(null)
                        .commit()
                }
                EnumMenu.INICIARVENDAS ->{
                    inciaLoja = 12
                    val dialogcnpj = DialogCnpjs()
                    dialogcnpj.dialogCnpjs(this,iniciaLoja = inciaLoja, atividade = this)
                }

                EnumMenu.CARGAS -> {
                    atualizaStatusCarga(1)
                    atualizaStatusCarga(1)
                    viewModelActHome.mudaStatusCarga(true, "")


                    val request = Requests()
                    request.corrotinasMarcas(this@ActHome, this@ActHome)
                }
                EnumMenu.REMOTO ->{
                    binding.constrainCarregando.isVisible = true

                    viewModelActHome.buscaLinkVendaremotas()
                }
            }
        }

        binding.menu.setOnClickListener {
                viewModelActHome.buscaNomeCnpj()
        }

        if(DialogSuccesoCarteiraImportada.abreDialogCarteira){
            DialogSuccesoCarteiraImportada.abreDialogCarteira = false
            val dialogcnpj = DialogCnpjs()
            dialogcnpj.dialogCnpjs(this, iniciaLoja = inciaLoja, atividade = this)
        }

        viewModelActHome.hashVendaRemota.observe(this){hash ->
            if(hash.isEmpty()){
                Toast.makeText(this, "Erro ao gerar link", Toast.LENGTH_SHORT).show()
            }else{
                binding.constrainCarregando.isVisible = false
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                val link = "${URLs.urlVendaRometa}${hash}"
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "${getString(R.string.fraseWhatsVendaRemota)}\n $link"
                )

                if (intent.resolveActivity(getPackageManager()) != null) {
                    val chooser = Intent.createChooser(
                        intent,
                        "Compartilhe seu link com seus clientes"
                    )
                    startActivity(chooser)
                }else {
                    Toast.makeText(
                        applicationContext,
                        "Nenhum aplicativo de compartilhamento encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
        viewModelActHome.mostraCarregando.observe(this){
            binding.constrainCarregando.isVisible = it
        }
        viewModelActHome.mostraMenssagem.observe(this){
            Alertas.alertaErro(this, it.second, getString(R.string.loiuInforma)){

            }
        }


        binding.remotoLinear.setOnClickListener {
            binding.constrainCarregando.isVisible = true

            viewModelActHome.buscaLinkVendaremotas()
        }

        viewModelActHome.cadastroProgresso.observe(this){
            atualizaStatusCarga(it)
        }
        viewModelActHome.urlNome.observe(this){
            binding.tituloTopo.text = it.first
            val fragmentWebView = FragmentsWebView()
            val bundle = Bundle()
            bundle.putString(ProjetoStrings.urlweb, it.second)
            fragmentWebView.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerfragments, fragmentWebView)
                .addToBackStack(null)
                .commit()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
   private fun atualizaStatusCarga(progresso: Int){
        MainScope().launch {
            binding.contrainsCargaInfo.isVisible = true
            when (progresso) {
                1 -> {
                    binding.imgCarga.setImageResource(R.drawable.atualizando)
                    animator = ObjectAnimator.ofFloat(binding.imgCarga, "rotation", 0f, 360f).apply {
                        duration = 2000
                        interpolator = LinearInterpolator()
                        repeatCount = ObjectAnimator.INFINITE
                        start()
                    }

                    binding. textoCarga.text = getString(R.string.AtualizandoCargas)
                    binding.textoCarga.setTextColor(getColor(R.color.gray500))
                    binding.contrainsCargaInfo.setBackgroundResource(R.drawable.carga_atualizando)
                }
                2 -> {
                    pararRotacaoETrocarIcone(R.drawable.sucesso)

                    binding.textoCarga.text = getString(R.string.CargaAtualizada)
                    binding.textoCarga.setTextColor(getColor(R.color.success600))
                    binding.contrainsCargaInfo.setBackgroundResource(R.drawable.carga_sucesso)
                    delay(3000)
                    binding.contrainsCargaInfo.isVisible = false
                }
                else -> {
                    pararRotacaoETrocarIcone(R.drawable.information)
                    binding.textoCarga.text = getString(R.string.ErroCarga)
                    binding.textoCarga.setTextColor(getColor(R.color.danger600))
                    binding. contrainsCargaInfo.setBackgroundResource(R.drawable.carga_finalizada_erro)
                    delay(3000)
                    binding.contrainsCargaInfo.isVisible = false
                    viewModelActHome.mudaStatusCarga(false, "")

                }
            }
        }
    }
    fun pararRotacaoETrocarIcone(novoIcone: Int) {
        animator?.cancel()
        binding.imgCarga.animate().cancel()
        binding.imgCarga.clearAnimation()
        binding.imgCarga.rotation = 0f
        binding.imgCarga.setImageResource(novoIcone)
    }

    override fun atualizaCargaProgresso(carga: Int) {
        atualizaStatusCarga(carga)
    }

    override fun escondeProgress(mostraCarregando: Boolean) {
        binding.constrainCarregando.isVisible = mostraCarregando
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri: Uri? = data?.getParcelableExtra("image_uri")
            if (imageUri != null){
                FormularioCadastro.fotoPerfil = imageUri
                viewModelActHome.mudaFotoPerfil()

            }
        }
    }

    private fun inciaHome(){
        binding.homeLinear.isEnabled = false
        val fragmentHomeItem = FragmentHome.newInstance(this, this)
        binding.tituloTopo.text = getString(R.string.Home)
        marcaSelecionado(binding.textHome, binding.selecionadoHome, binding.iconeHome)
        desMarcaSelecionado(binding.textVendas, binding.selecionadovendas, binding.iconeVenda)
        desMarcaSelecionado(binding.textFinanceiro, binding.selecionadoFinanceiro, binding.iconefinanceiro)
        desMarcaSelecionado(binding.textRemoto, binding.selecionadoRemoto, binding.iconeRemoto)
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerfragments, fragmentHomeItem)
            .addToBackStack(null)
            .commit()
        binding.homeLinear.isEnabled = true

    }
    fun marcaSelecionado(textoSelecionado:TextView, imgSelecionado:ImageView, imagem:ImageView){
        imgSelecionado.visibility = View.VISIBLE
        textoSelecionado.setTextColor(getColor(R.color.blue700))
        imagem.imageTintList = ContextCompat.getColorStateList(this, R.color.blue700)
    }
    fun desMarcaSelecionado(textoSelecionado:TextView, imgSelecionado:ImageView, imagem:ImageView){
        imgSelecionado.visibility = View.INVISIBLE
        textoSelecionado.setTextColor(getColor(R.color.gray600))
        imagem.imageTintList = ContextCompat.getColorStateList(this, R.color.gray600)
    }


}