package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActCameraGaleria
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.databinding.FragmentFotoDocumentoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition




class FotoDocumentoFragment : Fragment() {

    private var _binding: FragmentFotoDocumentoBinding? = null
    private val binding get() = _binding!!
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    private val REQUEST_IMAGE_CODE = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFotoDocumentoBinding.inflate(inflater, container, false)
        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelActCabecalho.mudaProgressoCadastro(3, 1f)


        if(FormularioCadastro.fotoDocumeto != Uri.EMPTY){
            binding.fotoDocumento.isVisible = true
            binding.fundo.isVisible = false
            binding.btnContinuar.setBackgroundResource(R.drawable.bordas_8_blue600)
            Glide.with(this)
                .asBitmap()
                .load(FormularioCadastro.fotoDocumeto)
                .into(object : com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.fotoDocumento.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        binding.fotoDocumento.setImageDrawable(placeholder)
                    }
                })
          }else{
            binding.btnContinuar.setBackgroundResource(R.drawable.bordas_radius_8_solid_blue300)

        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CAMERA), 4);
        }


        binding.adiconarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CAMERA), 4);
            }else{
                val intent = Intent(requireContext(), ActCameraGaleria::class.java)
                startActivityForResult(intent, REQUEST_IMAGE_CODE)
            }

    }

        binding.btnContinuar.setOnClickListener {
            if (FormularioCadastro.fotoDocumeto == Uri.EMPTY){
                Toast.makeText(requireContext(), getString(R.string.erroFoto), Toast.LENGTH_SHORT).show()
            }else{
                val dadosAreaAtuacao = DadosAreaDeAtuacaoFragment.newInstance(true)

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerCadastro, dadosAreaAtuacao)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri: Uri? = data?.getParcelableExtra("image_uri")
            binding.fotoDocumento.isVisible = true
            binding.fundo.isVisible = false
            imageUri?.let {
                Glide.with(this)
                    .asBitmap()
                    .load(imageUri)
                    .into(object : com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            binding.fotoDocumento.setImageBitmap(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            binding.fotoDocumento.setImageDrawable(placeholder)
                        }
                    })

            }
            binding.btnContinuar.setBackgroundResource(R.drawable.bordas_8_blue600)

        }
    }
}