package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory.ViewModelActCelularFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCelular
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.Strings
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.Views.isFocus
import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActCelularBinding

class ActCelular : AppCompatActivity() {
    private val binding by lazy {
       ActivityActCelularBinding.inflate(layoutInflater)
    }
    lateinit var  viewModelActCelular: ViewModelActCelular
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val factory = ViewModelActCelularFactory(applicationContext)
        viewModelActCelular = ViewModelProvider(this, factory)[ViewModelActCelular::class.java]

        FormataTextos.colocaMascaraInput(binding.inputCelular, Strings.mascaraTelefone)

        binding.inputCelular.setText(viewModelActCelular.recuperaCelular())

        viewModelActCelular.numeroTelefoneSuporte.observe(this) { numeroTelefoneSuporte ->
            binding.constrainCarregando.visibility = View.GONE
            if (numeroTelefoneSuporte.isEmpty()) {
                Toast.makeText(this, getString(R.string.erroSuporteWhats), Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(numeroTelefoneSuporte)
                startActivity(intent)
            }
        }

        binding.suporteLoiuConstrain.setOnClickListener {
            binding.constrainCarregando.visibility = View.VISIBLE
            viewModelActCelular.buscarNumeroTelefoneSuporte()
        }

        binding.inputCelular.isFocus(this)


        binding.btnContinuar.setOnClickListener {
            val celularCap = binding.inputCelular.text.toString()
            val codicaoCelular = celularCap.length < 14
            if (!codicaoCelular){
                viewModelActCelular.salvarCelular(celularCap)
                startActivity(Intent(this, ActToken::class.java))
            }else{
                binding.inputCelular.validaError(codicaoCelular, this@ActCelular)
            }
        }

        binding.setaVoltarImg.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}