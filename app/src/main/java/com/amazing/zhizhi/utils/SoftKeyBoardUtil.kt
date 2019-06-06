package com.amazing.zhizhi.utils

import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.amazing.zhizhi.listener.OnSoftKeyBoardChangeListener

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 软键盘控制类
 *
 * @author zzzz
 */
class SoftKeyBoardUtil(activity: AppCompatActivity) {

    // 根布局
    private var rootView: View = activity.window.decorView
    // 根布局可见高度
    private var rootViewVisibleHeight = 0
    // 声明监听器
    private lateinit var onSoftKeyBoardListener: OnSoftKeyBoardChangeListener

    init {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)

            val visibleHeight = r.height()
            if(rootViewVisibleHeight == 0){
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            if(rootViewVisibleHeight == visibleHeight){
                return@addOnGlobalLayoutListener
            }

            if(rootViewVisibleHeight - visibleHeight > 200){
                onSoftKeyBoardListener.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            if(visibleHeight - rootViewVisibleHeight > 200){
                onSoftKeyBoardListener.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }
        }
    }

    /**
     * 设置监听器
     *
     * @param onSoftKeyBoardListener 监听器对象
     */
    fun setOnSoftKeyBoardChangeListener(onSoftKeyBoardListener: OnSoftKeyBoardChangeListener){
        this.onSoftKeyBoardListener = onSoftKeyBoardListener
    }
}