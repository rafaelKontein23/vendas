package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentCodigoIndicacaoFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentCodigoIndicacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.IntentUtils
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCnpj
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraTelefone
import br.com.visaogrupo.tudofarmarep.Utils.Views.isFocusEditTextBasico
import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError
import br.com.visaogrupo.tudofarmarep.databinding.FragmentCodigoIndicacaoBinding


class CodigoIndicacaoFragment : Fragment() {

    private var _binding: FragmentCodigoIndicacaoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFragmentCodigoIndicacao: ViewModelFragmentCodigoIndicacao
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    var codigoIndicacaoValido = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodigoIndicacaoBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentCodigoIndicacaoFactory(requireContext())
        viewModelFragmentCodigoIndicacao = ViewModelProvider(this, factory)[ViewModelFragmentCodigoIndicacao::class.java]
        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelActCabecalho.mudaProgressoCadastro(5, 1f)

        binding.inputCodigoIndicacao.isFocusEditTextBasico(requireContext())
        if (FormularioCadastro.cadastro.hashIndicacao.isNotEmpty()){
            binding.btnContinuar.setBackgroundResource(R.drawable.bordas_8_blue600)
            binding.inputCodigoIndicacao.setText(FormularioCadastro.cadastro.hashIndicacao)
            viewModelFragmentCodigoIndicacao.buscaInformacaoIndicacao(FormularioCadastro.cadastro.hashIndicacao)
        }else{
            binding.btnContinuar.setBackgroundResource(R.drawable.bordas_radius_8_solid_blue300)
        }
        viewModelFragmentCodigoIndicacao.dadosIndicacao.observe(viewLifecycleOwner){dadosIndicacao ->
            binding.carregando.isVisible = false
            if(dadosIndicacao == null){
                binding.constrainInfosRepresentante.isVisible = false
                codigoIndicacaoValido = false
                binding.btnContinuar.setBackgroundResource(R.drawable.bordas_radius_8_solid_blue300)
               Alertas.alertaErro(requireContext(), getString(R.string.erroCodigoIndicacao), getString(R.string.tituloErro)){
                   binding.inputCodigoIndicacao.validaError(false, requireContext())
               }
            }else{
                binding.btnContinuar.setBackgroundResource(R.drawable.bordas_8_blue600)
                binding.inputCodigoIndicacao.validaError(false, requireContext())
                binding.constrainInfosRepresentante.isVisible = true
                binding.nomeRepresentante.text = dadosIndicacao.Nome
                binding.cnpjRepresentante.text = dadosIndicacao.CNPJ.aplicarMascaraCnpj()
                binding.celularRepresentante.text = dadosIndicacao.Telefone.aplicarMascaraTelefone()
                codigoIndicacaoValido = true

            }
        }
        binding.inputCodigoIndicacao.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val codigoIndicacao = s.toString()
                binding.inputCodigoIndicacao.validaError(codigoIndicacao.length < 8, requireContext())
                if (codigoIndicacao.length  == 8){
                    binding.carregando.isVisible = true
                    viewModelFragmentCodigoIndicacao.buscaInformacaoIndicacao(codigoIndicacao)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.btnContinuar.setOnClickListener {
            val campoHash = binding.inputCodigoIndicacao.text.toString()
            if (campoHash.length ==8 && codigoIndicacaoValido){
                binding.inputCodigoIndicacao.validaError(false, requireContext())
                viewModelFragmentCodigoIndicacao.enviaCadadstro(campoHash)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerCadastro, DadosContratoAceiteFragment())
                    .addToBackStack(null)
                    .commit()
            }else{
                binding.inputCodigoIndicacao.validaError(true, requireContext())
                Toast.makeText(requireContext(), getString(R.string.erroCodigoIndicacao), Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnContinuarSemCodigo.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerCadastro, DadosContratoAceiteFragment())
                .addToBackStack(null)
                .commit()
        }
        viewModelFragmentCodigoIndicacao.numeroTelefoneSuporte.observe(viewLifecycleOwner){numeroTelefoneSuporte->
            binding.constrainCarregando.isVisible = false
            if(numeroTelefoneSuporte.isEmpty()){
                Alertas.alertaErro(requireContext(), getString(R.string.erroSuporteWhats), getString(R.string.tituloErro)){}
            }else{
                IntentUtils.mandaParaWhatsApp(requireActivity(), numeroTelefoneSuporte)
            }
        }
        binding.textoIndormaDivergencia.setOnClickListener {
            binding.constrainCarregando.isVisible = true
            viewModelFragmentCodigoIndicacao.buscarNumeroTelefoneSuporte()

        }

        return  binding.root
    }
}