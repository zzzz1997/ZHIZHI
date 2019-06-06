package com.amazing.zhizhi.adapter.listener

import android.view.View
import com.amazing.zhizhi.adapter.base.OnBaseClickListener

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 聊天记录点击监听器
 *
 * @author zzzz
 */
interface OnTalkClickListener : OnBaseClickListener {

    /**
     * 对方头像点击事件
     */
    fun onUserIconClicked()

    /**
     * 己方头像点击事件
     */
    fun onMeIconClicked()

    /**
     * 消息内容长按时间
     *
     * @param view 点击的视图对象
     * @param position 点击位置
     */
    fun onLongClicked(view: View, position: Int)
}