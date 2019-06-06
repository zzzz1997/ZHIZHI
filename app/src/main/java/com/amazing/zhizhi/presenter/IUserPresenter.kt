package com.amazing.zhizhi.presenter

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户操作Presenter接口
 *
 * @author zzzz
 */
interface IUserPresenter {

    /**
     * 登录操作
     *
     * @param username 用户名
     * @param password 密码
     */
    fun login(username: String, password: String)

    /**
     * 注册操作
     *
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     */
    fun register(username: String, email: String, password: String)

    /**
     * 重置密码操作
     *
     * @param email 邮箱
     */
    fun reset(email: String)
}