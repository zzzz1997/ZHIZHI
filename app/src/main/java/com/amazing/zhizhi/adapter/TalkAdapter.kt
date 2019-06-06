package com.amazing.zhizhi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.newim.bean.BmobIMMessage
import com.amazing.zhizhi.R
import com.amazing.zhizhi.adapter.base.BaseAdapter
import com.amazing.zhizhi.adapter.listener.OnTalkClickListener
import com.amazing.zhizhi.adapter.viewholder.TalkViewHolder
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.utils.MyCode
import com.bumptech.glide.Glide

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 聊天界面的适配器
 *
 * @author zzzz
 */
class TalkAdapter(private val context: Context, private val messages: MutableList<BmobIMMessage>, private val user: User, private val icon: String)
    : BaseAdapter<BmobIMMessage, TalkViewHolder, OnTalkClickListener>(messages), View.OnClickListener, View.OnLongClickListener {

    // 声明监听器
    private lateinit var onTalkClickListener: OnTalkClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkViewHolder {
        return TalkViewHolder(LayoutInflater.from(context).inflate(R.layout.talk_recycle_item, parent, false))
    }

    override fun onBindViewHolder(holder: TalkViewHolder, position: Int) {
        if(getItemViewType(position) == MyCode.RECYCLER_TYPE_HEADER) {
            return
        }

        val pos = getDataPosition(holder)
        val message = messages[pos]
        if (message.fromId != user.objectId) {
            holder.meLayout.visibility = View.GONE
            Glide.with(context)
                    .load(icon)
                    .into(holder.userIcon)
            holder.userContent.text = message.content

            holder.userContent.tag = pos
            holder.userIcon.setOnClickListener(this)
            holder.userContent.setOnLongClickListener(this)
        } else {
            holder.userLayout.visibility = View.GONE
            Glide.with(context)
                    .load(user.icon)
                    .into(holder.meIcon)
            holder.meContent.text = message.content

            holder.meContent.tag = pos
            holder.meIcon.setOnClickListener(this)
            holder.meContent.setOnLongClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.talk_recycle_item_user_icon -> {
                onTalkClickListener.onUserIconClicked()
            }
            R.id.talk_recycle_item_me_icon -> {
                onTalkClickListener.onMeIconClicked()
            }
            else -> return
        }
    }

    override fun onLongClick(p0: View?): Boolean {
        onTalkClickListener.onLongClicked(p0!!, p0.tag as Int)
        return true
    }

    override fun setOnBaseClickListener(onBaseClickListener: OnTalkClickListener) {
        this.onTalkClickListener = onBaseClickListener
    }
}