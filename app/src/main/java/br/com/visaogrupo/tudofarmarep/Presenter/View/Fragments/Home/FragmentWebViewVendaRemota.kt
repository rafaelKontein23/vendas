package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Home

import FormularioCadastro
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils


class FragmentWebViewVendaRemota : Fragment() {

    private  lateinit var webview : WebView
    private lateinit var  constrainCarregadno : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view_venda_remota, container, false)
        webview = view.findViewById(R.id.webViewPaginas)
        constrainCarregadno = view.findViewById(R.id.constrainCarregando)
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                constrainCarregadno.isVisible = false


            }
        }
        val preferenciasUtils = PreferenciasUtils(requireContext())
        val hash = preferenciasUtils.recuperarTexto(ProjetoStrings.hashLogin)
        val urlweb = "${URLs.urlpedido}${hash}"

        webview.settings.javaScriptEnabled = true
        webview.loadUrl(urlweb)
        return view
    }

}