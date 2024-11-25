package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home.ActHome
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory.ViewModelActTokenFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActToken
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.IntentUtils
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
    private lateinit var viewModelActToken: ViewModelActToken
    private var numeroCelular = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val factory = ViewModelActTokenFactory(applicationContext)
        viewModelActToken = ViewModelProvider(this, factory)[ViewModelActToken::class.java]

        viewModelActToken.recuperarNumeroCelular()

        viewModelActToken.numeroCelular.observe(this){numeroCelular ->
            binding.numeroCelular.text = numeroCelular.aplicarMascaraTelefone()
            this.numeroCelular = numeroCelular
            binding.constrainCarregando.isVisible = true
            viewModelActToken.solicitaToken(numeroCelular)
        }



        viewModelActToken.repostaSolicita.observe(this){ respostaToken ->
            binding.constrainCarregando.isVisible = false
            if(respostaToken == null){
                Alertas.alertaErro(this,getString(R.string.erroSolicitaToken),getString(R.string.tituloErro)){
                    finish()
                }
            }else{
                cronometro = Cronometro(respostaToken.TempoTokenSegundos)
                cronometro.iniciar()
                Alertas.alertaAviso(this,getString(R.string.tokenSolicitadoComSucesso)){

                }
                lifecycleScope.launch {
                    cronometro.tempo.collect { tempoAtualizado ->
                        if (tempoAtualizado == "00:00"){
                            binding.naoRecebiTokenCronometro.text = getString(R.string.naoRecebiToken)
                            binding.naoRecebiTokenCronometro.isEnabled = true
                        }else{
                            binding.naoRecebiTokenCronometro.setText( "${ProjetoStrings.reenviarToken} $tempoAtualizado")
                            binding.naoRecebiTokenCronometro.isEnabled = false

                        }
                    }
                }

            }

        }

        binding.naoRecebiTokenCronometro.setOnClickListener {
            binding.constrainCarregando.isVisible = true
            viewModelActToken.solicitaToken(numeroCelular)
        }


        viewModelActToken.numeroTelefoneSuporte.observe(this) { numeroTelefoneSuporte ->
            binding.constrainCarregando.visibility = View.GONE
            if (numeroTelefoneSuporte.isEmpty()) {
                Toast.makeText(this, getString(R.string.erroSuporteWhats), Toast.LENGTH_LONG).show()
            } else {
                IntentUtils.mandaParaWhatsApp(this, numeroTelefoneSuporte)
            }
        }

        binding.btnContinuar.setOnClickListener {
            val campoToken1 = binding.campoToken1.text.toString()
            val campoToken2 = binding.campoToken2.text.toString()
            val campoToken3 = binding.campoToken3.text.toString()
            val campoToken4 = binding.campoToken4.text.toString()
            val token = "$campoToken1$campoToken2$campoToken3$campoToken4"
            if (token.length == 4) {
                binding.btnContinuar.isEnabled = false
                binding.constrainCarregando.visibility = View.VISIBLE
                viewModelActToken.confirmaToken(token)
            }else {
                Toast.makeText(this, getString(R.string.completeToken), Toast.LENGTH_LONG).show()
            }
        }
        viewModelActToken.login.observe(this){

            binding.constrainCarregando.isVisible = false
            if(it != null){
                  if(it.Representante_ID != 0){
                      viewModelActToken.salvarDadosUsuario(it.Representante_ID,it.Nome,it.Hash,it.FotoPerfil, it.UF)
                      startActivity(Intent(this,ActHome::class.java))

                  }else{
                      if (it.Status_Cod == 0){
                          viewModelActToken.mudaStatusCadastro()
                          startActivity(Intent(this,ActCabecalho::class.java))

                      }else{
                          Alertas.alertaErro(this,it.Mensagem,getString(R.string.tituloErro)){
                          }
                      }
                  }
            }else{
                Alertas.alertaErro(this,getString(R.string.erroPadrao),getString(R.string.tituloErro)){
                }
            }
        }

        viewModelActToken.confirmaToken.observe(this){ respostaConfirmaToken ->

            binding.btnContinuar.isEnabled = true
            binding.constrainCarregando.visibility = View.GONE
               if(respostaConfirmaToken != null){
                   Alertas.alertaErro(this,respostaConfirmaToken.Mensagem,getString(R.string.loiuInforma)){
                       if(respostaConfirmaToken.Sucesso){
                            binding.constrainCarregando.isVisible = true
                            viewModelActToken.buscaInformacoesLogin()
                       }
                   }
               }else{
                   Alertas.alertaErro(this, getString(R.string.erroPadrao),getString(R.string.tituloErro)){
                   }
               }
        }

        binding.suporteLoiuConstrain.setOnClickListener {
            binding.constrainCarregando.visibility = View.VISIBLE
            viewModelActToken.buscarNumeroTelefoneSuporte()
        }

        binding.setaVoltarImg.setOnClickListener {
            finish()
        }


        focusCampoToken(binding.campoToken1, binding.campoToken2,null)
        focusCampoToken(binding.campoToken2, binding.campoToken3, binding.campoToken1)
        focusCampoToken(binding.campoToken3, binding.campoToken4, binding.campoToken2)
        focusCampoToken(binding.campoToken4, null, binding.campoToken3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun focusCampoToken(editText: EditText, editTextFocusProximo:EditText?, editTextAnterior:EditText?) {
        editText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val capCampo = s.toString()
                if(editTextFocusProximo != null){
                    if (capCampo.length == 1){
                        editTextFocusProximo.requestFocus()
                    }
                }
                if (editTextAnterior != null){
                    if (capCampo.isEmpty()){
                        editTextAnterior.requestFocus()
                    }
                }

            }
            override fun afterTextChanged(s: Editable?) {
            }

        } )
    }
}