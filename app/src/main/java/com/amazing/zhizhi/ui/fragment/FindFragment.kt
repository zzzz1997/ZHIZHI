package com.amazing.zhizhi.ui.fragment

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.jzvd.JZVideoPlayer
import com.amazing.zhizhi.R
import com.amazing.zhizhi.adapter.FindAdapter
import com.amazing.zhizhi.adapter.listener.OnFindClickListener
import com.amazing.zhizhi.entity.Dynamic
import com.amazing.zhizhi.listener.TimerListener
import com.amazing.zhizhi.ui.base.BaseFragment
import com.amazing.zhizhi.utils.MyCountDownTimer
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout
import com.makeramen.roundedimageview.RoundedImageView
import es.dmoral.toasty.Toasty

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 发现Fragment
 *
 * @author zzzz
 */
class FindFragment : BaseFragment(), OnRefreshListener, OnLoadMoreListener {

    // 声明控件
    private lateinit var swipe: SwipeToLoadLayout
    private lateinit var recycler: RecyclerView
    private lateinit var header1: RoundedImageView

    // 发现适配器
    private var adapter: FindAdapter? = null

    // RecyclerView头视图
    private lateinit var header: View

    override fun setContentView(): Int {
        return R.layout.fragment_find
    }

    @SuppressLint("InflateParams")
    override fun initView() {
        // 获取视图
        header = LayoutInflater.from(context).inflate(R.layout.find_recycler_header, null, false)
        header.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // 获取控件对象
        swipe = findViewById(R.id.fragment_find_swipe) as SwipeToLoadLayout
        recycler = findViewById(R.id.swipe_target) as RecyclerView
        header1 = header.findViewById(R.id.find_recycle_header_1) as RoundedImageView

        // 设置刷新监听器
        swipe.setOnRefreshListener(this)
        swipe.setOnLoadMoreListener(this)

        // 设置RecyclerView焦点切换软件
        recycler.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewDetachedFromWindow(view: View) {
                JZVideoPlayer.onChildViewDetachedFromWindow(view)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                JZVideoPlayer.onChildViewAttachedToWindow(view, R.id.find_recycle_item_video)
            }
        })

        header1.setOnClickListener {
            Toasty.info(context!!, "header1").show()
        }

        refresh()
    }

    override fun loadView() {}

    override fun stopLoad() {}

    override fun refresh() {
        val query = BmobQuery<Dynamic>()
        query.include("author,company")
        query.order("-updatedAt")
        query.findObjects(object: FindListener<Dynamic>(){
            override fun done(p0: MutableList<Dynamic>?, p1: BmobException?) {
                if(p1 == null) {
                    if(p0 != null) {
                        adapter = FindAdapter(context!!, p0)
                        adapter!!.addHeader(header)
                        adapter!!.setOnBaseClickListener(object: OnFindClickListener{
                            override fun onItemClicked(position: Int) {
                                Toasty.info(context!!, "item: $position").show()
                            }

                            override fun onMenuClicked(position: Int) {
                                Toasty.info(context!!, "menu: $position").show()
                            }
                        })
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(context)
                    }
                } else {
                    Toasty.error(context!!, p1.message!!).show()
                }
            }
        })
        swipe.isRefreshing = false
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onLoadMore() {
        val timer = MyCountDownTimer(2000, object: TimerListener{
            override fun onFinish() {
                swipe.isLoadingMore = false
            }

            override fun onTick(time: String) {}
        })
        timer.start()
    }

    override fun onPause() {
        super.onPause()
        // 释放所有视频资源
        JZVideoPlayer.releaseAllVideos()
    }
}