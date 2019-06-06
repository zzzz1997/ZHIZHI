package com.amazing.zhizhi.adapter.base

import android.support.v7.widget.RecyclerView
import android.view.View
import com.amazing.zhizhi.utils.MyCode

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 基础适配器
 *
 * @author zzzz
 */
abstract class BaseAdapter<in T, R: BaseViewHolder, in C: OnBaseClickListener>(private val lists: MutableList<T>)
    : RecyclerView.Adapter<R>(), View.OnClickListener {

    // RecyclerView的header
    var header: View? = null

    override fun getItemCount(): Int {
        return if(header == null) {
            lists.size
        } else {
            lists.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(header == null) {
            return MyCode.RECYCLER_TYPE_DATA
        }

        return if(position == 0) {
            MyCode.RECYCLER_TYPE_HEADER
        } else {
            MyCode.RECYCLER_TYPE_DATA
        }
    }

    /**
     * RecyclerView插入操作
     *
     * @param bmobObject 插入对象
     * @param position 插入位置
     */
    fun insert(bmobObject: T, position: Int){
        lists.add(position, bmobObject)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, lists.size - position)
    }

    /**
     * RecyclerView删除操作
     *
     * @param position 删除位置
     */
    fun delete(position: Int){
        lists.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, lists.size - position)
    }

    /**
     * RecyclerView局部刷新
     *
     * @param position 刷新位置
     */
    fun fresh(position: Int){
        notifyItemChanged(position)
    }

    /**
     * 获取数据的位置
     *
     * @param holder 当前holder
     */
    fun getDataPosition(holder: R): Int {
        val position = holder.layoutPosition
        return if(header == null) {
            position
        } else {
            position - 1
        }
    }

    /**
     * RecyclerView添加头
     *
     * @param header 头布局
     */
    fun addHeader(header: View) {
        this.header = header
        notifyItemInserted(0)
    }

    /**
     * 设置RecyclerView的点击监听器
     *
     * @param onBaseClickListener 监听器对象
     */
    abstract fun setOnBaseClickListener(onBaseClickListener: C)
}