package com.amazing.zhizhi.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMConversation
import com.amazing.zhizhi.R
import com.bumptech.glide.Glide
import com.amazing.zhizhi.adapter.base.BaseAdapter
import com.amazing.zhizhi.adapter.base.BaseViewHolder
import com.amazing.zhizhi.adapter.listener.OnMessageClickListener
import com.amazing.zhizhi.utils.MyCode
import com.makeramen.roundedimageview.RoundedImageView
import q.rorbin.badgeview.QBadgeView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Project ZhiZhi
 * Date 2018/4/6
 *
 * 消息列表适配器
 *
 * @author zzzz
 */
class MessageAdapter(private val context: Context, private val conversations: MutableList<BmobIMConversation>, private val screenWidth: Int)
    : BaseAdapter<BmobIMConversation, MessageAdapter.MessageViewHolder, OnMessageClickListener>(conversations),
        View.OnClickListener, View.OnLongClickListener {

    // 适配布局大小
    private lateinit var layoutParams: ViewGroup.LayoutParams
    // 时间显示格式
    @SuppressLint("SimpleDateFormat")
    private val  simpleDateFormat = SimpleDateFormat(context.getString(R.string.date_format))

    // 点击监听器
    private lateinit var onMessageClickListener: OnMessageClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(header != null && viewType == MyCode.RECYCLER_TYPE_HEADER){
            MessageViewHolder(header!!)
        } else {
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_recycle_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        if(getItemViewType(position) == MyCode.RECYCLER_TYPE_HEADER) {
            return
        }

        val pos = getDataPosition(holder)
        val conversation = conversations[pos]

        layoutParams = holder.mIn!!.layoutParams
        layoutParams.width = screenWidth

        Glide.with(context)
                .load(conversation.conversationIcon)
                .into(holder.icon!!)
        holder.name!!.text = conversation.conversationTitle
        holder.time!!.text = simpleDateFormat.format(Date(conversation.updateTime))
        holder.content!!.text = conversation.messages[0].content
        QBadgeView(context).bindTarget(holder.badge)
                .badgeNumber = BmobIM.getInstance().getUnReadCount(conversation.conversationId).toInt()

        holder.itemView.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_UP -> {
                    val scrollX = holder.scrollView!!.scrollX
                    val width = holder.out!!.layoutParams.width
                    if(scrollX < width / 2){
                        holder.scrollView!!.smoothScrollTo(0, 0)
                    } else {
                        holder.scrollView!!.smoothScrollTo(width, 0)
                    }
                    true
                }
                else -> false
            }
        }

        if(holder.scrollView!!.scrollX != 0){
            holder.scrollView!!.smoothScrollTo(0, 0)
        }

        holder.mIn!!.tag = pos
        holder.top!!.tag = pos
        holder.del!!.tag = pos
        holder.mIn!!.setOnClickListener(this)
        holder.mIn!!.setOnLongClickListener(this)
        holder.top!!.setOnClickListener(this)
        holder.del!!.setOnClickListener(this)
    }

    override fun setOnBaseClickListener(onBaseClickListener: OnMessageClickListener) {
        this.onMessageClickListener = onBaseClickListener
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.message_recycle_item_in -> {
                val horizontalScrollView = p0.parent.parent as HorizontalScrollView
                if(horizontalScrollView.scrollX != 0){
                    horizontalScrollView.smoothScrollTo(0, 0)
                } else {
                    onMessageClickListener.onClicked(p0.tag as Int)
                }
            }
            R.id.message_recycle_item_top -> onMessageClickListener.onTopClicked(p0.tag as Int)
            R.id.message_recycle_item_delete -> onMessageClickListener.onDeleteClicked(p0.tag as Int)
            else -> return
        }
    }

    override fun onLongClick(p0: View?): Boolean {
        val horizontalScrollView = p0!!.parent.parent as HorizontalScrollView
        return if(horizontalScrollView.scrollX != 0){
            horizontalScrollView.smoothScrollTo(0, 0)
            false
        } else {
            onMessageClickListener.onLongClicked(p0.tag as Int)
            true
        }
    }

    inner class MessageViewHolder(itemView: View) : BaseViewHolder(itemView) {
        // 控件对象
        var scrollView: HorizontalScrollView? = null
        var mIn: LinearLayout? = null
        var icon: RoundedImageView? = null
        var name: TextView? = null
        var time: TextView? = null
        var content: TextView? = null
        var badge: View? = null
        var out: LinearLayout? = null
        var top: TextView? = null
        var del: TextView? = null

        init {
            if(itemView != header) {
                scrollView = itemView.findViewById(R.id.message_recycle_item_scroll_view)!!
                mIn = itemView.findViewById(R.id.message_recycle_item_in)!!
                icon = itemView.findViewById(R.id.message_recycle_item_icon)!!
                name = itemView.findViewById(R.id.message_recycle_item_name)!!
                time = itemView.findViewById(R.id.message_recycle_item_time)!!
                content = itemView.findViewById(R.id.message_recycle_item_content)!!
                badge = itemView.findViewById(R.id.message_recycle_item_badge)!!
                out = itemView.findViewById(R.id.message_recycle_item_out)!!
                top = itemView.findViewById(R.id.message_recycle_item_top)!!
                del = itemView.findViewById(R.id.message_recycle_item_delete)!!
            }
        }

    }
}