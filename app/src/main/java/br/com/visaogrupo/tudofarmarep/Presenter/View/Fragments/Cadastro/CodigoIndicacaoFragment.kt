package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.databinding.FragmentCodigoIndicacaoBinding


class CodigoIndicacaoFragment : Fragment() {

    private var _binding: FragmentCodigoIndicacaoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodigoIndicacaoBinding.inflate(inflater, container, false)
        return  binding.root
    }
}