package com.amazing.zhizhi.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.bean.BmobIMMessage
import cn.bmob.newim.bean.BmobIMTextMessage
import cn.bmob.newim.bean.BmobIMUserInfo
import cn.bmob.newim.core.BmobIMClient
import cn.bmob.newim.event.MessageEvent
import cn.bmob.newim.listener.MessageSendListener
import cn.bmob.v3.exception.BmobException
import com.amazing.zhizhi.R
import com.amazing.zhizhi.adapter.MessageAdapter
import com.amazing.zhizhi.adapter.listener.OnMessageClickListener
import com.amazing.zhizhi.entity.Notification
import com.amazing.zhizhi.ui.TalkActivity
import com.amazing.zhizhi.ui.base.BaseFragment
import com.amazing.zhizhi.utils.MyCode
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout
import es.dmoral.toasty.Toasty
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import q.rorbin.badgeview.QBadgeView

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 消息Fragment
 *
 * @author zzzz
 */
class MessageFragment : BaseFragment(), OnRefreshListener {

    // 获取DisplayMetrics
    private val dm = DisplayMetrics()

    // 聊天数据
    private var lists: List<BmobIMConversation>? = null
    // 消息适配器
    private lateinit var adapter: MessageAdapter

    // RecyclerView头布局
    private lateinit var header: View

    // 声明控件
    private lateinit var swipe: SwipeToLoadLayout
    private lateinit var recycler: RecyclerView
    private lateinit var system: RelativeLayout
    private lateinit var badge: View

    override fun setContentView(): Int {
        return R.layout.fragment_message
    }

    @SuppressLint("InflateParams")
    override fun initView() {
        // 注册EventBus
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        // 获取视图
        header = LayoutInflater.from(context).inflate(R.layout.message_recycler_header, null, false)
        header.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // 获取控件对象
        swipe = findViewById(R.id.fragment_message_swipe) as SwipeToLoadLayout
        recycler = findViewById(R.id.swipe_target) as RecyclerView
        system = header.findViewById(R.id.system_notification_layout) as RelativeLayout
        badge = header.findViewById(R.id.system_notification_badge)

        // 获取Metrics
        activity!!.windowManager.defaultDisplay.getMetrics(dm)

        // 设置下拉刷新事件
        swipe.setOnRefreshListener(this)

        // 设置系统消息点击事件
        system.setOnClickListener {
            Toasty.info(context!!, "系统消息").show()
            // debug代码
            val conversation = BmobIMConversation()
            conversation.conversationId = "OvE5FFFN"
            conversation.conversationTitle = "吱吱"
            conversation.conversationIcon = getString(R.string.default_icon)
            val message = BmobIMTextMessage()
            message.content = "测试"
            val conversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(),
                    BmobIM.getInstance().startPrivateConversation(BmobIMUserInfo(conversation.conversationId, conversation.conversationTitle, conversation.conversationIcon), null))
            conversationManager.sendMessage(message, object: MessageSendListener() {
                override fun done(p0: BmobIMMessage?, p1: BmobException?) {
                    if (p1 == null) {
                        Toasty.success(context!!, "发送成功").show()
                    } else {
                        Toasty.error(context!!, p1.message!!).show()
                    }
                }
            })
        }

        QBadgeView(context).bindTarget(badge).badgeNumber = 1

        refresh()
    }

    override fun loadView() {

    }

    override fun stopLoad() {

    }

    override fun refresh() {
        if(user != null) {
            lists = BmobIM.getInstance().loadAllConversation()
            if(lists != null) {
                adapter = MessageAdapter(context!!, lists as MutableList<BmobIMConversation>, dm.widthPixels)
                adapter.addHeader(header)
                adapter.setOnBaseClickListener(object : OnMessageClickListener {
                    override fun onClicked(position: Int) {
                        val intent = Intent(context!!, TalkActivity::class.java)
                        intent.putExtra("conversation", lists!![position])
                        startActivityForResult(intent, MyCode.REQUEST_TALK)
                        activity!!.overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left)
                    }

                    override fun onLongClicked(position: Int) {
                        Toasty.info(context!!, "长按").show()
                    }

                    override fun onTopClicked(position: Int) {
                        Toasty.info(context!!, "置顶").show()
                    }

                    override fun onDeleteClicked(position: Int) {
                        Toasty.info(context!!, "删除").show()
                    }
                })
                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(context)
            }
        }
        swipe.isRefreshing = false
    }

    override fun onRefresh() {
        refresh()
    }

    /**
     * 系统通知消息接收器
     *
     * @param notification 消息对象
     */
    @Subscribe
    fun onEvent(notification: Notification){
        QBadgeView(context).bindTarget(badge).badgeNumber = 1
    }

    /**
     * 聊天消息接收器
     *
     * @param event 消息对象
     */
    @Subscribe
    fun onEvent(event: MessageEvent){
        refresh()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MyCode.REQUEST_TALK && resultCode == AppCompatActivity.RESULT_OK) {
            refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 取消EventBus
        EventBus.getDefault().unregister(this)
    }
}