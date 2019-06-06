package com.amazing.zhizhi.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kotlinx.android.synthetic.main.activity_details.*

/**
 * Project ZhiZhi
 * Date 2018/4/7
 *
 * 用户详情页面
 *
 * @author zzzz
 */
class DetailsActivity : AppCompatActivity() {

    private val BLUR_RADIUS = 100

    private lateinit var user: User
    private var me: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initView()
    }

    /**
     * 初始化界面
     */
    private fun initView(){
        user = intent.getSerializableExtra("user") as User
        if(intent.getSerializableExtra("me") != null) {
            me = intent.getSerializableExtra("me") as User
        }

        activity_details_toolbar_layout.title = user.username
        activity_details_toolbar.setNavigationOnClickListener {
            finish()
        }
        // 添加菜单
        // details_activity_toolbar.inflateMenu(R.menu.details_menu)

        Glide.with(this)
                .load(user.icon)
                .apply(bitmapTransform(MultiTransformation(BlurTransformation(BLUR_RADIUS),
                        CropTransformation(60, 20, CropTransformation.CropType.CENTER))))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(activity_details_blur_background)

        Glide.with(this)
                .load(user.icon)
                .into(activity_details_icon)

        activity_details_relation.text = "测试"
        activity_details_signature.text = user.signature

        activity_details_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light)
        activity_details_refresh.setOnRefreshListener {
            refresh()
        }

        activity_details_refresh.isRefreshing = true
        refresh()
    }

    /**
     * 刷新界面
     */
    private fun refresh() {

    }
}