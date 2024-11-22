package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs


class FragmentsWebView : Fragment() {
    private  lateinit var webview : WebView
    private lateinit var  constrainCarregadno : ConstraintLayout

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
        var cnpj = prefs.getString("cnpjfim", "") ?: ""
        var cel = prefs.getString("celfinal", "") ?: ""
        cnpj = cnpj.replace("/", "").replace(".", "").replace("-", "")
        cel = cel.replace("(", "").replace(")", "").replace(" ", "")
        val urlmob = URLs.URL_Webviewmob

        val bundle = arguments?.getString("urlweb")
        val urlweb = "$urlmob$cnpj&celular=$cel&origemMobile=true&ReturnUrl=$bundle"

        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                constrainCarregadno.isVisible = false


            }
        }

        webview.settings.javaScriptEnabled = true
        webview.loadUrl(urlweb)

        return view
    }

}