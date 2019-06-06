package com.amazing.zhizhi.model

import cn.bmob.newim.BmobIM
import cn.bmob.newim.event.MessageEvent
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.listener.BaseListener
import com.amazing.zhizhi.listener.QueryUserListener
import com.amazing.zhizhi.listener.ResetListener
import com.amazing.zhizhi.listener.SignListener

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户操作Model
 *
 * @author zzzz
 */
class UserModel : IUserModel {

    // 默认头像链接
    private val DEFAULT_ICON = "http://bmob-cdn-17846.b0.upaiyun.com/2018/04/05/ddb35da740366d9e8065c119e37829d3.png"

    // 无法查询用户提示
    private val CANNOT_FOUND_USER = "查无此人"

    companion object{
        // Model实例
        private val INSTANCE = UserModel()

        /**
         * 获取单例对象
         */
        fun getInstance(): UserModel{
            return INSTANCE
        }
    }

    override fun login(username: String, password: String, signListener: SignListener) {
        BmobUser.loginByAccount(username, password, object: LogInListener<User>(){
            override fun done(p0: User?, p1: BmobException?) {
                if(p1 == null) {
                    signListener.onSuccess(p0!!)
                } else {
                    signListener.onFailed(p1)
                }
            }
        })
    }

    override fun register(username: String, email: String, password: String, signListener: SignListener) {
        val user = User()
        user.username = username
        user.email = email
        user.setPassword(password)
        user.icon = DEFAULT_ICON
        user.signUp(object: SaveListener<User>(){
            override fun done(p0: User?, p1: BmobException?) {
                if(p1 == null) {
                    signListener.onSuccess(p0!!)
                } else {
                    signListener.onFailed(p1)
                }
            }
        })
    }

    override fun reset(email: String, resetListener: ResetListener) {
        BmobUser.resetPasswordByEmail(email, object: UpdateListener(){
            override fun done(p0: BmobException?) {
                if(p0 == null) {
                    resetListener.onSuccess()
                } else {
                    resetListener.onFailed(p0)
                }
            }
        })
    }

    override fun update(event: MessageEvent, baseListener: BaseListener) {
        val conversation = event.conversation
        val info = event.fromUserInfo
        val msg = event.message
        val username = info.name
        val avatar = info.avatar
        val title = conversation.conversationTitle
        val icon = conversation.conversationIcon
        if(username != title || avatar != icon){
            UserModel.getInstance().queryUser(info.userId, object: QueryUserListener{
                override fun done(user: User?, e: BmobException?) {
                    if(e == null){
                        val name = user!!.username
                        val avatarIcon = user.icon
                        conversation.conversationIcon = avatarIcon
                        conversation.conversationTitle = name
                        info.name = name
                        info.avatar = avatarIcon
                        BmobIM.getInstance().updateUserInfo(info)
                        if(!msg.isTransient){
                            BmobIM.getInstance().updateConversation(conversation)
                        }
                    }
                    baseListener.done(null)
                }
            })
        } else {
            baseListener.done(null)
        }
    }

    override fun queryUser(objectId: String, queryUserListener: QueryUserListener) {
        val query = BmobQuery<User>()
        query.addWhereEqualTo("objectId", objectId)
        query.findObjects(object: FindListener<User>(){
            override fun done(p0: MutableList<User>?, p1: BmobException?) {
                if(p1 == null){
                    if(p0 != null && p0.size > 0){
                        queryUserListener.done(p0[0], null)
                    } else {
                        queryUserListener.done(null, BmobException(CANNOT_FOUND_USER))
                    }
                } else {
                    queryUserListener.done(null, p1)
                }
            }
        })
    }
}