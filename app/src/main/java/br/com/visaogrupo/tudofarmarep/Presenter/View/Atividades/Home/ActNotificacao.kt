package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterFragmentItens
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home.AdapterNotificacao
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home.FragNotificacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.Factory.ViewModelFragmentNotificacaoFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelFragmentNotificacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActNotificacaoBinding

class ActNotificacao : AppCompatActivity() {
    val binding:ActivityActNotificacaoBinding by lazy {
        ActivityActNotificacaoBinding.inflate(layoutInflater)
    }
    var naoLidas = false
    lateinit var viewModelFragmentNotificacao : ViewModelFragmentNotificacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val  factory = ViewModelFragmentNotificacaoFactory(this)
        viewModelFragmentNotificacao = ViewModelProvider(this, factory)[ViewModelFragmentNotificacao::class.java]
        binding.ProgresBuscaNotificacao.isVisible = true
        val adapterNotificacao = AdapterFragmentItens(supportFragmentManager, this.lifecycle)
        binding.viewPagerNotificacao.isUserInputEnabled = true
        binding.linearLidos.setOnClickListener {
            if (!naoLidas) {
                binding.linearLido.visibility = View.VISIBLE
                binding.linearNaoLido.visibility = View.INVISIBLE
                naoLidas = true
                binding.viewPagerNotificacao.setCurrentItem(1, true)
            }
        }

        binding.linearNaolidos.setOnClickListener {
            if (naoLidas) {
                binding.linearLido.visibility = View.INVISIBLE
                binding.linearNaoLido.visibility = View.VISIBLE
                naoLidas = false
                binding.viewPagerNotificacao.setCurrentItem(0, true)
            }
        }
        binding.setaVoltarNotificacao.setOnClickListener {
            finish()
        }
        binding.viewPagerNotificacao.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 0) {
                    binding.linearLido.visibility = View.INVISIBLE
                    binding.linearNaoLido.visibility = View.VISIBLE
                    naoLidas = false
                    binding.viewPagerNotificacao.setCurrentItem(0, true)
                } else  {
                    binding.linearLido.visibility = View.VISIBLE
                    binding.linearNaoLido.visibility = View.INVISIBLE
                    naoLidas = true
                    binding.viewPagerNotificacao.setCurrentItem(1, true)
                }
            }
        })



        viewModelFragmentNotificacao.listaNotificacaoLidas.observe(this){
            val fragNotificacao = FragNotificacao.newInstance(it)
            adapterNotificacao.addFragment(fragNotificacao)
            binding.viewPagerNotificacao.adapter = adapterNotificacao


        }
        viewModelFragmentNotificacao.listaNotificacaoNaoLidas.observe(this){
            binding.ProgresBuscaNotificacao.isVisible = false
            val fragNotificacao = FragNotificacao.newInstance(it)
            adapterNotificacao.addFragment(fragNotificacao)
        }

        viewModelFragmentNotificacao.buscanotificacao()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}