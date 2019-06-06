package com.amazing.zhizhi.listener

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 定时器监听器
 *
 * @author zzzz
 */
interface TimerListener {

    /**
     * 定时器步进事件
     *
     * @param time 事件文本
     */
    fun onTick(time: String)

    /**
     * 定时器结束
     */
    fun onFinish()
}