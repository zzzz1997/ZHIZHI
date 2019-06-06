package com.amazing.zhizhi.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import com.amazing.zhizhi.R
import com.amazing.zhizhi.listener.TimerListener
import com.amazing.zhizhi.utils.MyCountDownTimer
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 欢迎界面的活动
 *
 * @author zzzz
 */
class SplashActivity : Activity() {

    // SharedPreferences存储表名
    private val SETTING = "setting"

    // isFirst存储对象名
    private val IS_FIRST = "isFirst"

    // 倒计时时间
    private val COUNT_TIME = 3

    // SharedPreferences
    private lateinit var shared: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // 是否第一次使用
    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initView()
    }

    /**
     * 初始化界面
     */
    @SuppressLint("CommitPrefEdits")
    private fun initView(){
        // 开启定时器
        myCountDownTimer.start()

        // 获取SharedPreferences对象
        shared = getSharedPreferences(SETTING, Context.MODE_PRIVATE)
        editor = shared.edit()

        isFirst = shared.getBoolean(IS_FIRST, true)

        activity_splash_count_down.setOnClickListener {
            myCountDownTimer.cancel()
            startApp()
        }
    }

    // 实例化定时器
    private val myCountDownTimer = MyCountDownTimer(COUNT_TIME, object: TimerListener{
        @SuppressLint("SetTextI18n")
        override fun onTick(time: String) {
            activity_splash_count_down.text = time + getString(R.string.skip)
        }

        override fun onFinish() {
            startApp()
        }
    })

    /**
     * 进入应用
     */
    private fun startApp(){
        if(isFirst) {
            startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            editor.putBoolean(IS_FIRST, false)
            editor.commit()
        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }

        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // 禁用返回键
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消定时器
        myCountDownTimer.cancel()
    }
}