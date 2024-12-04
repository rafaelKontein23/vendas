package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Cadastro.SpinnerAdapter
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentDadosCnpjFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosCnpj
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCep
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCnpj
import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosCnpjBinding





class DadosCnpjFragment : Fragment() {

    private var _binding: FragmentDadosCnpjBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFragmentDadosCnpj: ViewModelFragmentDadosCnpj
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    private var mudaTextoSpinner = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDadosCnpjBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentDadosCnpjFactory(requireContext())

        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelFragmentDadosCnpj = ViewModelProvider(this, factory)[ViewModelFragmentDadosCnpj::class.java]
        viewModelFragmentDadosCnpj.alimentaSpinerCore()

        viewModelActCabecalho.mostraCarregando(true)

        binding.tituloCore.setOnClickListener {
            binding.informativoCore.isVisible = !binding.informativoCore.isVisible
            viewModelActCabecalho.mudainfoVisivelCnpj(this)
        }

        viewModelFragmentDadosCnpj.buscaDadosCnpj()
        viewModelFragmentDadosCnpj.listaSpinner.observe(viewLifecycleOwner){
            val adapter = SpinnerAdapter(requireContext(), it)
            binding.possuiCoreSpinner.adapter = adapter
        }

       binding.possuiCoreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    binding.possuiCoreSpinner.validaError(false, requireContext(), binding.textoSelecionadoSpnniner)
                    binding.textoSelecionadoSpnniner.text = selectedItem
                    mudaTextoSpinner ++
                    if((mudaTextoSpinner == 2 || mudaTextoSpinner == 1) && FormularioCadastro.cadastro.possuiCoreText.isNotEmpty() && position == 0){
                        binding.textoSelecionadoSpnniner.text = FormularioCadastro.cadastro.possuiCoreText

                        if(mudaTextoSpinner == 1 ){
                            mudaTextoSpinner = 1
                        }
                    }else{
                        FormularioCadastro.cadastro.possuiCoreText = selectedItem

                    }
                  if(selectedItem.toLowerCase() == "sim"){
                      FormularioCadastro.cadastro.possuiCore = true
                  }else if (selectedItem.toLowerCase() == "não"){
                      FormularioCadastro.cadastro.possuiCore = false

                  }

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
            mudaTextoSpinner = 0
            binding.possuiCoreSpinner.validaError(codicao, requireContext(), binding.textoSelecionadoSpnniner)
            if(codicao){
               binding.scrollViewPessoais.smoothScrollTo(0, binding.possuiCoreSpinner.top)
            }else{
                viewModelFragmentDadosCnpj.salvarInformacoesCnpj(
                    binding.textCnpj.text.toString(),
                    textoSelecionado,
                    binding.textEstado.text.toString()
                )
                viewModelFragmentDadosCnpj.enviaCadastro()
                val dadosPessoaisFragment = DadosPessoaisFragment.newInstance(true)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerCadastro, dadosPessoaisFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }
    fun isInfoVisible(): Boolean {
        return binding.informativoCore.isVisible
    }

    fun hideMenu() {
        binding.informativoCore.isVisible = false
    }


}