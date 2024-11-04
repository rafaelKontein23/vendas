package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro.DadosPessoaisFragment
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes.Progress.Companion.animateProgressBarHorizontal
import br.com.visaogrupo.tudofarmarep.Utils.Views.Animacoes.rotateYView
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActCabecalhoBinding

class ActCabecalho : AppCompatActivity() {
    private  val binding: ActivityActCabecalhoBinding by lazy {
        ActivityActCabecalhoBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerCadastro, DadosPessoaisFragment())
                .addToBackStack(null)

                .commit()
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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