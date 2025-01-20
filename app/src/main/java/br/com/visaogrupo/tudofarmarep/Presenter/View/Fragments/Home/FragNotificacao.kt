package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaCargaProgresso
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaProgress
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home.AdapterNotificacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelFragmentNotificacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.Factory.ViewModelFragmentNotificacaoFactory
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaNotificacao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.NotificacaoRequest
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
    private var listaNotificacao: ArrayList<RespostaNotificacao> = ArrayList()
    private lateinit var viewModel: ViewModelFragmentNotificacao


    var _binding : FragmentFragNotificacaoBinding? = null
    val binding get() = _binding!!

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
        if (listaNotificacao.isEmpty()){
            binding.semNotificacao.isVisible = true
        }else{
            val adapterNotificacao = AdapterNotificacao(listaNotificacao, viewModel)
            binding.recyNotificacao.adapter = adapterNotificacao
            binding.recyNotificacao.layoutManager = LinearLayoutManager(requireContext())
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(
            listaNotificacaoItem: ArrayList<RespostaNotificacao>, viewModel: ViewModelFragmentNotificacao ): FragNotificacao {
            return FragNotificacao().apply {
                this.listaNotificacao = listaNotificacaoItem
                this.viewModel = viewModel
            }
        }
    }
}