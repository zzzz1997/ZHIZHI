package com.amazing.zhizhi.listener

import cn.bmob.v3.exception.BmobException
import com.amazing.zhizhi.entity.User

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户登录注册的监听器
 *
 * @author zzzz
 */
interface SignListener {

    /**
     * 登录注册成功
     *
     * @param user 用户对象
     */
    fun onSuccess(user: User)

    /**
     * 登录注册失败
     *
     * @param e 错误信息
     */
    fun onFailed(e: BmobException)
}