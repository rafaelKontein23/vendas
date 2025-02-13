package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.ImagensUltis
import br.com.visaogrupo.tudofarmarep.databinding.ActivityActCameraGaleriaBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class ActCameraGaleria : AppCompatActivity() {
    private lateinit var imageCapture: ImageCapture
    private val PICK_IMAGE_REQUEST = 1
    private var isUsingFrontCamera = false // Controle para alternar câmera

    private val binding by lazy {
        ActivityActCameraGaleriaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val viraCamera = intent.getBooleanExtra("viraCamera", false)
        isUsingFrontCamera = viraCamera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), 4);
        }


        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        binding.viraCamera.setOnClickListener {
            toggleCamera()
        }
        binding.imgGaleria.setOnClickListener {
            if (hasStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }

        binding.btnTiraFoto.setOnClickListener {
           capturePhoto()
        }

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.camera.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector =  if (isUsingFrontCamera) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("CameraX", "Erro ao inicializar a câmera.", exc)
            }
        }, ContextCompat.getMainExecutor(this))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun toggleCamera() {
        isUsingFrontCamera = !isUsingFrontCamera

        val cameraSelector = if (isUsingFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        startCamera(cameraSelector)
    }
    private fun startCamera(cameraSelector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Liberar qualquer uso anterior da câmera
            cameraProvider.unbindAll()

            try {
                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(binding.camera.surfaceProvider)
                }

                // Configurar o ImageCapture
                imageCapture = ImageCapture.Builder().build()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraX", "Erro ao iniciar a câmera: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasStoragePermission()) {
                val permissions = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                    else -> {
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
                ActivityCompat.requestPermissions(this, permissions, 1001)
            }
        }
    }

    private fun openGallery() {
        if (hasStoragePermission()) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        } else {
            requestStoragePermission()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    // Mostra uma mensagem explicando a necessidade da permissão
                    Toast.makeText(this, "Permissão necessária para acessar a galeria", Toast.LENGTH_SHORT).show()
                    requestStoragePermission()
                } else {
                    // Abre as configurações se o usuário escolheu "Não perguntar novamente"
                    Toast.makeText(this, "Permissão negada. Ative-a nas configurações.", Toast.LENGTH_SHORT).show()
                    openAppSettings()
                }
            }
        }
    }
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!

            val tempFile = ImagensUltis.copyUriToInternalStorage(this, selectedImageUri)

            if (tempFile != null) {
                println("Arquivo copiado: ${tempFile.absolutePath}")

                val base64String = ImagensUltis.fileToBase64(tempFile)
                FormularioCadastro.base64Galeria = base64String.toString()
            } else {
                println("Erro ao copiar o arquivo da URI.")
            }
            val resultIntent = Intent().apply {
                putExtra("image_uri", selectedImageUri)
            }
            FormularioCadastro.fotoDocumeto = selectedImageUri
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }


    private fun capturePhoto() {
        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())}.jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val resultIntent = Intent().apply {
                        putExtra("image_uri", savedUri)
                    }
                    FormularioCadastro.base64Galeria = ""
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Erro ao capturar imagem: ${exception.message}", exception)
                }
            }
        )
    }

}
