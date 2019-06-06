package com.amazing.zhizhi.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.presenter.UserPresenter
import com.amazing.zhizhi.ui.base.AbstractUserView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 登录页面
 *
 * @author zzzz
 */
class LoginActivity : AbstractUserView() {
    override fun setContentView(): Int {
        return R.layout.activity_login
    }

    override fun dialogText(): Int {
        return R.string.login
    }

    override fun initView() {
        // 初始化Presenter
        userPresenter = UserPresenter(this)

        activity_login_toolbar.setNavigationOnClickListener {
            finish()
        }

        activity_login_name.hint = getString(R.string.name)
        val nameEdit = activity_login_name.editText!!

        activity_login_password.hint = getString(R.string.password)
        val passwordEdit = activity_login_password.editText!!

        activity_login_forget_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetActivity::class.java))
        }

        activity_login_quick_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        activity_login_login.setOnClickListener {
            if(nameEdit.text.toString().length < 2){
                activity_login_name.error = getString(R.string.name_too_short)
                activity_login_name.isErrorEnabled = true
                return@setOnClickListener
            }
            activity_login_name.isErrorEnabled = false
            if(passwordEdit.text.toString().length < 6){
                activity_login_password.error = getString(R.string.password_too_short)
                activity_login_password.isErrorEnabled = true
                return@setOnClickListener
            }
            activity_login_password.isErrorEnabled = false

            // 登录
            userPresenter.login(activity_login_name.editText!!.text.toString(), activity_login_password.editText!!.text.toString())
        }
    }

    override fun onSuccess(user: User?) {
        // 登录成功，返回用户对象
        Toasty.success(this, getString(R.string.login_success)).show()
        val intent = Intent()
        intent.putExtra("user", user!!)
        setResult(AppCompatActivity.RESULT_OK, intent)
        finish()
    }
}