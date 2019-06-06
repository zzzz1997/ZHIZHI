package com.amazing.zhizhi.handler

import android.content.Context
import cn.bmob.newim.event.MessageEvent
import cn.bmob.newim.event.OfflineMessageEvent
import cn.bmob.newim.listener.BmobIMMessageHandler
import cn.bmob.v3.exception.BmobException
import com.amazing.zhizhi.listener.BaseListener
import com.amazing.zhizhi.model.UserModel
import org.greenrobot.eventbus.EventBus

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 消息接收控制器
 *
 * @author zzzz
 */
class MyMessageHandler(val context: Context) : BmobIMMessageHandler() {

    override fun onMessageReceive(p0: MessageEvent?) {
        if(p0 != null) {
            executeMessage(p0)
        }
    }

    override fun onOfflineReceive(p0: OfflineMessageEvent?) {
        if(p0 != null) {
            val map = p0.eventMap
            for(entry in map.entries) {
                val list = entry.value
                for(event in list) {
                    executeMessage(event)
                }
            }
        }
    }

    /**
     * 分类处理消息
     *
     * @param event 消息对象
     */
    private fun executeMessage(event: MessageEvent) {
        UserModel.getInstance().update(event, object: BaseListener{
            override fun done(e: BmobException?) {
                executeSdkMessage(event)
            }
        })
    }

    /**
     * 处理系统消息
     *
     * @param event 消息对象
     */
    private fun executeSdkMessage(event: MessageEvent) {
        EventBus.getDefault().post(event)
    }
}