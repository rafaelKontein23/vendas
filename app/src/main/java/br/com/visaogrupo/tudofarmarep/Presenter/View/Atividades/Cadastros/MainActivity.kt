package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utis.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utis.ValidarTextos
import br.com.visaogrupo.tudofarmarep.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val viewModelMainActivity = ViewModelMainActivity()
        FormataTextos.colocaMascaraInput(binding.inputCnpj,"##.###.###/####-##")

        binding.inputCnpj.setOnFocusChangeListener(object :OnFocusChangeListener{
            override fun onFocusChange(v: View?, isFocus: Boolean) {
                if (isFocus){
                    binding.inputCnpj.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)

                }else{
                    binding.inputCnpj.setBackgroundResource(R.drawable.bordas_8_stroke_1_gray300)

                }
            }
        })

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
            CoroutineScope(Dispatchers.IO).launch {
                val numeroTelefoneSuporte =   viewModelMainActivity.buscarNumeroTelefoneSuporte()
                MainScope().launch {
                    if(numeroTelefoneSuporte.isEmpty()){
                        // COLOCAR MENSAGEM DE ERRO AQUI , USAR O AQUIVO DE STRINGS
                    }else{
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(numeroTelefoneSuporte))
                        startActivity(intent)
                    }
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