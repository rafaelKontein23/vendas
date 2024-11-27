package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActSucessoCadastro
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogContrato
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentContratoAceiteFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelContratoAceite
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosContratoAceiteBinding


class DadosContratoAceiteFragment : Fragment() {
    var _binding : FragmentDadosContratoAceiteBinding? = null
    val binding get() = _binding!!
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    private lateinit var  viewModelContratoAceite: ViewModelContratoAceite



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentDadosContratoAceiteBinding.inflate(inflater, container, false)
        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelActCabecalho.mudaProgressoCadastro(6, 1f)
        val factory = ViewModelFragmentContratoAceiteFactory(requireContext())
        viewModelContratoAceite = ViewModelProvider(this, factory)[ViewModelContratoAceite::class.java]


        if (FormularioCadastro.cadastro.isPoliticaPrivacidade ){
              mudarcheck(binding.aceiteBoxPoliticaPrivacidade)
        }
        if (FormularioCadastro.cadastro.isTermoPolitica){
            mudarcheck(binding.aceiteBoxTermosDeUso)
        }
        if (FormularioCadastro.cadastro.isAssinaContrato){
            mudarcheck(binding.assinaContratoBox)
        }

        viewModelContratoAceite.contratoAssinado.observe(viewLifecycleOwner){
            if (it){
                (activity as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                FormularioCadastro.cadastro.isAssinaContrato = true
                mudarcheck(binding.assinaContratoBox)

            }
            binding.scroolcontrato.post {
                binding.scroolcontrato.fullScroll(View.FOCUS_DOWN)
            }

        }

        binding.textoPolitica.setOnClickListener {
            val  dialog = DialogContrato(requireContext(), viewModelContratoAceite)
            dialog.dialogContratoPolitica(getString(R.string.politicaPrivacidade), getString(R.string.politica))

        }
        binding.textoTermosDeUso.setOnClickListener {
            val  dialog = DialogContrato(requireContext(), viewModelContratoAceite)
            dialog.dialogContratoPolitica(getString(R.string.termosDeUso), "${getString(R.string.temoDeuso)}  ${getString(R.string.segundaParteTermos)}")
        }
        binding.aceiteBoxPoliticaPrivacidade.setOnClickListener {
            mudarcheck(binding.aceiteBoxPoliticaPrivacidade)
            FormularioCadastro.cadastro.isPoliticaPrivacidade = binding.aceiteBoxPoliticaPrivacidade.tag == "1"
        }
        binding.aceiteBoxTermosDeUso.setOnClickListener {
            mudarcheck(binding.aceiteBoxTermosDeUso)
            FormularioCadastro.cadastro.isTermoPolitica = binding.aceiteBoxTermosDeUso.tag == "1"

        }
        binding.assinaContratoBox.setOnClickListener {
            (activity as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val dialog = DialogContrato(requireContext(), viewModelContratoAceite)
            dialog.dialogAssina()
        }
        binding.textoAssinar.setOnClickListener {
            (activity as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val dialog = DialogContrato(requireContext(), viewModelContratoAceite)
            dialog.dialogAssina()

        }
        viewModelContratoAceite.fazCadastro.observe(viewLifecycleOwner){
            viewModelActCabecalho.mostraCarregando(false)
            if(it){
                 val intent = Intent(requireContext(), ActSucessoCadastro::class.java)
                 startActivity(intent)
                 requireActivity().finish()
            }else{
                Alertas.alertaErro(requireContext(), getString(R.string.erroCadastro), getString(R.string.tituloErro) ){
                }
            }
        }

        binding.btnContinuar.setOnClickListener {
            if (FormularioCadastro.cadastro.isPoliticaPrivacidade && FormularioCadastro.cadastro.isTermoPolitica && FormularioCadastro.cadastro.isAssinaContrato){
                  viewModelActCabecalho.mostraCarregando(true)
                  viewModelContratoAceite.enviaCadastroFinal()

            }else{
                Toast.makeText(requireContext(), getString(R.string.erroAceites), Toast.LENGTH_SHORT).show()
            }
        }

        return  binding.root
    }

    fun mudarcheck( constraintLayout: ConstraintLayout){
        if (constraintLayout.tag == "0"){
            constraintLayout.tag = "1"
            constraintLayout.setBackgroundResource(R.drawable.bordas_8_blue600)
        }else{
            constraintLayout.tag = "0"
            constraintLayout.setBackgroundResource(R.drawable.bordas_radius_4_solid_blue600)
        }
    }

}