package com.amazing.zhizhi.listener

import cn.bmob.v3.exception.BmobException

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 基础监听器
 *
 * @author zzzz
 */
interface BaseListener {

    /**
     * 操作完成
     */
    fun done(e: BmobException?)
}