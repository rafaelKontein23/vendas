package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterFragmentItens
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.FragNotificacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActNotificacaoBinding

class ActNotificacao : AppCompatActivity() {
    val binding:ActivityActNotificacaoBinding by lazy {
        ActivityActNotificacaoBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val adapterNotificacao = AdapterFragmentItens(supportFragmentManager, this.lifecycle)
        adapterNotificacao.addFragment(FragNotificacao())
        adapterNotificacao.addFragment(FragNotificacao())
        binding.viewPagerNotificacao.adapter = adapterNotificacao
        binding.viewPagerNotificacao.isUserInputEnabled = false
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}