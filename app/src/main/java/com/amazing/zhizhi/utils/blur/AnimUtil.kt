package com.amazing.zhizhi.utils.blur

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewAnimationUtils
import android.animation.Animator
import com.amazing.zhizhi.R
import com.amazing.zhizhi.ui.application.MyApplication

/**
 * Project ZhiZhi
 * Date 2018/4/8
 *
 * 动画工具
 *
 * @author zzzz
 */
object AnimUtil {

    /**
     * 显示动画
     *
     * @param cancel 取消按钮
     * @param back 背景
     * @param duration 动画时长
     */
    fun show(cancel: View, back: View, duration: Long) {
        // 获取应用上下文
        val context = MyApplication.getInstance().applicationContext

        // 取消按钮旋转动画
        val anim = ObjectAnimator.ofFloat(cancel, "rotation", 0f, 135f)
        anim.duration = duration

        // 设置控件可见
        cancel.visibility = View.VISIBLE
        back.alpha = 0f
        back.visibility = View.VISIBLE

        // 背景展开动画
        back.post {
            val animator = ViewAnimationUtils.createCircularReveal(back, back.width / 2,
                    back.height - context.resources.getDimensionPixelSize(R.dimen.ah_icon_margin_bottom) - context.resources.getDimensionPixelSize(R.dimen.ah_icon_size) / 2,
                    0f, (back.height / 2).toFloat())
            animator.duration = duration
            // animator.interpolator = AccelerateInterpolator()
            back.alpha = 1f
            animator.start()
        }

        // 动画开始
        anim.start()
    }

    /**
     * 隐藏动画
     *
     * @param cancel 取消按钮
     * @param back 背景
     * @param duration 动画时长
     */
    fun hide(cancel: View, back: View, duration: Long) {
        // 获取应用上下文
        val context = MyApplication.getInstance().applicationContext

        // 取消按钮旋转动画
        val anim = ObjectAnimator.ofFloat(cancel, "rotation", 135f, 0f)
        anim.duration = duration

        // 背景收缩动画
        val animator = ViewAnimationUtils.createCircularReveal(back, back.width / 2,
                back.height - context.resources.getDimensionPixelSize(R.dimen.ah_icon_margin_bottom) - context.resources.getDimensionPixelSize(R.dimen.ah_icon_size) / 2,
                Math.hypot(back.width.toDouble(), back.height.toDouble()).toFloat(), 0f)
        animator.duration = duration
        animator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // 动画结束，控件隐藏
                cancel.visibility = View.GONE
                back.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        // 动画开始
        anim.start()
        animator.start()
    }
}