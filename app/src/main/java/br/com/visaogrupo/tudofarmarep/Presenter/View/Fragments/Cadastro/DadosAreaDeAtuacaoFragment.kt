package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentDadosAreaDeAtuacaoFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelFragmentDadosAreaDeAtuacao
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDadosAreaDeAtuacaoBinding.inflate(inflater, container, false)
        val factory = ViewModelFragmentDadosAreaDeAtuacaoFactory(requireContext())
        viewModelFragmentDadosAreaDeAtuacao = ViewModelProvider(this, factory)[ViewModelFragmentDadosAreaDeAtuacao::class.java]
        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelActCabecalho.mudaProgressoCadastro(4, 1f)

        val ufSelecionada = FormularioCadastro.cadastro.UF.obterNomeCompletoUF(FormularioCadastro.cadastro.UF)
        viewModelFragmentDadosAreaDeAtuacao.selecionaUF(ufSelecionada)
        binding.inputEstadoAreaDeAtuacao.text = ufSelecionada

        viewModelFragmentDadosAreaDeAtuacao.buscaDadosAreaDeAtuacaoMesorregiao(FormularioCadastro.cadastro.UF, true)

        viewModelFragmentDadosAreaDeAtuacao.cidadeSelecionada.observe(viewLifecycleOwner){cidadeSelecionada ->
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

        viewModelFragmentDadosAreaDeAtuacao.mesorregiaoSelecionada.observe(viewLifecycleOwner){ mesorregiaoSelecionadas ->
            if (mesorregiaoSelecionadas != null){
                if(viewModelFragmentDadosAreaDeAtuacao.confereTamanhoListaMesorregiao()){
                    binding.inputMesorregioesAreaDeAtuacao.text = getText(R.string.todos)
                    binding.inputMesorregioesAreaDeAtuacao.validaError(false, requireContext())
                }else if (mesorregiaoSelecionadas.isEmpty()){
                    binding.inputMesorregioesAreaDeAtuacao.validaError(true, requireContext())
                    binding.inputMesorregioesAreaDeAtuacao.text = getText(R.string.Selecione)
                }else{
                    var mesorregioesNomes = ""
                    for (mesoRegiao in mesorregiaoSelecionadas){
                        mesorregioesNomes += if (mesoRegiao == mesorregiaoSelecionadas.last()) "${mesoRegiao.Mesorregiao_Nome} " else "${mesoRegiao.Mesorregiao_Nome}, "
                        if(mesorregioesNomes.length > 35) break
                    }
                    binding.inputMesorregioesAreaDeAtuacao.text = mesorregioesNomes.formataTextoGrandes(mesorregioesNomes,35)
                    binding.inputMesorregioesAreaDeAtuacao.validaError(false, requireContext())
                }
            }else{
                Alertas.alertaErro(requireContext(), mensagem =   getString(R.string.erroSuporteWhats), titulo =  getString(R.string.tituloErro)){}
            }
        }

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
                binding.inputCidadesAreaDeAtuacao.validaError(true, requireContext())
                binding.inputMesorregioesAreaDeAtuacao.validaError(true, requireContext())
            }else{

            }
        }
        return binding.root
    }
}