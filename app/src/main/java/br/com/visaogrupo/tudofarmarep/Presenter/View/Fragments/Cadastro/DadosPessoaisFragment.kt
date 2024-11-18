package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.annotation.SuppressLint
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
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentDadosPessoalFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosPessoais
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.ValidarTextos
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos
import br.com.visaogrupo.tudofarmarep.Utils.Views.isFocusCPF
import br.com.visaogrupo.tudofarmarep.Utils.Views.isFocusEditTextBasico
import br.com.visaogrupo.tudofarmarep.Utils.Views.isFocusEmail

import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosPessoaisBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DadosPessoaisFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDadosPessoaisBinding? = null
    private val binding get() = _binding!!

    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    private lateinit var viewModelFragmentDadosPessoal: ViewModelFragmentDadosPessoais


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDadosPessoaisBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentDadosPessoalFactory(requireContext())
        viewModelFragmentDadosPessoal = ViewModelProvider(this, factory)[ViewModelFragmentDadosPessoais::class.java]

        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelActCabecalho.mudaProgressoCadastro(2, 1f)

        FormataTextos.colocaMascaraInput(binding.inputCpf, ProjetoStrings.mascaraCPF)
        FormataTextos.colocaMascaraInput(binding.inputTelefoneComercial, ProjetoStrings.mascaraTelefone)
        FormataTextos.colocaMascaraInput(binding.inputCelular, ProjetoStrings.mascaraCelular)
        viewModelFragmentDadosPessoal.recuperaNumeroCelular()
        if(FormularioCadastro.cadastro.nome.isNotEmpty()){
            binding.inputNome.setText(FormularioCadastro.cadastro.nome)
            binding.inputSobrenome.setText(FormularioCadastro.cadastro.sobrenome)
            binding.inputCpf.setText(FormularioCadastro.cadastro.cpf)
            binding.inputDataNacimento.setText(FormularioCadastro.cadastro.dataNascimento)
            binding.inputEmail.setText(FormularioCadastro.cadastro.email)
            binding.inputTelefoneComercial.setText(FormularioCadastro.cadastro.telefoneComercial)
        }
        viewModelFragmentDadosPessoal.numeroCelular.observe(viewLifecycleOwner){
            binding.inputCelular.setText(it)
            binding.inputCelular.isEnabled = false
            binding.inputCelular.setTextColor(requireContext().getColor(R.color.gray500))
        }

        binding.inputNome.isFocusEditTextBasico(requireContext())
        binding.inputSobrenome.isFocusEditTextBasico(requireContext())
        binding.inputCpf.isFocusCPF(requireContext())
        binding.inputTelefoneComercial.isFocusEditTextBasico(requireContext())
        binding.inputCpf.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cpfCap = s.toString()
                binding.inputCpf.validaError(!ValidarTextos.isCPF(cpfCap) && cpfCap.length >= 14, requireContext())
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailCap = s.toString()
                binding.inputEmail.validaError( emailCap.isEmpty() || !emailCap.contains("@") || !emailCap.contains(".com"), requireContext())
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.inputEmail.isFocusEmail(requireContext())

        binding.textViewCelular.setOnClickListener {
            binding.informativoCelular.isVisible = !binding.informativoCelular.isVisible
            viewModelActCabecalho.mudaInfoVisivel(this)
        }

        binding.inputDataNacimento.setOnClickListener {
            Alertas.showDatePickerDialog(binding.inputDataNacimento, requireContext())
        }

        binding.btnContinuar.setOnClickListener {
            val nome = binding.inputNome.text.toString()
            val sobrenome = binding.inputSobrenome.text.toString()
            val cpf = binding.inputCpf.text.toString()
            val dataNacimento = binding.inputDataNacimento.text.toString()
            val email = binding.inputEmail.text.toString()
            val telefoneComercial = binding.inputTelefoneComercial.text.toString()

            if(nome.isEmpty() ||
                sobrenome.isEmpty() ||
                cpf.isEmpty() ||
                !ValidarTextos.isCPF(cpf)||
                dataNacimento.isEmpty() ||
                email.isEmpty() ||
                !email.contains("@") ||
                !email.contains(".com")){
                Toast.makeText(requireContext(), "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
                if(email.isEmpty() || !email.contains("@") || !email.contains(".com")){
                    binding.scroolPessoais.smoothScrollTo(0,  binding.inputEmail.top)
                    binding.inputEmail.validaError( true, requireContext())

                }
                if(dataNacimento.isEmpty()){
                    binding.scroolPessoais.smoothScrollTo(0,  binding.inputDataNacimento.top)
                    binding.inputDataNacimento.validaError( true , requireContext())
                }

                if(cpf.isEmpty() || !ValidarTextos.isCPF(cpf)){
                    binding.scroolPessoais.smoothScrollTo(0,  binding.inputCpf.top)
                    binding.inputCpf.validaError( true, requireContext())
                }
                if(sobrenome.isEmpty()){
                    binding.scroolPessoais.smoothScrollTo(0,  binding.inputSobrenome.top)
                    binding.inputSobrenome.validaError( true, requireContext())
                }

                if(nome.isEmpty()){
                    binding.scroolPessoais.smoothScrollTo(0,  binding.inputNome.top)
                    binding.inputNome.validaError( true, requireContext())
                }


            }else{
                viewModelFragmentDadosPessoal.salvaCamposPessoais(nome, sobrenome, cpf, dataNacimento, email, telefoneComercial)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerCadastro, FotoDocumentoFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }


        return binding.root
    }

     fun isInfoVisible(): Boolean {
        return binding.informativoCelular.isVisible
    }

    fun hideMenu() {
        binding.informativoCelular.isVisible = false
    }
    fun isTouchInsideMenu(x: Int, y: Int): Boolean {
        val location = IntArray(2)
        binding.informativoCelular.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        return (x >= viewX && x <= viewX + binding.informativoCelular.width && y >= viewY && y <= viewY + binding.informativoCelular.height)
    }
}