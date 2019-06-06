package com.amazing.zhizhi.model

import com.amazing.zhizhi.listener.ResetListener
import com.amazing.zhizhi.listener.SignListener
import com.amazing.zhizhi.listener.BaseListener
import cn.bmob.newim.event.MessageEvent
import com.amazing.zhizhi.listener.QueryUserListener


/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户操作接口
 *
 * @author zzzz
 */
interface IUserModel {

    /**
     * 登录操作
     *
     * @param username 用户名
     * @param password 密码
     * @param signListener 登录监听器
     */
    fun login(username: String, password: String, signListener: SignListener)

    /**
     * 注册操作
     *
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @param signListener 注册监听器
     */
    fun register(username: String, email: String, password: String, signListener: SignListener)

    /**
     * 重置密码操作
     *
     * @param email 邮箱
     * @param resetListener 重置密码监听器
     */
    fun reset(email: String, resetListener: ResetListener)

    /**
     * 更新用户资料监听器
     *
     * @param event 消息事件
     * @param baseListener 操作完成监听器
     */
    fun update(event: MessageEvent, baseListener: BaseListener)

    /**
     * 查询用户信息
     *
     * @param objectId 用户id
     * @param queryUserListener 查询监听器
     */
    fun queryUser(objectId: String, queryUserListener: QueryUserListener)
}