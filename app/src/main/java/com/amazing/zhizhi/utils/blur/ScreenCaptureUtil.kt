package com.amazing.zhizhi.utils.blur

import android.graphics.Bitmap
import android.app.Activity
import android.view.*


/**
 * Project ZhiZhi
 * Date 2018/4/8
 *
 * 屏幕截图工具类
 *
 * @author zzzz
 */
object ScreenCaptureUtil {

    /**
     * 获取状态栏高度
     *
     * @param activity 活动
     * @return 高度像素
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val resources = activity.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        return resources.getDimensionPixelSize(resourceId) + getNavigationBarHeight(activity)
    }

    /**
     * 获取导航栏高度
     *
     * @param activity 活动
     * @return 高度像素
     */
    private fun getNavigationBarHeight(activity: Activity): Int {
        // 是否有菜单键
        val hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey()
        // 是否有返回键
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        val rid = activity.resources.getIdentifier("config_showNavigationBar", "bool", "android")
        val hasNavigationBar = rid != 0 && !hasMenuKey && !hasBackKey
        return if(hasNavigationBar) {
            val resourceId = activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            activity.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }


    /**
     * 获取屏幕截屏 【不包含状态栏】
     *
     * @param activity
     * @return
     */
    fun getScreenshot(activity: Activity): Bitmap? {
        return try {
            val window = activity.window
            val view = window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache(true)
            val bmp1 = view.drawingCache
            /**
             * 除去状态栏和标题栏
             */
            val height = getStatusBarHeight(activity)
            Bitmap.createBitmap(bmp1, 0, height, bmp1.width, bmp1.height - height)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取Activity截图
     *
     * @param activity
     * @return bitmap
     */
    fun getDrawing(activity: Activity): Bitmap? {
        return try {
            val view = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
            view.isDrawingCacheEnabled = true
            var tBitmap = view.drawingCache
            // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
            tBitmap = tBitmap.copy(Bitmap.Config.ARGB_8888, true)
            view.isDrawingCacheEnabled = false
            tBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
}