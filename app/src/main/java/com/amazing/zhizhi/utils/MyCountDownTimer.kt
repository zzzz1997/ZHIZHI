package com.amazing.zhizhi.utils

import android.os.CountDownTimer
import com.amazing.zhizhi.listener.TimerListener

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 封装CountDownTimer类
 *
 * @author zzzz
 */
class MyCountDownTimer(time: Int, private val timerListener: TimerListener)
    : CountDownTimer((time * 1000).toLong(), 1000){

    override fun onTick(millisUntilFinished: Long) {
        timerListener.onTick("${millisUntilFinished / 1000 + 1}s")
    }

    override fun onFinish() {
        timerListener.onFinish()
    }
}