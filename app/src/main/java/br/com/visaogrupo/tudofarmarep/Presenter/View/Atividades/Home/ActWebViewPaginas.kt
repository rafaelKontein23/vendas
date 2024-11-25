package br.com.visaogrupo.tudofarmarep.Views.Activitys

import android.os.Bundle
import android.preference.PreferenceManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs

class ActWebViewPaginas : AppCompatActivity() {
    private  lateinit var webview : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_act_web_view_paginas)
        webview = findViewById(R.id.webViewPaginas)
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var cnpj = prefs.getString("cnpjfim", "")
        var cel = prefs.getString("celfinal", "")
        cnpj = cnpj!!.replace("/", "").replace(".", "").replace("-", "")
        cel = cel!!.replace("(", "").replace(")", "").replace(" ", "")
        val urlmob = prefs.getString("URL_Webviewmob", URLs.URL_Webviewmob)

        val bundle = intent.getStringExtra("urlweb")
        val urlweb = urlmob + cnpj + "&celular=" + cel + "&origemMobile=" + "true" + "&ReturnUrl=" + bundle
        webview.webViewClient = WebViewClient()
        webview.getSettings().setJavaScriptEnabled(true)

        webview.loadUrl(urlweb)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}