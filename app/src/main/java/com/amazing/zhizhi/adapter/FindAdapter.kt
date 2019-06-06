package com.amazing.zhizhi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.amazing.zhizhi.R
import com.amazing.zhizhi.adapter.base.BaseAdapter
import com.amazing.zhizhi.adapter.base.BaseViewHolder
import com.amazing.zhizhi.adapter.listener.OnFindClickListener
import com.amazing.zhizhi.entity.Dynamic
import com.amazing.zhizhi.utils.MyCode
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

/**
 * Project ZhiZhi
 * Date 2018/4/11
 *
 * 发现的适配器
 *
 * @author zzzz
 */
class FindAdapter(private val context: Context, private val dynamics: MutableList<Dynamic>)
    : BaseAdapter<Dynamic, FindAdapter.FindViewHolder, OnFindClickListener>(dynamics){

    // 点击监听器
    private lateinit var onFindClickListener: OnFindClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        return if(header != null && viewType == MyCode.RECYCLER_TYPE_HEADER){
            FindViewHolder(header!!)
        }else {
            FindViewHolder(LayoutInflater.from(context).inflate(R.layout.find_recycler_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        if(getItemViewType(position) == MyCode.RECYCLER_TYPE_HEADER) {
            return
        }

        val pos = getDataPosition(holder)

        val dynamic = dynamics[pos]
        when(dynamic.dynamicType){
            MyCode.DYNAMIC_TYPE_VIDEO -> {
                holder.sign!!.visibility = View.VISIBLE
                holder.video!!.visibility = View.VISIBLE
                holder.image!!.visibility = View.GONE

                Glide.with(context)
                        .load(dynamic.author!!.icon)
                        .into(holder.icon!!)
                holder.title!!.text = dynamic.title
                holder.name!!.text = dynamic.author!!.username
                holder.video!!.setUp(dynamic.video, JZVideoPlayer.SCREEN_WINDOW_LIST)
                Glide.with(context)
                        .load(dynamic.videoImg)
                        .into(holder.video!!.thumbImageView)
                holder.text!!.text = dynamic.content
                holder.info!!.text = "测试。。。"
            }
            MyCode.DYNAMIC_TYPE_POST -> {
                Glide.with(context)
                        .load(dynamic.author!!.icon)
                        .into(holder.icon!!)
                holder.title!!.text = dynamic.title
                holder.name!!.text = dynamic.author!!.username
                holder.text!!.text = dynamic.content
                Glide.with(context)
                        .load(dynamic.image)
                        .into(holder.image!!)
                holder.info!!.text = "测试。。。"
            }
            MyCode.DYNAMIC_TYPE_ADS -> {
                holder.icon!!.visibility = View.GONE
                holder.name!!.visibility = View.GONE
                holder.ads!!.visibility = View.VISIBLE

                holder.title!!.text = dynamic.title
                holder.text!!.text = dynamic.content
                Glide.with(context)
                        .load(dynamic.image)
                        .into(holder.image!!)
                holder.info!!.text = dynamic.company!!.name
            }
            else -> return
        }

        holder.view!!.tag = pos
        holder.menu!!.tag = pos
        holder.view!!.setOnClickListener(this)
        holder.menu!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.find_recycle_item_menu -> onFindClickListener.onMenuClicked(v.tag as Int)
            else -> onFindClickListener.onItemClicked(v.tag as Int)
        }
    }

    override fun setOnBaseClickListener(onBaseClickListener: OnFindClickListener) {
        this.onFindClickListener = onBaseClickListener
    }

    inner class FindViewHolder(itemView: View) : BaseViewHolder(itemView) {
        // 控件对象
        var view: LinearLayout? = null
        var icon: RoundedImageView? = null
        var title: TextView? = null
        var name: TextView? = null
        var sign: TextView? = null
        var menu: ImageView? = null
        var video: JZVideoPlayerStandard? = null
        var text: TextView? = null
        var image: ImageView? = null
        var ads: TextView? = null
        var info: TextView? = null

        init {
            if(itemView != header) {
                view = itemView.findViewById(R.id.find_recycle_item_view)!!
                icon = itemView.findViewById(R.id.find_recycle_item_icon)!!
                title = itemView.findViewById(R.id.find_recycle_item_title)!!
                name = itemView.findViewById(R.id.find_recycle_item_name)!!
                sign = itemView.findViewById(R.id.find_recycle_item_sign)!!
                menu = itemView.findViewById(R.id.find_recycle_item_menu)!!
                video = itemView.findViewById(R.id.find_recycle_item_video)!!
                text = itemView.findViewById(R.id.find_recycle_item_content_text)!!
                image = itemView.findViewById(R.id.find_recycle_item_content_image)!!
                ads = itemView.findViewById(R.id.find_recycle_item_ads)!!
                info = itemView.findViewById(R.id.find_recycle_item_info)!!
            }
        }
    }
}