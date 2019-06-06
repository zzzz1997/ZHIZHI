package com.amazing.zhizhi.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import cn.bingoogolapple.bgabanner.BGALocalImageSize
import com.amazing.zhizhi.R

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 欢迎界面活动
 *
 * @author zzzz
 */
class WelcomeActivity : Activity(){

    // 控件对象
    private lateinit var background: BGABanner
    private lateinit var foreground: BGABanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initView()
    }

    /**
     *初始化界面
     */
    private fun initView() {

        // 获取控件
        background = findViewById(R.id.activity_welcome_background)
        foreground = findViewById(R.id.activity_welcome_foreground)

        // 添加banner点击事件
        foreground.setEnterSkipViewIdAndDelegate(R.id.activity_welcome_enter, R.id.activity_welcome_skip) {
                    startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    finish()
                }

        val localImageSize = BGALocalImageSize(720, 1280, 320f, 640f)

        // 设置数据
        background.setData(localImageSize,
                ImageView.ScaleType.CENTER_CROP,
                R.drawable.welcome_background_1,
                R.drawable.welcome_background_2,
                R.drawable.welcome_background_3)

        foreground.setData(localImageSize,
                ImageView.ScaleType.CENTER_CROP,
                R.drawable.welcome_foreground_1,
                R.drawable.welcome_foreground_2,
                R.drawable.welcome_foreground_3)
    }

    override fun onResume() {
        super.onResume()
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        background.setBackgroundResource(android.R.color.white)
    }
}