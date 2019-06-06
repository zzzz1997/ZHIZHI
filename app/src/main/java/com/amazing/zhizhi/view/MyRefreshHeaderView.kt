package com.amazing.zhizhi.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import com.amazing.zhizhi.R
import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout
import android.widget.TextView

/**
 * Project ZhiZhi
 * Date 2018/4/6
 *
 * 下拉刷新header
 *
 * @author zzzz
 */
class MyRefreshHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : SwipeRefreshHeaderLayout(context, attrs, defStyleAttr) {

    // header高度
    private val headerHeight = resources.getDimensionPixelOffset(R.dimen.refresh_header_height)
    // 下拉上拉动画
    private val rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up)
    private val rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down)

    // 声明控件
    private lateinit var icon: ImageView
    private lateinit var success: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var text: TextView

    // 是否旋转
    private var rotated = false

    override fun onFinishInflate() {
        super.onFinishInflate()

        // 获取控件对象
        icon = findViewById(R.id.header_icon)
        success = findViewById(R.id.header_success)
        progressBar = findViewById(R.id.header_progressbar)
        text = findViewById(R.id.header_text)
    }

    override fun onRefresh() {
        super.onRefresh()

        // 刷新
        icon.clearAnimation()
        icon.visibility = View.GONE
        success.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        text.text = context.getString(R.string.refreshing)
    }

    override fun onMove(y: Int, isComplete: Boolean, automatic: Boolean) {
        super.onMove(y, isComplete, automatic)

        // 滑动
        if(!isComplete) {
            icon.visibility = View.VISIBLE
            success.visibility = View.GONE
            progressBar.visibility = View.GONE

            if(y > headerHeight) {
                text.text = context.getString(R.string.release_to_refresh)

                if(!rotated) {
                    icon.clearAnimation()
                    icon.startAnimation(rotateUp)
                    rotated = true
                }
            } else {
                text.text = context.getString(R.string.swipe_to_refresh)

                if(rotated) {
                    icon.clearAnimation()
                    icon.startAnimation(rotateDown)
                    rotated = false
                }
            }
        }
    }

    override fun onComplete() {
        super.onComplete()

        // 完成
        icon.clearAnimation()
        icon.visibility = View.GONE
        success.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        text.text = context.getString(R.string.refresh_success)
        rotated = false
    }

    override fun onReset() {
        super.onReset()

        // 重置
        icon.clearAnimation()
        icon.visibility = View.GONE
        success.visibility = View.GONE
        progressBar.visibility = View.GONE
        rotated = false
    }
}