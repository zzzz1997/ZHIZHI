package com.amazing.zhizhi.utils.blur

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.RectF
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * Project ZhiZhi
 * Date 2018/4/8
 *
 * 图片处理工具
 *
 * @author zzzz
 */
object BitmapUtil {

    /**
     * 将Drawable对象转化为Bitmap对象
     *
     * @param drawable Drawable对象
     * @return 对应的Bitmap对象
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap: Bitmap

        //如果本身就是BitmapDrawable类型 直接转换即可
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        //取得Drawable固有宽高
        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            //创建一个1x1像素的单位色图
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            //直接设置一下宽高和ARGB
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        //重新绘制Bitmap
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


    /**
     * 模糊ImageView
     *
     * @param context
     * @param img     ImageView
     * @param level   模糊等级【0 ~ 25之间】
     */
    fun blurImageView(context: Context, img: ImageView, level: Float) {
        // 将图片处理成模糊
        val bitmap = BlurBitmapUtil.blurBitmap(context, drawableToBitmap(img.drawable), level)
        if (bitmap != null) {
            img.setImageBitmap(bitmap)
        }
    }

    /**
     * 模糊ImageView
     *
     * @param context
     * @param img     ImageView
     * @param level   模糊等级【0 ~ 25之间】
     * @param color   为ImageView蒙上一层颜色
     */
    fun blurImageView(context: Context, img: ImageView, level: Float, color: Int) {
        // 将图片处理成模糊
        val bitmap = BlurBitmapUtil.blurBitmap(context, drawableToBitmap(img.drawable), level)
        if (bitmap != null) {
            val drawable = coverColor(context, bitmap, color)
            img.setImageDrawable(drawable)
        } else {
            img.setImageBitmap(null)
            img.setBackgroundColor(color)
        }
    }

    /**
     * 将bitmap转成蒙上颜色的Drawable
     *
     * @param context
     * @param bitmap
     * @param color   要蒙上的颜色
     * @return Drawable
     */
    private fun coverColor(context: Context, bitmap: Bitmap, color: Int): Drawable {
        val paint = Paint()
        paint.color = color
        val rect = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        Canvas(bitmap).drawRoundRect(rect, 0f, 0f, paint)
        return BitmapDrawable(context.resources, bitmap)
    }
}