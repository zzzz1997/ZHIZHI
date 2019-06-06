package com.amazing.zhizhi.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.TextView
import com.amazing.zhizhi.R

/**
 * Project ZhiZhi
 * Date 2018/4/27
 *
 * 自定义图像文本控件
 *
 * @author zzzz
 */
class DrawableTextView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : TextView(context, attrs, defStyleAttr, defStyleRes) {

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
        val width = attr.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, -1)
        val height = attr.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, -1)

        var textDrawable: Drawable? = null

        for (drawable in compoundDrawables){
            if (drawable != null) {
                textDrawable = drawable
            }
        }

        val notNull = textDrawable != null && width != -1 && height != -1
        if (notNull) {
            textDrawable!!.setBounds(0, 0, width, height)
        }

        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])

        attr.recycle()
    }
}