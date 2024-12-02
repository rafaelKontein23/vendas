package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogInstituicao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.Factory.ViewModelDadosBancariosFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Fragments.ViewModelDadosBancarios
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCnpj
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosBancariosBinding
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosCnpjBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DadosBancariosFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDadosBancariosBinding? = null
    private val binding get() = _binding!!
    private  lateinit var  viewModelDadosBancarios: ViewModelDadosBancarios

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
        _binding = FragmentDadosBancariosBinding.inflate(inflater, container, false)
        val factory = ViewModelDadosBancariosFactory(requireContext())
        viewModelDadosBancarios = ViewModelProvider(this, factory)[ViewModelDadosBancarios::class.java]
        binding.constrainCarregando.isVisible = true
        viewModelDadosBancarios.buscaDadosBancarios()
        viewModelDadosBancarios.recuperaCNPJ()

        viewModelDadosBancarios.recuperaCNPJ.observe(viewLifecycleOwner){
            binding.textoDados.setText(it.aplicarMascaraCnpj())
        }
        viewModelDadosBancarios.dadosBancarios.observe(viewLifecycleOwner){
            binding.constrainCarregando.isVisible = false
            if (it != null){
                if (it.Status_cod == 0 ){
                    bloquiacampos(true)
                }else{
                    bloquiacampos(false)
                }
            }else{
                Alertas.alertaErro(requireActivity(), mensagem =  getString(R.string.erro_busca_dados_bancarios), titulo =  getString(R.string.tituloErro)){
                }
            }
        }
        viewModelDadosBancarios.textoInstituicao.observe(viewLifecycleOwner){
            binding.inputInstituicao.setText(it)
        }
        viewModelDadosBancarios.listabanco.observe(viewLifecycleOwner){
            binding.constrainCarregando.isVisible = false
            if(it.isNotEmpty()){
                val dialogInstituicao = DialogInstituicao()
                dialogInstituicao.dialogInstituicao(requireContext(), it, viewModelDadosBancarios, binding.inputInstituicao.text.toString())
            }else{
                Alertas.alertaErro(requireActivity(), mensagem =  getString(R.string.erro_busca_dados_bancarios), titulo =  getString(R.string.tituloErro)){
                }
            }

        }
        binding.inputInstituicao.setOnClickListener {
            binding.constrainCarregando.isVisible = true
            viewModelDadosBancarios.buscaDadosInstituicaoBancaria()
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DadosBancariosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun bloquiacampos( bloqueiaCampo:Boolean ){
        binding.inputInstituicao.isEnabled = bloqueiaCampo
        binding.inputAgencia.isEnabled = bloqueiaCampo
        binding.inputConta.isEnabled = bloqueiaCampo

    }
}