package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home.ActHome
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogsMainAtividade
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.Factory.ViewModelMainActivityFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.IntentUtils
import br.com.visaogrupo.tudofarmarep.Utils.PushFirebase
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.ValidarTextos
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.Pushs
import br.com.visaogrupo.tudofarmarep.Utils.Views.isFocus
import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError

import br.com.visaogrupo.tudofarmarep.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }
    private lateinit var viewModelMainActivity: ViewModelMainActivity
    var context: MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
       context = this


        FormataTextos.colocaMascaraInput(binding.inputCnpj,ProjetoStrings.mascaraCNPJ)
        val factory = ViewModelMainActivityFactory(applicationContext)
        viewModelMainActivity = ViewModelProvider(this, factory)[ViewModelMainActivity::class.java]
        viewModelMainActivity.recuperaAmbiente()

        viewModelMainActivity.fezCadastro.observe(this){
            if(it){
                val dialogsMainAtividade = DialogsMainAtividade(this,viewModelMainActivity)
                dialogsMainAtividade.dialogBiometria(viewModelMainActivity, this)
            }
        }

        binding.inputCnpj.setText( viewModelMainActivity.recuperaCnpj())
        viewModelMainActivity.verificaCadastro(binding.inputCnpj.text.toString())

        val pushFirebase = PushFirebase()
        pushFirebase.recuperaDeviceToken()

        viewModelMainActivity.login.observe(this){
            binding.constrainCarregando.isVisible = false
            if(it != null){
                MainScope().launch {
                    if(it.Representante_ID != 0){
                        viewModelMainActivity.salvarDadosUsuario(it.Representante_ID,it.Nome ?: "",it.Hash ?: "",it.FotoPerfil ?: "", it.UF ?: "")
                        val intent = Intent(context, ActHome::class.java)
                        startActivity(intent)
                    }else{
                        Alertas.alertaErro(context!!,it.Mensagem,getString(R.string.tituloErro)){
                            if(it.Status_Cod == 99){
                                startActivity(Intent(context,ActToken::class.java))
                            }
                        }
                    }
                }

            }else{
                MainScope().launch {
                    Alertas.alertaErro(context!!,getString(R.string.erroPadrao),getString(R.string.tituloErro)){
                    }
                }

            }
        }

        binding.inputCnpj.isFocus(this)

        binding.inputCnpj.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cnpjCap = s.toString()
                binding.inputCnpj.validaError(!ValidarTextos.isCNPJ(cnpjCap) && cnpjCap.length >= 18, this@MainActivity)

            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.suporteLoiuConstrain.setOnClickListener {
            binding.constrainCarregando.visibility = View.VISIBLE
            viewModelMainActivity.buscarNumeroTelefoneSuporte()
        }

        binding.loiuLogo.setOnClickListener {
            viewModelMainActivity.abrirModalContator()
        }

        binding.btnContinuar.setOnClickListener {

           val cnpjCap = binding.inputCnpj.text.toString()
            if(ValidarTextos.isCNPJ(cnpjCap)) {
                viewModelMainActivity.salvaCnpj(cnpjCap)
                FormularioCadastro.limpaCadastro()
                val intent = Intent(this, ActCelular::class.java)
                startActivity(intent)
            }
        }

        viewModelMainActivity.numeroTelefoneSuporte.observe(this) { numeroTelefoneSuporte ->
            binding.constrainCarregando.visibility = View.GONE
            if (numeroTelefoneSuporte.isEmpty()) {
                Toast.makeText(this, getString(R.string.erroSuporteWhats), Toast.LENGTH_LONG).show()
            } else {
                IntentUtils.mandaParaWhatsApp(this, numeroTelefoneSuporte)
            }
        }

        viewModelMainActivity.contadorModal.observe(this){contador ->
            if(contador == 5){
                val dialog = DialogsMainAtividade(this,viewModelMainActivity)
                dialog.dialogSenha()
            }
        }

        viewModelMainActivity.ambiente.observe(this){ambiente ->
            val push = Pushs()
            when (ambiente) {
                1 -> {
                    Toast.makeText(this, getString(R.string.ambienteAr), Toast.LENGTH_LONG).show()
                }
                2 -> {
                    Toast.makeText(this, getString(R.string.ambienteQA), Toast.LENGTH_LONG).show()
                    push.criarNotificacaoPush(this,"Ambiente","Você está no ambiente QA")


                }
                3 -> {
                    Toast.makeText(this, getString(R.string.ambienteExterno), Toast.LENGTH_LONG).show()
                    push.criarNotificacaoPush(this,"Ambiente","Você está no ambiente Externo")

                }
                4 -> {
                    Toast.makeText(this, getString(R.string.ambienteInterno), Toast.LENGTH_LONG).show()
                    push.criarNotificacaoPush(this,"Ambiente","Você está no ambiente Interno")

                }
                5 -> {
                    Toast.makeText(this, getString(R.string.ambienteStage), Toast.LENGTH_LONG).show()
                }
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}