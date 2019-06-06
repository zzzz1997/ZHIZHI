package com.amazing.zhizhi.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.amazing.zhizhi.R
import com.amazing.zhizhi.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 聊天界面ViewHolder
 *
 * @author zzzz
 */
class TalkViewHolder(itemView: View) : BaseViewHolder(itemView){
    val userLayout = itemView.findViewById<LinearLayout>(R.id.talk_recycle_item_user_layout)!!
    val userIcon = itemView.findViewById<RoundedImageView>(R.id.talk_recycle_item_user_icon)!!
    val userContent = itemView.findViewById<TextView>(R.id.talk_recycle_item_user_content)!!
    val meLayout = itemView.findViewById<LinearLayout>(R.id.talk_recycle_item_me_layout)!!
    val meIcon = itemView.findViewById<RoundedImageView>(R.id.talk_recycle_item_me_icon)!!
    val meContent = itemView.findViewById<TextView>(R.id.talk_recycle_item_me_content)!!
}