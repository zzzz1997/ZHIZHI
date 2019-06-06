package com.amazing.zhizhi.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amazing.zhizhi.R
import kotlinx.android.synthetic.main.activity_banner.*

/**
 * Project ZhiZhi
 * Date 2018/4/9
 *
 * banner利用uri跳转界面
 *
 * @author zzzz
 */
class BannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        initView()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        activity_banner_text.text = intent.data!!.getQueryParameter("data")
    }
}