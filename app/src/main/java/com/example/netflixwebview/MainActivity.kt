package com.example.netflixwebview

import android.app.AlertDialog
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        setupWebView()

        showTokenInputDialog()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitOrNewTokenDialog()
            }
        })
    }

    private fun setupWebView() {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        // Set User Agent to resemble a Desktop Chrome browser
        webSettings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        // Enable third-party cookies for modern web compat
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
    }

    private fun showTokenInputDialog() {
        val editText = EditText(this)
        editText.hint = "Paste Netflix URL with nftoken"

        AlertDialog.Builder(this)
            .setTitle("Enter Netflix URL")
            .setMessage("Paste the full Netflix URL containing the nftoken.")
            .setView(editText)
            .setCancelable(false)
            .setPositiveButton("Load") { dialog, _ ->
                val url = editText.text.toString().trim()
                if (url.isNotEmpty()) {
                    webView.loadUrl(url)
                } else {
                    showTokenInputDialog() // Show again if empty
                }
                dialog.dismiss()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .show()
    }

    private fun showExitOrNewTokenDialog() {
        AlertDialog.Builder(this)
            .setTitle("Options")
            .setMessage("Do you want to enter a new URL or exit the app?")
            .setPositiveButton("New URL") { dialog, _ ->
                showTokenInputDialog()
                dialog.dismiss()
            }
            .setNegativeButton("Exit App") { _, _ ->
                finish()
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
