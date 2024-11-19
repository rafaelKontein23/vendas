package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory.ViewModelActSucessoCadastroFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActSucessoCadastro
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.IntentUtils
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActSucessoCadastroBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogAssinarContratoBinding

class ActSucessoCadastro : AppCompatActivity() {
    val binding: ActivityActSucessoCadastroBinding by lazy {
        ActivityActSucessoCadastroBinding.inflate(layoutInflater)
    }
    lateinit var viewModelActSucessoCadastro:ViewModelActSucessoCadastro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val factory = ViewModelActSucessoCadastroFactory(this)
        viewModelActSucessoCadastro = ViewModelProvider(this,factory)[ViewModelActSucessoCadastro::class.java]

        viewModelActSucessoCadastro.numeroTelefoneSuporte.observe(this){ numeroTelefoneSuporte ->
            binding.constrainCarregando.isVisible = false
            if (numeroTelefoneSuporte.isEmpty()) {
                Toast.makeText(this, getString(R.string.erroSuporteWhats), Toast.LENGTH_LONG).show()
            } else {
                IntentUtils.mandaParaWhatsApp(this, numeroTelefoneSuporte)
            }
        }

        binding.faleConosco.setOnClickListener {
            binding.constrainCarregando.isVisible = true
            viewModelActSucessoCadastro.buscarNumeroTelefoneSuporte()

        }
        binding.btnFinalizar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}