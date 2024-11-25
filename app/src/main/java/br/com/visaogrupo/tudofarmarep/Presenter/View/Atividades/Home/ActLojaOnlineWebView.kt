package br.com.visaogrupo.tudofarmarep.Views.Activitys

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.R

class ActLojaOnlineWebView : AppCompatActivity() {
    lateinit var webViewLojaOnline:WebView
    lateinit var progressBarWebView:ProgressBar
    lateinit var voltaLojaWebView:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_act_loja_online_web_view)
        webViewLojaOnline = findViewById(R.id.webViewLojaOnline)
        progressBarWebView = findViewById(R.id.progressBarWebView)
        voltaLojaWebView = findViewById(R.id.voltaLojaWebView)
        voltaLojaWebView.setOnClickListener {
            finish()
        }

        val urlLogin = intent.getStringExtra("URL_LOGIN")
        val webSettings = webViewLojaOnline.settings
        webSettings.javaScriptEnabled = true


        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true


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


        webViewLojaOnline.loadUrl(urlLogin!!)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}