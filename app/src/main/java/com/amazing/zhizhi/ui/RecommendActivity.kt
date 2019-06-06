package com.amazing.zhizhi.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amazing.zhizhi.R
import kotlinx.android.synthetic.main.activity_recommend.*

/**
 * Project ZhiZhi
 * Date 2018/6/6
 *
 * 推荐目标界面
 *
 * @author zzzz
 */
class RecommendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        initView()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        activity_recommend_text.text = intent.data!!.getQueryParameter("data")
    }
}