package com.amazing.zhizhi.listener

import cn.bmob.v3.exception.BmobException
import com.amazing.zhizhi.entity.User

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 查询用户监听器
 *
 * @author zzzz
 */
interface QueryUserListener {

    /**
     * 查询完成
     *
     * @param user 用户对象
     * @param e 错误信息
     */
    fun done(user: User?, e: BmobException?)
}