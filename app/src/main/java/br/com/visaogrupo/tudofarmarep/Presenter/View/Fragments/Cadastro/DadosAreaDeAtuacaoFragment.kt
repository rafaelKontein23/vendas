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

class DadosAreaDeAtuacaoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDadosAreaDeAtuacaoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao
    private lateinit  var  listaUF: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        viewModelFragmentDadosAreaDeAtuacao.ufSelecionada.observe(viewLifecycleOwner){
            binding.inputEstadoAreaDeAtuacao.text = it
            binding.inputCidadesAreaDeAtuacao.text = getText(R.string.todos)
            binding.inputMesorregioesAreaDeAtuacao.text = getText(R.string.todos)
        }

        binding.inputEstadoAreaDeAtuacao.setOnClickListener {
            viewModelFragmentDadosAreaDeAtuacao.listaEstados
            val dialog = DialogDadosAreaDeAtuacao(requireActivity(),
                viewModelFragmentDadosAreaDeAtuacao,
                viewLifecycleOwner)
            listaUF = viewModelFragmentDadosAreaDeAtuacao.listaEstados.value!!
            dialog.dialogUF(listaUF)
        }
        binding.inputMesorregioesAreaDeAtuacao.setOnClickListener {
            val dialog = DialogDadosAreaDeAtuacao(requireActivity(),
                viewModelFragmentDadosAreaDeAtuacao,
                viewLifecycleOwner)

            dialog.dialogMessoRegiao(binding.inputEstadoAreaDeAtuacao.text.toString())

        }
        return binding.root
    }


}