package com.amazing.zhizhi.handler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.bmob.push.PushConstants
import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.Notification
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 系统推送的接受类
 *
 * @author zzzz
 */
class MyPushMessageReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action == PushConstants.ACTION_MESSAGE) {
            // 获取Notification对象
            val notification = Gson().fromJson(intent.getStringExtra(context!!.getString(R.string.msg)), Notification::class.java)
            // 发送消息
            EventBus.getDefault().post(notification)
        }
    }
}