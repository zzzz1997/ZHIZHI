package com.amazing.zhizhi.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.amazing.zhizhi.R
import es.dmoral.toasty.Toasty

/**
 * Project ZhiZhi
 * Date 2018/4/9
 *
 * 活动相关工具
 *
 * @author zzzz
 */
object ActivityUtil {

    fun startActivityByUri(context: Context, uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        // 判断activity是否存在
        if(intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toasty.error(context, context.getString(R.string.unknown_uri)).show()
        }
    }
}