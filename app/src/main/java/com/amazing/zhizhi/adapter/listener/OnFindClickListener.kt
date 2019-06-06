package com.amazing.zhizhi.adapter.listener

import com.amazing.zhizhi.adapter.base.OnBaseClickListener

/**
 * Project ZhiZhi
 * Date 2018/4/11
 *
 * 发现点击监听器
 *
 * @author zzzz
 */
interface OnFindClickListener : OnBaseClickListener {

    /**
     * item点击事件
     *
     * @param position item所在的位置
     */
    fun onItemClicked(position: Int)

    /**
     * item菜单点击事件
     *
     * @param position item所在位置
     */
    fun onMenuClicked(position: Int)
}