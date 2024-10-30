package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.Dialogs
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.Factory.ViewModelMainActivityFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.ValidarTextos
import br.com.visaogrupo.tudofarmarep.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }
    private lateinit var viewModelMainActivity: ViewModelMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        FormataTextos.colocaMascaraInput(binding.inputCnpj,"##.###.###/####-##")

        val factory = ViewModelMainActivityFactory(applicationContext)
        viewModelMainActivity = ViewModelProvider(this, factory)[ViewModelMainActivity::class.java]

        binding.inputCnpj.setOnFocusChangeListener { _, isFocus ->
            if (isFocus) {
                binding.inputCnpj.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)

            } else {
                binding.inputCnpj.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)

            }
        }

        binding.inputCnpj.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cnpjCap = s.toString()
                if(!ValidarTextos.isCNPJ(cnpjCap) && cnpjCap.length >= 18){
                    binding.inputCnpj.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
                    binding.inputCnpj.setTextColor(getColor(R.color.danger500))
                }else{
                    binding.inputCnpj.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
                    binding.inputCnpj.setTextColor(getColor(R.color.black))
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnContinuar.setOnClickListener {
            binding.inputCnpj.isEnabled =false
        }

        binding.suporteLoiuConstrain.setOnClickListener {
            binding.constrainCarregando.visibility = View.VISIBLE
            viewModelMainActivity.buscarNumeroTelefoneSuporte()
        }
        binding.loiuLogo.setOnClickListener {
            viewModelMainActivity.abrirModalContator()
        }

        viewModelMainActivity.numeroTelefoneSuporte.observe(this) { numeroTelefoneSuporte ->
            binding.constrainCarregando.visibility = View.GONE
            if (numeroTelefoneSuporte.isEmpty()) {
                Toast.makeText(this, getString(R.string.erroSuporteWhats), Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(numeroTelefoneSuporte)
                startActivity(intent)
            }
        }

        viewModelMainActivity.contadorModal.observe(this){contador ->
            if(contador == 5){
                binding.constrainCarregando.visibility = View.VISIBLE
            }else{
                val dialog = Dialogs(this)
                dialog.dialogTrocaAmbiente()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}