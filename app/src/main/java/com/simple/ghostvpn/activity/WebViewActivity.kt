package com.simple.ghostvpn.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.simple.ghostvpn.R
import com.simple.ghostvpn.util.ktx.immersiveStatusBar
import com.simple.ghostvpn.util.ktx.statusBarPadding

class WebViewActivity : AppCompatActivity() {
    private lateinit var ivBack: AppCompatImageView
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        immersiveStatusBar(false)

        ivBack = findViewById(R.id.ivBack)
        ivBack.statusBarPadding()
        ivBack.setOnClickListener { finish() }

        webView = findViewById(R.id.webView)
        webView.loadUrl(URL)
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }
    }

    companion object {
        private const val URL =
            "https://res.openvpnsafeconnect.com/PrivacyPolicy.html"
        fun start(context: Context) {
            context.startActivity(Intent(context, WebViewActivity::class.java))
        }
    }
}