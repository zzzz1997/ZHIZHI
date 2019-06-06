package com.amazing.zhizhi.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.amazing.zhizhi.R
import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout

/**
 * Project ZhiZhi
 * Date 2018/4/6
 *
 * 上拉加载footer
 *
 * @author zzzz
 */
class MyRefreshFooterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : SwipeLoadMoreFooterLayout(context, attrs, defStyleAttr) {

    // footer高度
    private val footerHeader = resources.getDimensionPixelOffset(R.dimen.refresh_footer_height)

    // 声明控件
    private lateinit var progressbar: ProgressBar
    private lateinit var success: ImageView
    private lateinit var text: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()

        // 获取控件对象
        progressbar = findViewById(R.id.footer_progressbar)
        success = findViewById(R.id.footer_success)
        text = findViewById(R.id.footer_text)
    }

    override fun onPrepare() {
        super.onPrepare()

        // 准备中
        text.visibility = View.GONE
    }

    override fun onMove(y: Int, isComplete: Boolean, automatic: Boolean) {
        super.onMove(y, isComplete, automatic)

        // 滑动
        if(!isComplete) {
            progressbar.visibility = View.GONE
            success.visibility = View.GONE

            if(-y > footerHeader) {
                text.text = context.getString(R.string.release_to_load_more)
            } else {
                text.text = context.getString(R.string.swipe_to_load_more)
            }
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()

        // 加载中
        progressbar.visibility = View.VISIBLE
        text.text = context.getString(R.string.loading_more)
    }

    override fun onComplete() {
        super.onComplete()

        // 加载完成
        progressbar.visibility = View.GONE
        success.visibility = View.VISIBLE
    }

    override fun onReset() {
        super.onReset()

        // 重置
        success.visibility = View.GONE
    }
}