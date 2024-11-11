package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentDadosAreaDeAtuacaoFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.obterNomeCompletoUF
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosAreaDeAtuacaoBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DadosAreaDeAtuacaoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDadosAreaDeAtuacaoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao
    lateinit  var  listaUF: List<String>
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
        _binding = FragmentDadosAreaDeAtuacaoBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentDadosAreaDeAtuacaoFactory(requireContext())
        viewModelFragmentDadosAreaDeAtuacao = ViewModelProvider(this, factory)[ViewModelFragmentDadosAreaDeAtuacao::class.java]
        val ufSelecionada = FormularioCadastro.cadastro.UF.obterNomeCompletoUF(FormularioCadastro.cadastro.UF)
        viewModelFragmentDadosAreaDeAtuacao.selecionaUF(ufSelecionada)
        binding.inputEstadoAreaDeAtuacao.text = ufSelecionada

        viewModelFragmentDadosAreaDeAtuacao.ufSelcionada.observe(viewLifecycleOwner){
            binding.inputEstadoAreaDeAtuacao.text = it
            binding.inputCidadesAreaDeAtuacao.text = getText(R.string.todos)
            binding.inputMesorregioesAreaDeAtuacao.text = getText(R.string.todos)
        }


        binding.inputEstadoAreaDeAtuacao.setOnClickListener {
            viewModelFragmentDadosAreaDeAtuacao.listaEstados
            val dialog = DialogDadosAreaDeAtuacao(requireActivity(), viewModelFragmentDadosAreaDeAtuacao)
            listaUF = viewModelFragmentDadosAreaDeAtuacao.listaEstados.value!!
            dialog.dialogUF(listaUF)
        }
        binding.inputMesorregioesAreaDeAtuacao.setOnClickListener {
            viewModelFragmentDadosAreaDeAtuacao.buscaDadosAreaDeAtuacaoMesorregiao("SP")
        }
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DadosAreaDeAtuacaoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}