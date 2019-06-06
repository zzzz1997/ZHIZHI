package com.amazing.zhizhi.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.bean.BmobIMMessage
import cn.bmob.newim.bean.BmobIMTextMessage
import cn.bmob.newim.bean.BmobIMUserInfo
import cn.bmob.newim.core.BmobIMClient
import cn.bmob.newim.event.MessageEvent
import cn.bmob.newim.listener.MessageSendListener
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.amazing.zhizhi.R
import com.amazing.zhizhi.adapter.TalkAdapter
import com.amazing.zhizhi.adapter.listener.OnTalkClickListener
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.listener.OnSoftKeyBoardChangeListener
import com.amazing.zhizhi.utils.SoftKeyBoardUtil
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_talk.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 聊天界面
 *
 * @author zzzz
 */
class TalkActivity : AppCompatActivity() {

    // 连接对象
    private lateinit var conversation: BmobIMConversation
    // 用户对象
    private lateinit var user: User
    // 连天适配器
    private lateinit var adapter: TalkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)

        initView()
    }

    /**
     * 初始化界面
     */
    private fun initView(){
        // 注册EventBus
        EventBus.getDefault().register(this)

        // 获取聊天连接
        conversation = intent.getSerializableExtra("conversation") as BmobIMConversation

        // 获取当前用户
        user = BmobUser.getCurrentUser(User::class.java)

        activity_talk_toolbar.setNavigationOnClickListener {
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        activity_talk_name.text = conversation.conversationTitle

        //发送消息
        activity_talk_send.setOnClickListener {
            val content = activity_talk_edit.text.toString()
            if(!content.isEmpty()) {
                val message = BmobIMTextMessage()
                message.content = content
                val map = HashMap<String, Any>(1)
                map["level"] = 1

                message.setExtraMap(map)
                val conversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(),
                        BmobIM.getInstance().startPrivateConversation(BmobIMUserInfo(conversation.conversationId, conversation.conversationTitle,conversation.conversationIcon), null))
                conversationManager.sendMessage(message, object: MessageSendListener() {
                    override fun done(p0: BmobIMMessage?, p1: BmobException?) {
                        if (p1 == null) {
                            addNewMessage(message)
                            activity_talk_edit.text = null
                            Toasty.success(this@TalkActivity, "发送成功").show()
                        } else {
                            Toasty.error(this@TalkActivity, p1.message!!).show()
                        }
                    }
                })
            }
        }

        // 添加键盘监听器
        SoftKeyBoardUtil(this).setOnSoftKeyBoardChangeListener(object: OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                activity_talk_recycler.scrollToPosition(adapter.itemCount - 1)
            }

            override fun keyBoardHide(height: Int) {

            }
        })

        initRecycler()
    }

    /**
     * EventBus的接收器
     *
     * @param event 接收到的消息事件
     */
    @Subscribe
    fun onEvent(event: MessageEvent){
        addNewMessage(event.message)
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecycler() {
        val messages = conversation.messages
        messages.reverse()
        adapter = TalkAdapter(this, messages, user, conversation.conversationIcon)
        adapter.setOnBaseClickListener(object: OnTalkClickListener {
            override fun onUserIconClicked() {
                val query = BmobQuery<User>()
                query.addWhereEqualTo("objectId", conversation.conversationId)
                query.findObjects(object: FindListener<User>(){
                    override fun done(p0: MutableList<User>?, p1: BmobException?) {
                        if(p1 == null){
                            val intent = Intent(this@TalkActivity, DetailsActivity::class.java)
                            intent.putExtra("user", p0!![0])
                            intent.putExtra("me", user)
                            startActivity(intent)
                        } else {
                            Toasty.error(this@TalkActivity, p1.message!!).show()
                        }
                    }
                })
            }

            override fun onMeIconClicked() {
                val intent = Intent(this@TalkActivity, DetailsActivity::class.java)
                intent.putExtra("user", user)
                intent.putExtra("me", user)
                startActivity(intent)
            }

            override fun onLongClicked(view: View, position: Int) {
                Toasty.info(this@TalkActivity, messages[position].content).show()
            }
        })
        activity_talk_recycler.adapter = adapter
        activity_talk_recycler.layoutManager = LinearLayoutManager(this)
        activity_talk_recycler.scrollToPosition(adapter.itemCount - 1)
    }

    /**
     * 添加新的消息到列表
     *
     * @param message 消息
     */
    private fun addNewMessage(message: BmobIMMessage){
        adapter.insert(message, adapter.itemCount)
        activity_talk_recycler.scrollToPosition(adapter.itemCount - 1)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(AppCompatActivity.RESULT_OK)
            finish()
            overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right)
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 注销EventBus
        EventBus.getDefault().unregister(this)
    }
}