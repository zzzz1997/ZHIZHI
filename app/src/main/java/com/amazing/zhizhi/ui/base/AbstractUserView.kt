package com.amazing.zhizhi.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import cn.bmob.v3.exception.BmobException
import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.presenter.UserPresenter
import es.dmoral.toasty.Toasty

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * @author zzzz
 */
abstract class AbstractUserView : AppCompatActivity() {

    // Presenter对象
    lateinit var userPresenter: UserPresenter

    // 弹窗对象
    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setContentView())

        initView()
    }

    /**
     * 新建progress dialog提示
     */
    @SuppressLint("InflateParams")
    fun newDialog() {
        val view  = layoutInflater.inflate(R.layout.dialog_progress, null)
        view.findViewById<TextView>(R.id.dialog_progress_text).text = getString(dialogText())
        dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        dialog.show()
    }

    /**
     * 取消dialog
     */
    fun dismissDialog() {
        dialog.dismiss()
    }

    /**
     * 操作失败
     *
     * @param e 错误信息
     */
    fun onFailure(e: BmobException) {
        Toasty.error(this, e.message!!).show()
    }

    /**
     * 界面资源
     *
     * @return 界面资源id
     */
    abstract fun setContentView(): Int

    /**
     * 弹窗内容
     *
     * @return 文本内容
     */
    abstract fun dialogText(): Int

    /**
     * 初始化操作
     */
    abstract fun initView()

    /**
     * 操作成功
     *
     * @param user 用户信息
     */
    abstract fun onSuccess(user: User?)
}