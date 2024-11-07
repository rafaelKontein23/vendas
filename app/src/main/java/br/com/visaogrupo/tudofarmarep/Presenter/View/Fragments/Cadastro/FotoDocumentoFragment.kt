package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActCameraGaleria
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelFotoDocumento
import br.com.visaogrupo.tudofarmarep.databinding.FragmentFotoDocumentoBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FotoDocumentoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentFotoDocumentoBinding? = null
    private val binding get() = _binding!!
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    private val REQUEST_IMAGE_CODE = 100




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
        _binding = FragmentFotoDocumentoBinding.inflate(inflater, container, false)
        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelActCabecalho.mudaProgressoCadastro(3, 1f)


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA), 4);
        }



    binding.adiconarFoto.setOnClickListener {
        val intent = Intent(requireContext(), ActCameraGaleria::class.java)
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri: Uri? = data?.getParcelableExtra("image_uri")
            imageUri?.let {

            }
        }
    }
}