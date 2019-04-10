package com.truckintransit.operator.activity

import android.os.Bundle
import android.view.MenuItem
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.constants.AppConstants.TERMSURL
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.content_main.*
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebViewClient




class WebViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initWebView()

        webView.loadUrl(TERMSURL)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initWebView() {
       // webView.webChromeClient = MyWebChromeClient(this)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                //invalidateOptionsMenu()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                webView.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
               // invalidateOptionsMenu()
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE
              //  invalidateOptionsMenu()
            }
        }
        webView.clearCache(true)
        webView.clearHistory()
        webView.settings.javaScriptEnabled = true
      //  webView.isHorizontalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = true

    }
}
