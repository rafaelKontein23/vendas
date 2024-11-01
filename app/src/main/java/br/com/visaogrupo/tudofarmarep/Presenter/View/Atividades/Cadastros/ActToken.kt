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
import androidx.lifecycle.lifecycleScope
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory.ViewModelActTokenFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActToken
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.Strings
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.Cronometro
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraTelefone
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActTokenBinding
import kotlinx.coroutines.launch

class ActToken : AppCompatActivity() {
    private val binding by lazy {
           ActivityActTokenBinding.inflate(layoutInflater)
    }
    private lateinit var cronometro: Cronometro
    lateinit var viewModelActToken: ViewModelActToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val factory = ViewModelActTokenFactory(applicationContext)
        viewModelActToken = ViewModelProvider(this, factory)[ViewModelActToken::class.java]

        val numeroCelular = viewModelActToken.recuprarNumeroCelular() ?: ""
        binding.numeroCelular.text = numeroCelular.aplicarMascaraTelefone()

        viewModelActToken.repostaSolicita.observe(this){ respostaToken ->
            if(respostaToken == null){
                Alertas.alertaErro(this,Strings.erroSolicitaToken,Strings.tituloErro){
                    finish()
                }
            }else{
                cronometro = Cronometro(respostaToken.TempoTokenSegundos)
                cronometro.iniciar()

                lifecycleScope.launch {
                    cronometro.tempo.collect { tempoAtualizado ->
                        if (tempoAtualizado.equals("00:00")){
                            binding.naoRecebiTokenCronometro.text = "${Strings.naoRecebiToken}"
                            binding.naoRecebiTokenCronometro.isEnabled = true
                        }else{
                            binding.naoRecebiTokenCronometro.text = "${Strings.reenviarToken} $tempoAtualizado"
                            binding.naoRecebiTokenCronometro.isEnabled = false

                        }
                    }
                }

            }

        }


        viewModelActToken.solicitaToken(numeroCelular)

        viewModelActToken.numeroTelefoneSuporte.observe(this) { numeroTelefoneSuporte ->
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
            viewModelActToken.buscarNumeroTelefoneSuporte()
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