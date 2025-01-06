package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentDadosAreaDeAtuacaoFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.formataTextoGrandes
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.obterNomeCompletoUF
import br.com.visaogrupo.tudofarmarep.Utils.Views.validaError
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosAreaDeAtuacaoBinding

class DadosAreaDeAtuacaoFragment : Fragment() {

    private var _binding: FragmentDadosAreaDeAtuacaoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFragmentDadosAreaDeAtuacao: ViewModelFragmentDadosAreaDeAtuacao
    private lateinit  var  listaUF: List<String>
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    private var isCadastro: Boolean = false

    companion object {
        private const val ARG_IS_EDITABLE = "ARG_IS_EDITABLE"

        fun newInstance(isEditable: Boolean): DadosAreaDeAtuacaoFragment {
            val fragment = DadosAreaDeAtuacaoFragment()
            val args = Bundle().apply {
                putBoolean(ARG_IS_EDITABLE, isEditable)
            }
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            isCadastro = args.getBoolean(ARG_IS_EDITABLE, false)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDadosAreaDeAtuacaoBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentDadosAreaDeAtuacaoFactory(requireContext())
        viewModelFragmentDadosAreaDeAtuacao = ViewModelProvider(this, factory)[ViewModelFragmentDadosAreaDeAtuacao::class.java]
        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        bloqueiaCampo(true)

        if(isCadastro){
            viewModelActCabecalho.mudaProgressoCadastro(4, 1f)
            val ufSelecionada = if(FormularioCadastro.cadastroRequestAreaAtuacal.UF != "") FormularioCadastro.cadastroRequestAreaAtuacal.UF else FormularioCadastro.cadastro.UF
            viewModelFragmentDadosAreaDeAtuacao.selecionaUF(ufSelecionada)
            binding.inputEstadoAreaDeAtuacao.text = ufSelecionada.obterNomeCompletoUF(ufSelecionada)
            viewModelFragmentDadosAreaDeAtuacao.buscaDadosAreaDeAtuacaoMesorregiao(ufSelecionada, true)

        }else{
            viewModelFragmentDadosAreaDeAtuacao.buscaDadosAreaDeAtuacaoEdicao()
            binding.btnContinuar.text = getString(R.string.AtualizarDados)
            binding.constrainCarregando.isVisible = true
        }


        viewModelFragmentDadosAreaDeAtuacao.cidadeSelecionada.observe(viewLifecycleOwner){cidadeSelecionada ->
            bloqueiaCampo(false)

            if (cidadeSelecionada != null){
                if(viewModelFragmentDadosAreaDeAtuacao.confereTamanhoListaCidades()){
                    binding.inputCidadesAreaDeAtuacao.text = getText(R.string.todos)
                    binding.inputCidadesAreaDeAtuacao.validaError(false, requireContext())
                }else if (cidadeSelecionada.isEmpty()){
                    binding.inputCidadesAreaDeAtuacao.validaError(true, requireContext())
                    binding.inputCidadesAreaDeAtuacao.text = getText(R.string.Selecione)
                }else{
                    var cidadesNomes = ""
                    for (cidades in cidadeSelecionada){
                        if (cidades.Cidade == "Todas") continue
                        cidadesNomes += if (cidades == cidadeSelecionada.last()) "${cidades.Cidade} " else "${cidades.Cidade}, "
                        if(cidadesNomes.length > 35) break
                    }
                    binding.inputCidadesAreaDeAtuacao.text = cidadesNomes.formataTextoGrandes(cidadesNomes,35)
                    binding.inputCidadesAreaDeAtuacao.validaError(false, requireContext())
                }
            }else{
                Alertas.alertaErro(requireContext(), mensagem =   getString(R.string.erroSuporteWhats), titulo =  getString(R.string.tituloErro)){}
            }
        }
        viewModelFragmentDadosAreaDeAtuacao.editaDadosObs.observe(viewLifecycleOwner){
            binding.constrainCarregando.isVisible = false
            if(!isCadastro){
                if(it){
                    Alertas.alertaErro(requireContext(), mensagem =   getString(R.string.DadosAtuazlizadoComSucesso), titulo =  getString(R.string.loiuInforma)){

                    }
                }else{
                    Alertas.alertaErro(requireContext(), mensagem =   getString(R.string.erroAtualiza), titulo =  getString(R.string.tituloErro)){}
                }
            }

        }

        viewModelFragmentDadosAreaDeAtuacao.mesorregiaoSelecionada.observe(viewLifecycleOwner){ mesorregiaoSelecionadas ->
            bloqueiaCampo(false)

            if (mesorregiaoSelecionadas != null){
                if(viewModelFragmentDadosAreaDeAtuacao.confereTamanhoListaMesorregiao()){
                    binding.inputMesorregioesAreaDeAtuacao.text = getText(R.string.todos)
                    binding.inputMesorregioesAreaDeAtuacao.validaError(false, requireContext())
                }else if (mesorregiaoSelecionadas.isEmpty()){
                    binding.inputMesorregioesAreaDeAtuacao.validaError(true, requireContext())
                    binding.inputMesorregioesAreaDeAtuacao.text = getText(R.string.Selecione)
                }else{
                    var mesorregioesNomes = ""
                    val confere = viewModelFragmentDadosAreaDeAtuacao.confereTamanhoListaMesorregiao()
                    if (confere){ // continuar aquiiiiii
                        binding.inputMesorregioesAreaDeAtuacao.text = getText(R.string.todos)
                    }else{
                        for (mesoRegiao in mesorregiaoSelecionadas){
                            if (mesoRegiao.Mesorregiao_Nome == "Todas") continue
                            mesorregioesNomes += if (mesoRegiao == mesorregiaoSelecionadas.last()) "${mesoRegiao.Mesorregiao_Nome} " else "${mesoRegiao.Mesorregiao_Nome}, "
                            if(mesorregioesNomes.length > 35) break
                        }
                        binding.inputMesorregioesAreaDeAtuacao.text = mesorregioesNomes.formataTextoGrandes(mesorregioesNomes,35)
                        binding.inputMesorregioesAreaDeAtuacao.validaError(false, requireContext())
                    }

                }
            }else{
                Alertas.alertaErro(requireContext(), mensagem =   getString(R.string.erroSuporteWhats), titulo =  getString(R.string.tituloErro)){}
            }
        }

        viewModelFragmentDadosAreaDeAtuacao.ufTextoObs.observe(viewLifecycleOwner){
            binding.inputEstadoAreaDeAtuacao.text = it
            binding.constrainCarregando.isVisible = false

        }

        viewModelFragmentDadosAreaDeAtuacao.ufSelecionada.observe(viewLifecycleOwner){
            bloqueiaCampo(true)
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
            dialog.dialogMessoRegiao(binding.inputEstadoAreaDeAtuacao.text.toString(), isCadastro)
        }

        binding.inputCidadesAreaDeAtuacao.setOnClickListener {
            if (viewModelFragmentDadosAreaDeAtuacao.confereMessoRegiaoList()){
                 Alertas.alertaErro(requireContext(), mensagem =   getString(R.string.erroMessoRegiao), titulo =  getString(R.string.tituloErro)){}
            }else{
                val dialog = DialogDadosAreaDeAtuacao(requireActivity(),
                    viewModelFragmentDadosAreaDeAtuacao,
                    viewLifecycleOwner)

                dialog.dialogCidades()
            }

        }

        binding.btnContinuar.setOnClickListener {
            if (viewModelFragmentDadosAreaDeAtuacao.confereMessoRegiaoList() || viewModelFragmentDadosAreaDeAtuacao.confereCidadesList()){
                binding.inputCidadesAreaDeAtuacao.validaError(viewModelFragmentDadosAreaDeAtuacao.confereCidadesList(), requireContext())
                binding.inputMesorregioesAreaDeAtuacao.validaError(viewModelFragmentDadosAreaDeAtuacao.confereMessoRegiaoList(), requireContext())
            }else{
                binding.inputCidadesAreaDeAtuacao.validaError(false, requireContext())
                binding.inputMesorregioesAreaDeAtuacao.validaError(false, requireContext())
                viewModelFragmentDadosAreaDeAtuacao.mandaCadatro(!isCadastro)
                if(isCadastro){
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerCadastro, CodigoIndicacaoFragment())
                        .addToBackStack(null)
                        .commit()
                }else{
                    binding.constrainCarregando.isVisible = true
                }

            }
        }
        return binding.root
    }

    fun bloqueiaCampo(isvisivel : Boolean){
        viewModelActCabecalho.mostraCarregando(isvisivel)
        binding.btnContinuar.isEnabled = !isvisivel

    }
}