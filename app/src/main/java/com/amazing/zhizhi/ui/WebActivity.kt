package com.amazing.zhizhi.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.webkit.*
import com.amazing.zhizhi.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Project MeiZhi
 * Date 2018-4-09
 *
 * 浏览器页面
 *
 * @author zzzz
 */

class WebActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 设置菜单
        menuInflater.inflate(R.menu.menu_web,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // 菜单点击事件
        when(item!!.itemId){
            R.id.copy -> {
                // 拷贝链接文本
                val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val cd = ClipData.newPlainText(getString(R.string.url), intent.data!!.getQueryParameter(getString(R.string.url)))
                cb.primaryClip = cd
                Toasty.success(this, getString(R.string.copy_success)).show()
            }
            R.id.open -> {
                // 在系统浏览器中打开链接
                val intent = Intent("android.intent.action.VIEW")
                intent.data = Uri.parse(activity_web_web_view.url)
                startActivity(intent)
            }
            else -> return false
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        initView()
    }

    /**
     * 初始化界面
     */
    private fun initView(){
        // 设置toolbar
        setSupportActionBar(activity_web_toolbar)
        activity_web_toolbar.setNavigationOnClickListener {
            finish()
        }

        val webSettings = activity_web_web_view.settings

        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.allowFileAccess = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"

        activity_web_web_view.loadUrl(intent.data!!.getQueryParameter(getString(R.string.url)))
        activity_web_web_view.webViewClient = object: WebViewClient(){
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view!!.loadUrl(request!!.url.toString())
                return true
            }
        }
        activity_web_web_view.webChromeClient = object: WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, titles: String?) {
                activity_web_toolbar.title = titles
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if(newProgress == 100){
                    activity_web_progress.visibility = View.GONE
                } else {
                    if(activity_web_progress.visibility == View.GONE){
                        activity_web_progress.visibility = View.VISIBLE
                    }
                    activity_web_progress.progress = newProgress
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && activity_web_web_view.canGoBack()) {
            // 浏览器点击返回事件
            activity_web_web_view.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity_web_web_view.destroy()
    }
}