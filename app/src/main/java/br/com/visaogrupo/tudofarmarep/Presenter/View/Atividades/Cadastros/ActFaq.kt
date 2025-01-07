package br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs

class ActFaq : AppCompatActivity() {
    lateinit var webViewLojaOnline:WebView
    lateinit var progressBarWebView: ProgressBar
    lateinit var voltaLojaWebView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_act_faq)
        webViewLojaOnline = findViewById(R.id.webViewLojaOnline)
        progressBarWebView = findViewById(R.id.progressBarWebView)
        voltaLojaWebView = findViewById(R.id.voltaLojaWebView)
        voltaLojaWebView.setOnClickListener {
            if (webViewLojaOnline.canGoBack()) {
                webViewLojaOnline.goBack()
            } else {
                finish()
            }
        }

        val webSettings = webViewLojaOnline.settings
        webSettings.javaScriptEnabled = true


        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        // Exemplo básico de configuração da WebView
        webSettings.domStorageEnabled = true
        webViewLojaOnline.addJavascriptInterface(WebAppInterface(this), "whatspersonaandroidjs")

        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webViewLojaOnline.visibility = View.GONE
        progressBarWebView.visibility = View.VISIBLE


        webViewLojaOnline.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }

            // Método chamado quando a página termina de carregar
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBarWebView.visibility = View.GONE
                webViewLojaOnline.visibility = View.VISIBLE
            }
        }


        webViewLojaOnline.webChromeClient = WebChromeClient()


        webViewLojaOnline.loadUrl(URLs.url_suporte)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    class WebAppInterface(private val context: Context) {
        @JavascriptInterface
        fun LinkWhatsPersonal(linkWhats: String) {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkWhats))
            context.startActivity(intent)
        }
    }
    override fun onBackPressed() {
        if (webViewLojaOnline.canGoBack()) {
            webViewLojaOnline.goBack()
        } else {
            super.onBackPressed()
        }
    }

}