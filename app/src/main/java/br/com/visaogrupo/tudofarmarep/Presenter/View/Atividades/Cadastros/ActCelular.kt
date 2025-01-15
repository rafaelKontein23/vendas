package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import FormularioCadastro
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home.ActHome
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogsMainAtividade
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.contextThis
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory.ViewModelActCelularFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory.ViewModelMainActivityFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCelular
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.IntentUtils
import br.com.visaogrupo.tudofarmarep.Utils.ValidarTextos
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.Views.isFocus
import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActCelularBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ActCelular : AppCompatActivity() {
    private val binding by lazy {
       ActivityActCelularBinding.inflate(layoutInflater)
    }
    private lateinit var viewModelMainActivity: ViewModelMainActivity

    private lateinit var  viewModelActCelular: ViewModelActCelular
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val factory = ViewModelActCelularFactory(applicationContext)
        val factoryMain = ViewModelMainActivityFactory(applicationContext)

        viewModelActCelular = ViewModelProvider(this, factory)[ViewModelActCelular::class.java]
        viewModelActCelular.exluiCadastro()
        viewModelMainActivity = ViewModelProvider(this, factoryMain)[ViewModelMainActivity::class.java]

        FormataTextos.colocaMascaraInput(binding.inputCelular, ProjetoStrings.mascaraCelular)

        binding.inputCelular.setText(viewModelActCelular.recuperaCelular())

        viewModelActCelular.numeroTelefoneSuporte.observe(this) { numeroTelefoneSuporte ->
            binding.constrainCarregando.visibility = View.GONE
            if (numeroTelefoneSuporte.isEmpty()) {
                Toast.makeText(this, getString(R.string.erroSuporteWhats), Toast.LENGTH_LONG).show()
            } else {
                IntentUtils.mandaParaWhatsApp(this, numeroTelefoneSuporte)
            }
        }
        viewModelMainActivity.login.observe(this){
            binding.constrainCarregando.isVisible = false
            if(it != null){
                MainScope().launch {
                    viewModelMainActivity.salvarDadosUsuario(it.Representante_ID,it.Nome ?: "",it.Hash ?: "",it.FotoPerfil ?: "", it.UF ?: "")

                    if(it.Representante_ID != 0){
                        val intent = Intent(this@ActCelular, ActHome::class.java)
                        startActivity(intent)
                    }else{

                        Alertas.alertaErro(this@ActCelular!!,it.Mensagem,getString(R.string.tituloErro)){
                            if(it.Status_Cod == 99){
                                startActivity(Intent(this@ActCelular,ActToken::class.java))
                            }
                        }
                    }
                }

            }else{
                MainScope().launch {
                    Alertas.alertaErro(this@ActCelular!!,getString(R.string.erroPadrao),getString(R.string.tituloErro)){
                    }
                }

            }
        }
        binding.inputCelular.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val celularCap = binding.inputCelular.text.toString()
                val codicaoCelular = celularCap.length < 14
                if (!codicaoCelular){
                    viewModelActCelular.salvarCelular(celularCap)
                    startActivity(Intent(this, ActToken::class.java))
                }else{
                    binding.inputCelular.validaError(codicaoCelular, this@ActCelular)
                }
                true
            } else {
                false
            }
        }
        viewModelMainActivity.fezCadastro.observe(this){
            if(it.first){
                val dialogsMainAtividade = DialogsMainAtividade(this,viewModelMainActivity)
                dialogsMainAtividade.dialogBiometria(viewModelMainActivity, this)
            }else{
                val celularCap = binding.inputCelular.text.toString()

                val codicaoCelular = celularCap.length < 14
                if (!codicaoCelular){
                    viewModelActCelular.salvarCelular(celularCap)
                    viewModelMainActivity.salvaCnpj(FormularioCadastro.cnpjCampo)

                    startActivity(Intent(this, ActToken::class.java))
                }else{
                    binding.inputCelular.validaError(codicaoCelular, this@ActCelular)
                }
            }
        }
        binding.inputCelular.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val celularCap = s.toString()
                binding.inputCelular.validaError( ValidarTextos.isCelular(celularCap), this@ActCelular)

            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.suporteLoiuConstrain.setOnClickListener {
            binding.constrainCarregando.visibility = View.VISIBLE
            viewModelActCelular.buscarNumeroTelefoneSuporte()
        }

        binding.inputCelular.isFocus(this)


        binding.btnContinuar.setOnClickListener {
            val celularCap = binding.inputCelular.text.toString()
            viewModelMainActivity.verificaCadastroCelular(FormularioCadastro.cnpjCampo ,celularCap)
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