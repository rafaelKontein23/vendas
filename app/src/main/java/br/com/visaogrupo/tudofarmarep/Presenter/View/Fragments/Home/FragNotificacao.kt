package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home.AdapterNotificacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelFragmentNotificacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.Factory.ViewModelFragmentNotificacaoFactory
import br.com.visaogrupo.tudofarmarep.databinding.FragmentFragNotificacaoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragNotificacao.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragNotificacao : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var _binding : FragmentFragNotificacaoBinding? = null
    val binding get() = _binding!!
    lateinit var viewModelFragmentNotificacao : ViewModelFragmentNotificacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentFragNotificacaoBinding.inflate(inflater, container, false)
        binding.ProgresBuscaNotificacao.isVisible = true
        val  factory = ViewModelFragmentNotificacaoFactory(requireContext())
        viewModelFragmentNotificacao = ViewModelProvider(this, factory)[ViewModelFragmentNotificacao::class.java]

        viewModelFragmentNotificacao.listaNotificacao.observe(viewLifecycleOwner){
            binding.ProgresBuscaNotificacao.isVisible = false
            if(it.isEmpty()){
                binding.semNotificacao.isVisible = true
            }else{
                binding.recyNotificacao.layoutManager = LinearLayoutManager(requireContext())
                binding.recyNotificacao.adapter = AdapterNotificacao(it)
            }
        }

        viewModelFragmentNotificacao.buscanotificacao()


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragNotificacao().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}