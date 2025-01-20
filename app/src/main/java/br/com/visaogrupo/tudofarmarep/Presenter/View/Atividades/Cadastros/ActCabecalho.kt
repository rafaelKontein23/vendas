package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import FormularioCadastro
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosCnpjFragment
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosPessoaisFragment
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes.Progress.Companion.animateProgressBarHorizontal
import br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes.rotateYView
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.DAO.DAOCadastro
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActCabecalhoBinding


class ActCabecalho : AppCompatActivity() {
    private  val binding: ActivityActCabecalhoBinding by lazy {
        ActivityActCabecalhoBinding.inflate(layoutInflater)
    }
    private var  infoFragmentDadosPessoais : DadosPessoaisFragment? = null
    private var  infoFragmentDadosCnpjFragment : DadosCnpjFragment? = null

    private lateinit var viewModelActCabecalho: ViewModelActCabecalho

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerCadastro, DadosCnpjFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModelActCabecalho = ViewModelProvider(this)[ViewModelActCabecalho::class.java]

        viewModelActCabecalho.mostraCarregando.observe(this){mostraCarregando ->
            binding.constrainCarregando.isVisible = mostraCarregando
        }

        viewModelActCabecalho.finalizaAtividade.observe(this){
            finish()
        }

        binding.setaVoltar.setOnClickListener {
            voltaTela()

        }

        binding.progressCnpj.animateProgressBarHorizontal(1f, 1000)

        binding.verPrgressoImg.setOnClickListener {
            if (binding.contrainsProgressVertical.isVisible){
                binding.contrainsProgressVertical.isVisible = false
                binding.verPrgressoImg.rotateYView(0f)

            }else{
                binding.contrainsProgressVertical.isVisible = true
                binding.verPrgressoImg.rotateYView(180f)
            }
        }

        viewModelActCabecalho.passoAtual.observe(this){passo ->
            mudaProgresso(passo.first, passo.second)
        }

        viewModelActCabecalho.infoVisvelFragement.observe(this){
             infoFragmentDadosPessoais = it

        }
        viewModelActCabecalho.infoVisvelFragementCnpj.observe(this){
            infoFragmentDadosCnpjFragment = it

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    private fun mudaProgresso(progresso: Int, progress:Float){

       when (progresso){
          2 ->{
              binding.progressPessoal.animateProgressBarHorizontal(progress, 1000)
              binding.progressVerticalPessoal.isVisible = true
              binding.tituloCadastro.text = getString(R.string.DadosPessoal)

           }
           3 ->{
               binding.progressPessoal.animateProgressBarHorizontal(progress, 0)
               binding.progressDocumento.animateProgressBarHorizontal(progress, 1000)
               binding.progressVerticalPessoal.isVisible = true

               binding.progressVerticalDocumento.isVisible = true
               binding.tituloCadastro.text = getString(R.string.DadosDocumento)
           }
           4 ->{
               binding.progressPessoal.animateProgressBarHorizontal(progress, 0)
               binding.progressDocumento.animateProgressBarHorizontal(progress, 0)
               binding.progressAreaAtuacao.animateProgressBarHorizontal(progress, 1000)
               binding.progressVerticalPessoal.isVisible = true

               binding.progressVerticalDocumento.isVisible = true
               binding.progressVerticalAtuacao.isVisible = true
               binding.tituloCadastro.text = getString(R.string.DadosAreaAtuacao)
           }
           5 ->{
               binding.progressPessoal.animateProgressBarHorizontal(progress, 0)
               binding.progressDocumento.animateProgressBarHorizontal(progress, 0)
               binding.progressAreaAtuacao.animateProgressBarHorizontal(progress, 0)
               binding.progressCodigoIndicacao.animateProgressBarHorizontal(progress, 1000)
               binding.progressVerticalPessoal.isVisible = true

               binding.progressVerticalDocumento.isVisible = true
               binding.progressVerticalAtuacao.isVisible = true
               binding.progressVerticalIndicacao.isVisible = true
               binding.tituloCadastro.text = getString(R.string.CodigoIndicacao)
           }
           6 ->{
               binding.progressPessoal.animateProgressBarHorizontal(progress, 0)
               binding.progressDocumento.animateProgressBarHorizontal(progress, 0)
               binding.progressAreaAtuacao.animateProgressBarHorizontal(progress, 0)
               binding.progressCodigoIndicacao.animateProgressBarHorizontal(progress, 0)
               binding.progressContratoAceite.animateProgressBarHorizontal(progress, 1000)

               binding.progressVerticalPessoal.isVisible = true
               binding.progressVerticalDocumento.isVisible = true
               binding.progressVerticalAtuacao.isVisible = true
               binding.progressVerticalIndicacao.isVisible = true
               binding.progressVerticalContratoAceite.isVisible = true
               binding.tituloCadastro.text = getString(R.string.ContratoAceite)
           }

       }
    }

    override fun onBackPressed() {
        voltaTela()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null && infoFragmentDadosPessoais != null  || infoFragmentDadosCnpjFragment != null ) {
            if(infoFragmentDadosPessoais != null){
                if(infoFragmentDadosPessoais!!.isInfoVisible()){
                    infoFragmentDadosPessoais?.hideMenu()
                    infoFragmentDadosPessoais = null
                    return true
                }
            }
            if(infoFragmentDadosCnpjFragment != null){
                if(infoFragmentDadosCnpjFragment!!.isInfoVisible()){
                    infoFragmentDadosCnpjFragment?.hideMenu()
                    infoFragmentDadosCnpjFragment = null
                    return true
                }
            }

            return true
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onPause() {
        super.onPause()
        val dbHelper = DAOHelper(this).writableDatabase
        val daoCadastro = DAOCadastro()
        dbHelper.beginTransaction()
        val areaAtuacao = FormularioCadastro.cadastroRequestAreaAtuacal
        try {
            val fotoDocmento = FormularioCadastro.fotoDocumeto
            daoCadastro.inserirCadastro(dbHelper, FormularioCadastro.cadastro,fotoDocmento)
            if (areaAtuacao.UF != ""){
                daoCadastro.inserirCadastroAreaAtuacao(dbHelper, FormularioCadastro.cadastroRequestAreaAtuacal)
            }
            dbHelper.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()

        } finally {
            dbHelper.endTransaction()
            dbHelper.close()
        }
    }

    override fun onResume() {
        super.onResume()
        val dbHelper = DAOHelper(this).readableDatabase
        val daoCadastro = DAOCadastro()

        FormularioCadastro.cadastro = daoCadastro.recuperarCadastro(dbHelper) ?: FormularioCadastro.cadastro

        FormularioCadastro.fotoDocumeto = if(FormularioCadastro.fotoDocumeto != Uri.EMPTY) FormularioCadastro.fotoDocumeto else daoCadastro.recuperarFotos(dbHelper)
        FormularioCadastro.cadastroRequestAreaAtuacal = daoCadastro.recuperarCadastroAreaAtuacao(dbHelper) ?:FormularioCadastro.cadastroRequestAreaAtuacal

        dbHelper.close()
    }

    fun voltaTela(){
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
            val backStackCount = supportFragmentManager.backStackEntryCount
            mudaProgresso(backStackCount, 0f)
        } else {
            finish()
        }
    }

}