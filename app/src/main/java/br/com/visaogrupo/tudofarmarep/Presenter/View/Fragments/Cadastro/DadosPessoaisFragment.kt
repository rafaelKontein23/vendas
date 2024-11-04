package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.SpinnerAdapter
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentDadosCnpjFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosCnpj
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCep
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCnpj
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosPessoaisBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DadosPessoaisFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDadosPessoaisBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModelFragmentDadosCnpj: ViewModelFragmentDadosCnpj


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
        _binding = FragmentDadosPessoaisBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentDadosCnpjFactory(requireContext())


        viewModelFragmentDadosCnpj = ViewModelProvider(this, factory)[ViewModelFragmentDadosCnpj::class.java]
        viewModelFragmentDadosCnpj.alimentaSpinerCore()

        viewModelFragmentDadosCnpj._lista.observe(viewLifecycleOwner){
            val adapter = SpinnerAdapter(requireContext(), it)
            binding.possuiCoreSpinner.adapter = adapter
        }
        binding.possuiCoreSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                binding.textoSelecionadoSpnniner.text = selectedItem

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        });

        viewModelFragmentDadosCnpj.buscaDadosCnpj()

        viewModelFragmentDadosCnpj.cnpj.observe(viewLifecycleOwner){ cnpj ->
            binding.textCnpj.text = cnpj.CNPJ.aplicarMascaraCnpj()
            binding.textRazao.text = cnpj.RazaoSocial
            binding.textFantasia.text = cnpj.Fantasia
            binding.textCep.text = cnpj.CEP.aplicarMascaraCep()
            binding.textEndereco.text = cnpj.Endereco
            binding.textBairro.text = cnpj.Bairro
            binding.textCidade.text = cnpj.Cidade
            binding.textEstado.text = cnpj.UF
        }

        return binding.root
    }


}