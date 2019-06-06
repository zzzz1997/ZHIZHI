package com.amazing.zhizhi.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amazing.zhizhi.R
import kotlinx.android.synthetic.main.activity_router.*

/**
 * Project ZhiZhi
 * Date 2018/6/6
 *
 * 路由目标界面
 *
 * @author zzzz
 */
class RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)

        initView()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        activity_router_text.text = intent.data!!.getQueryParameter("data")
    }
}