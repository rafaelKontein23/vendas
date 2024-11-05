package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosCnpjFragment
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes.Progress.Companion.animateProgressBarHorizontal
import br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes.rotateYView
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActCabecalhoBinding

class ActCabecalho : AppCompatActivity() {
    private  val binding: ActivityActCabecalhoBinding by lazy {
        ActivityActCabecalhoBinding.inflate(layoutInflater)
    }
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
            finish()
        }

        binding.progressCnpj.animateProgressBarHorizontal(1f, 1000)

        binding.verPrgressoImg.setOnClickListener {
            if (binding.contrainsProgressVertical.isVisible){
                binding.verPrgressoImg.rotateYView(0f)

            }else{
                binding.verPrgressoImg.rotateYView(180f)
            }
        }
        viewModelActCabecalho.passoAtual.observe(this){passo ->
            mudaProgresso(passo)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun mudaProgresso(progresso: Int){

       when (progresso){
          2 ->{
              binding.progressPessoal.animateProgressBarHorizontal(0.1f, 1000)
              binding.progressVerticalPessoal.setBackgroundResource(R.drawable.passo_azul)

           }
           3 ->{
               binding.progressDocumento.animateProgressBarHorizontal(0.1f, 1000)
               binding.progressVerticalDocumento.setBackgroundResource(R.drawable.passo_azul)
           }
           4 ->{
               binding.progressAreaAtuacao.animateProgressBarHorizontal(0.1f, 1000)
               binding.progressVerticalAtuacao.setBackgroundResource(R.drawable.passo_azul)
           }
           5 ->{
               binding.progressCodigoIndicacao.animateProgressBarHorizontal(0.1f, 1000)
               binding.progressVerticalIndicacao.setBackgroundResource(R.drawable.passo_azul)
           }
           6 ->{
               binding.progressContratoAceite.animateProgressBarHorizontal(0.1f, 1000)
               binding.progressVerticalContratoAceite.setBackgroundResource(R.drawable.passo_azul)
           }

       }
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}