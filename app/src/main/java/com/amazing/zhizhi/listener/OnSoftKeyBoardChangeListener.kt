package com.amazing.zhizhi.listener

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 软键盘状态监听器
 *
 * @author zzzz
 */
interface OnSoftKeyBoardChangeListener {

    /**
     * 软键盘出现
     *
     * @param height 高度
     */
    fun keyBoardShow(height: Int)

    /**
     * 软键盘隐藏
     *
     * @param height 高度
     */
    fun keyBoardHide(height: Int)
}