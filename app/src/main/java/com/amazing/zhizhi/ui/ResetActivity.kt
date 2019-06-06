package com.amazing.zhizhi.ui

import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.presenter.UserPresenter
import com.amazing.zhizhi.ui.base.AbstractUserView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_reset.*
import java.util.regex.Pattern

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 重置密码页面
 *
 * @author zzzz
 */
class ResetActivity : AbstractUserView() {

    override fun setContentView(): Int {
        return R.layout.activity_reset
    }

    override fun dialogText(): Int {
        return  R.string.reset
    }

    override fun initView() {

        // 初始化Presenter
        userPresenter = UserPresenter(this)

        activity_reset_toolbar.setNavigationOnClickListener {
            finish()
        }

        activity_reset_email.hint = getString(R.string.forget_email)
        val emailEdit = activity_reset_email.editText!!

        activity_reset_reset.setOnClickListener {
            val pattern = Pattern.compile("^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
                    "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$")
            if(!pattern.matcher(emailEdit.text.toString()).matches()){
                activity_reset_email.error = getString(R.string.email_wrong_format)
                activity_reset_email.isErrorEnabled = true
                return@setOnClickListener
            }
            activity_reset_email.isErrorEnabled = false

            // 重置密码操作
            userPresenter.reset(emailEdit.text.toString())
        }
    }

    override fun onSuccess(user: User?) {
        // 请求重置密码成功，返回登录界面
        Toasty.success(this, getString(R.string.reset_success)).show()
        finish()
    }
}