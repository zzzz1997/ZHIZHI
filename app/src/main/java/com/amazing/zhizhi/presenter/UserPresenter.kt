package com.amazing.zhizhi.presenter

import cn.bmob.v3.exception.BmobException
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.listener.ResetListener
import com.amazing.zhizhi.listener.SignListener
import com.amazing.zhizhi.model.UserModel
import com.amazing.zhizhi.ui.base.AbstractUserView

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户操作Presenter
 *
 * @author zzzz
 */
class UserPresenter(val userView: AbstractUserView) : IUserPresenter {
    override fun login(username: String, password: String) {
        userView.newDialog()
        UserModel.getInstance().login(username, password, object: SignListener{
            override fun onSuccess(user: User) {
                userView.onSuccess(user)
            }

            override fun onFailed(e: BmobException) {
                userView.onFailure(e)
            }
        })
        userView.dismissDialog()
    }

    override fun register(username: String, email: String, password: String) {
        userView.newDialog()
        UserModel.getInstance().register(username, email, password, object: SignListener{
            override fun onSuccess(user: User) {
                userView.onSuccess(user)
            }

            override fun onFailed(e: BmobException) {
                userView.onFailure(e)
            }
        })
        userView.dismissDialog()
    }

    override fun reset(email: String) {
        userView.newDialog()
        UserModel.getInstance().reset(email, object: ResetListener{
            override fun onSuccess() {
                userView.onSuccess(null)
            }

            override fun onFailed(e: BmobException) {
                userView.onFailure(e)
            }
        })
        userView.dismissDialog()
    }
}