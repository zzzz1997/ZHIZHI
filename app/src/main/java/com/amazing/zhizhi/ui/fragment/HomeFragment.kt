package com.amazing.zhizhi.ui.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.bingoogolapple.bgabanner.BGABanner
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.Banner
import com.amazing.zhizhi.entity.Recommend
import com.amazing.zhizhi.entity.Router
import com.amazing.zhizhi.ui.SearchActivity
import com.amazing.zhizhi.ui.base.BaseFragment
import com.amazing.zhizhi.utils.ActivityUtil
import com.amazing.zhizhi.utils.MyCode
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.citywheel.CityConfig
import com.lljjcoder.style.citypickerview.CityPickerView
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import es.dmoral.toasty.Toasty
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 主页Fragment
 *
 * @author zzzz
 */
class HomeFragment : BaseFragment(), OnRefreshListener, OnLoadMoreListener {

    // 图片的圆角
    private val RADIUS = 30
    // 默认省份
    private val DEFAULT_PROVINCE = "云南省"
    // 默认城市
    private val DEFAULT_CITY = "昆明市"

    // 声明城市选择器
    private val picker = CityPickerView()

    private lateinit var swipe: SwipeToLoadLayout

    // 声明控件
    private lateinit var cityPicker: TextView
    private lateinit var searchBar: EditText
    private lateinit var scan: ImageView
    private lateinit var banner: BGABanner

    // 懒加载路由布局
    private val routerLayout: LinearLayout by lazy {
        findViewById(R.id.fragment_home_router_layout) as LinearLayout
    }

    // 懒加载推荐布局
    private val recommendLayout: LinearLayout by lazy {
        findViewById(R.id.fragment_home_recommended_layout) as LinearLayout
    }

    override fun setContentView(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        // 获取控件对象
        cityPicker = findViewById(R.id.fragment_home_city_picker) as TextView
        searchBar = findViewById(R.id.fragment_home_search) as EditText
        scan = findViewById(R.id.fragment_home_scan) as ImageView
        swipe = findViewById(R.id.fragment_home_swipe) as SwipeToLoadLayout
        banner = findViewById(R.id.fragment_home_banner) as BGABanner

        // 城市选择器点击按钮
        cityPicker.setOnClickListener {
            // 设置默认属性
            val cityConfig = CityConfig.Builder()
                    .confirTextColor("03A9F4")
                    .setCityWheelType(CityConfig.WheelType.PRO_CITY)
                    .provinceCyclic(false)
                    .province(DEFAULT_PROVINCE)
                    .city(DEFAULT_CITY)
                    .setShowGAT(true)
                    .build()
            picker.setConfig(cityConfig)
            picker.setOnCityItemClickListener(object: OnCityItemClickListener(){
                override fun onSelected(province: ProvinceBean?, city: CityBean?, district: DistrictBean?) {
                    var string = ""

                    if(province != null) {
                        string += province.name
                    }

                    if(city != null) {
                        string += city.name
                    }

                    if(district != null) {
                        string += district.name
                    }

                    Toasty.success(context!!, string).show()
                }

                override fun onCancel() {
                    Toasty.info(context!!, "已取消").show()
                }
            })
            picker.showCityPicker()
        }

        // 搜索框聚焦
        searchBar.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                startActivity(Intent(activity!!, SearchActivity::class.java))
                searchBar.clearFocus()
            }
        }

        // 扫描按钮点击事件
        scan.setOnClickListener {
            // 请求权限
            AndPermission.with(context!!)
                    .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                    .onGranted {
                        // 请求权限成功，扫描
                        startActivityForResult(Intent(activity, CaptureActivity::class.java), MyCode.REQUEST_SCAN)
                    }
                    .onDenied {
                        // 请求失败
                        val packageURI = Uri.parse("package:" + activity!!.packageName)
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                        Toasty.warning(context!!, getString(R.string.cant_scan_without_permission)).show()
                    }
                    .start()
        }

        // 设置下拉刷新监听
        swipe.setOnRefreshListener(this)

        // 轮播图资源适配
        banner.setAdapter { _, itemView, model, _ ->
            Glide.with(context!!)
                    .load((model as Banner).img)
                    .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(RADIUS, 0)))
                    .into(itemView as ImageView)
        }
        // 轮播图点击事件
        banner.setDelegate{ _, _, model, _ ->
            ActivityUtil.startActivityByUri(context!!, (model as Banner).uri)
        }

        refresh()
        picker.init(context!!)
    }

    override fun loadView() {

    }

    override fun stopLoad() {

    }

    override fun refresh() {
        // 加载轮播数据
        val bannerQuery = BmobQuery<Banner>()
        bannerQuery.findObjects(object: FindListener<Banner>(){
            override fun done(p0: MutableList<Banner>?, p1: BmobException?) {
                if(p1 == null) {
                    if(p0 != null) {
                        banner.setData(p0, MutableList(p0.size, { _ -> "" }))
                    }
                } else {
                    Toasty.error(context!!, p1.message!!).show()
                }
            }
        })
        // 加载路由数据
        val routerQuery = BmobQuery<Router>()
        routerQuery.findObjects(object: FindListener<Router>() {
            override fun done(p0: MutableList<Router>?, p1: BmobException?) {
                if(p1 == null) {
                    if (p0 != null) {
                        fillRouter(p0)
                    }
                } else {
                    Toasty.error(context!!, p1.message!!).show()
                }
            }
        })
        // 加载推荐数据
        val recommendedQuery = BmobQuery<Recommend>()
        recommendedQuery.findObjects(object: FindListener<Recommend>() {
            override fun done(p0: MutableList<Recommend>?, p1: BmobException?) {
                if(p1 == null) {
                    if (p0 != null) {
                        fillRecommend(p0)
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

    }

    /**
     * 填充路由数据
     */
    private fun fillRouter(routers: MutableList<Router>) {
        routerLayout.removeAllViews()
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        // 初始化控件
        val textViews = MutableList(routers.size, {
            TextView(context!!)
        })
        for (i in 0 until routers.size) {
            Glide.with(this)
                    .load(routers[i].img)
                    .into(object: SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            Toasty.info(context!!, "get")
                            resource.setBounds(0, 0,
                                    resources.getDimensionPixelSize(R.dimen.icon_size), resources.getDimensionPixelSize(R.dimen.icon_size))
                            textViews[i].setCompoundDrawables(null, resource, null, null)
                            textViews[i].compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.text_drawable_padding)
                        }
                    })
            textViews[i].text = routers[i].title
            textViews[i].setOnClickListener {
                ActivityUtil.startActivityByUri(context!!, routers[i].uri)
            }
            textViews[i].layoutParams = layoutParams
            textViews[i].gravity = Gravity.CENTER
            textViews[i].setPadding(resources.getDimensionPixelSize(R.dimen.text_drawable_padding), resources.getDimensionPixelSize(R.dimen.text_drawable_padding),
                    resources.getDimensionPixelSize(R.dimen.text_drawable_padding), resources.getDimensionPixelSize(R.dimen.text_drawable_padding))
        }

        // 四个控件一行
        val rows = Math.ceil(routers.size.toDouble() / 4).toInt()
        val linearLayouts = MutableList(rows, {
            LinearLayout(context!!)
        })
        for (i in 0 until rows) {
            linearLayouts[i].addView(textViews[4 * i])
            linearLayouts[i].addView(textViews[4 * i + 1])
            linearLayouts[i].addView(textViews[4 * i + 2])
            linearLayouts[i].addView(textViews[4 * i + 3])

            routerLayout.addView(linearLayouts[i])
        }
    }

    /**
     * 填充推荐数据
     */
    private fun fillRecommend(recommends: MutableList<Recommend>) {
        recommendLayout.removeAllViews()
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        val textViews = MutableList(recommends.size, {
            TextView(context!!)
        })

        for (i in 0 until recommends.size) {
            Glide.with(this)
                    .load(recommends[i].img)
                    .into(object: SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            Toasty.info(context!!, "get")
                            resource.setBounds(0, 0,
                                    resources.getDimensionPixelSize(R.dimen.recommend_icon_width), resources.getDimensionPixelSize(R.dimen.recommend_icon_height))
                            textViews[i].setCompoundDrawables(null, resource, null, null)
                            textViews[i].compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.text_drawable_padding)
                        }
                    })
            textViews[i].text = recommends[i].title
            textViews[i].setOnClickListener {
                ActivityUtil.startActivityByUri(context!!, recommends[i].uri)
            }
            textViews[i].layoutParams = layoutParams
            textViews[i].gravity = Gravity.CENTER
            textViews[i].setPadding(resources.getDimensionPixelSize(R.dimen.text_drawable_padding), resources.getDimensionPixelSize(R.dimen.text_drawable_padding),
                    resources.getDimensionPixelSize(R.dimen.text_drawable_padding), resources.getDimensionPixelSize(R.dimen.text_drawable_padding))

            recommendLayout.addView(textViews[i])
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MyCode.REQUEST_SCAN && resultCode == AppCompatActivity.RESULT_OK) {
            if(data != null) {
                Toasty.success(context!!, data.getStringExtra(Constant.CODED_CONTENT)).show()
            }
        }
    }
}