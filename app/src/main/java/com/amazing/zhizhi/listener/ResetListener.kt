package com.amazing.zhizhi.listener

import cn.bmob.v3.exception.BmobException

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 重置密码监听器
 *
 * @author zzzz
 */
interface ResetListener {

    /**
     * 重置密码成功
     */
    fun onSuccess()

    /**
     * 重置密码失败
     *
     * @param e 错误信息
     */
    fun onFailed(e: BmobException)
}