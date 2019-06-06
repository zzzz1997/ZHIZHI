package com.amazing.zhizhi.ui

import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.presenter.UserPresenter
import com.amazing.zhizhi.ui.base.AbstractUserView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 注册页面
 *
 * @author zzzz
 */
class RegisterActivity : AbstractUserView() {
    override fun setContentView(): Int {
        return R.layout.activity_register
    }

    override fun dialogText(): Int {
        return R.string.register
    }

    override fun initView() {
        // 初始化Presenter
        userPresenter = UserPresenter(this)

        activity_register_toolbar.setNavigationOnClickListener {
            finish()
        }

        activity_register_name.hint = getString(R.string.name)
        val nameEdit = activity_register_name.editText!!

        activity_register_password.hint = getString(R.string.password)
        val passwordEdit = activity_register_password.editText!!

        activity_register_password_again.hint = getString(R.string.password_again)
        val againEdit = activity_register_password_again.editText!!

        activity_register_email.hint = getString(R.string.email)
        val emailEdit = activity_register_email.editText!!

        activity_register_register.setOnClickListener {
            if(nameEdit.text.toString().length < 2){
                activity_register_name.error = getString(R.string.name_too_short)
                activity_register_name.isErrorEnabled = true
                return@setOnClickListener
            }
            activity_register_name.isErrorEnabled = false
            val pattern = Pattern.compile("^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
                    "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$")
            if(!pattern.matcher(emailEdit.text.toString()).matches()){
                activity_register_email.error = getString(R.string.email_wrong_format)
                activity_register_email.isErrorEnabled = true
                return@setOnClickListener
            }
            activity_register_email.isErrorEnabled = false
            if(passwordEdit.text.toString().length < 6){
                activity_register_password.error = getString(R.string.password_too_short)
                activity_register_password.isErrorEnabled = true
                return@setOnClickListener
            }
            activity_register_password.isErrorEnabled = false
            if(againEdit.text.toString() != passwordEdit.text.toString()){
                activity_register_password_again.error = getString(R.string.password_not_matching)
                activity_register_password_again.isErrorEnabled = true
                return@setOnClickListener
            }
            activity_register_password_again.isErrorEnabled = false

            // 注册操作
            userPresenter.register(nameEdit.text.toString(), emailEdit.text.toString(), passwordEdit.text.toString())
        }
    }

    override fun onSuccess(user: User?) {
        // 注册成功返回登录界面
        Toasty.success(this@RegisterActivity, getString(R.string.register_success)).show()
        finish()
    }
}