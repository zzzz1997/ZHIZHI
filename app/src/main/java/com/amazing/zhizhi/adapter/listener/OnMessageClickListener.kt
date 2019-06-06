package com.amazing.zhizhi.adapter.listener

import com.amazing.zhizhi.adapter.base.OnBaseClickListener

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 消息点击监听器
 *
 * @author zzzz
 */
interface OnMessageClickListener : OnBaseClickListener {

    /**
     * 内容点击事件
     *
     * @param position 在RecyclerView中的位置
     */
    fun onClicked(position: Int)

    /**
     * 内容长按事件
     *
     * @param position 在RecyclerView中的位置
     */
    fun onLongClicked(position: Int)

    /**
     * 置顶点击事件
     *
     * @param position 在RecyclerView中的位置
     */
    fun onTopClicked(position: Int)

    /**
     * 删除点击事件
     *
     * @param position 在RecyclerView中的位置
     */
    fun onDeleteClicked(position: Int)
}