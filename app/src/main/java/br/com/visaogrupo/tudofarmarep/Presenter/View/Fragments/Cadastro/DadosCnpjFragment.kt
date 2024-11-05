package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.SpinnerAdapter
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentDadosCnpjFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosCnpj
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCep
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCnpj
import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosCnpjBinding
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosPessoaisBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DadosCnpjFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDadosCnpjBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFragmentDadosCnpj: ViewModelFragmentDadosCnpj
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho


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
    ): View {
        _binding = FragmentDadosCnpjBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentDadosCnpjFactory(requireContext())

        viewModelActCabecalho = ViewModelProvider(this)[ViewModelActCabecalho::class.java]
        viewModelFragmentDadosCnpj = ViewModelProvider(this, factory)[ViewModelFragmentDadosCnpj::class.java]
        viewModelFragmentDadosCnpj.alimentaSpinerCore()

        viewModelActCabecalho.mostraCarregando(true)

        viewModelFragmentDadosCnpj.buscaDadosCnpj()
        viewModelFragmentDadosCnpj.listaSpinner.observe(viewLifecycleOwner){
            val adapter = SpinnerAdapter(requireContext(), it)
            binding.possuiCoreSpinner.adapter = adapter
        }

        binding.possuiCoreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                binding.textoSelecionadoSpnniner.text = selectedItem
                binding.possuiCoreSpinner.validaError(false, requireContext(), binding.textoSelecionadoSpnniner)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        };



        viewModelFragmentDadosCnpj.cnpj.observe(viewLifecycleOwner){ cnpj ->
            viewModelActCabecalho.mostraCarregando(false)
            if(cnpj == null){
                Alertas.alertaErro(requireContext(), getString(R.string.erroCnpj), getString(R.string.tituloErro) ){
                    viewModelActCabecalho.finalizaAtividade()
                }
            }else{
                binding.textCnpj.text = cnpj.CNPJ.aplicarMascaraCnpj()
                binding.textRazao.text = cnpj.RazaoSocial
                binding.textFantasia.text = cnpj.Fantasia
                binding.textCep.text = cnpj.CEP.aplicarMascaraCep()
                binding.textEndereco.text = cnpj.Endereco
                binding.textBairro.text = cnpj.Bairro
                binding.textCidade.text = cnpj.Cidade
                binding.textEstado.text = cnpj.UF
            }
        }

        binding.btnContinuar.setOnClickListener {
            val textoSelecionado = binding.textoSelecionadoSpnniner.text.toString()
            val codicao = textoSelecionado == getString(R.string.Selecione)
            binding.possuiCoreSpinner.validaError(codicao, requireContext(), binding.textoSelecionadoSpnniner)
            if(codicao){
               binding.scrollViewPessoais.smoothScrollTo(0, binding.possuiCoreSpinner.top)
            }else{
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerCadastro, DadosPessoaisFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }


}