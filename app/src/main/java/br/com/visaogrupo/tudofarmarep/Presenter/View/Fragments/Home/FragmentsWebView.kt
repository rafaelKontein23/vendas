package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class FragmentsWebView : Fragment() {
    private  lateinit var webview : WebView
    private lateinit var  constrainCarregadno : ConstraintLayout
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var filePathCallbackTeste: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fragments_web_view, container, false)
        webview = view.findViewById(R.id.webViewPaginas)
        constrainCarregadno = view.findViewById(R.id.constrainCarregando)

        constrainCarregadno.isVisible = true
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        var cnpj = prefs.getString(ProjetoStrings.cnpjLogin, "") ?: ""
        var cel = prefs.getString(ProjetoStrings.celular, "") ?: ""
        cnpj = cnpj.replace("/", "").replace(".", "").replace("-", "")
        cel = cel.replace("(", "").replace(")", "").replace(" ", "")
        val urlmob = URLs.URL_Webviewmob
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        WebView.setWebContentsDebuggingEnabled(true)
        val bundle = arguments?.getString(ProjetoStrings.urlweb)
        val urlweb = "$urlmob$cnpj&celular=$cel&origemMobile=true&ReturnUrl=$bundle"

        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                constrainCarregadno.isVisible = false


            }
        }

        webview.settings.javaScriptEnabled = true
        webview.loadUrl(urlweb)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (webview.canGoBack()) {
                webview.goBack()
            } else {
                requireActivity().onBackPressed() // Chama o onBackPressed da Activity
            }
        }
        webview.setWebChromeClient(object : WebChromeClient() {

            // Método para mostrar o seletor de arquivo (por exemplo, para a câmera ou galeria)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                filePathCallbackTeste = filePathCallback
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
                return true
            }

            // Método para permissões de geolocalização
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                if (request != null) {
                    val requestedResources = request.resources
                    if (requestedResources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE) ||
                        requestedResources.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                        // Permitir o acesso à câmera e ao microfone
                        request.grant(requestedResources)
                    } else {
                        // Negar outras permissões
                        request.deny()
                    }
                }
            }

        })


        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            uri?.let {
                filePathCallbackTeste?.onReceiveValue(arrayOf(it))
            }
        } else {
            filePathCallbackTeste?.onReceiveValue(null)
        }
        filePathCallbackTeste = null
    }

}