package com.amazing.zhizhi.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.ViewGroup
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMUserInfo
import cn.bmob.newim.core.ConnectionStatus
import cn.bmob.newim.event.MessageEvent
import cn.bmob.newim.listener.ConnectListener
import cn.bmob.newim.listener.ConnectStatusChangeListener
import cn.bmob.push.BmobPush
import cn.bmob.v3.*
import cn.bmob.v3.exception.BmobException
import com.amazing.zhizhi.R
import com.amazing.zhizhi.adapter.ViewPagerAdapter
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.utils.MyCode
import com.amazing.zhizhi.utils.blur.AnimUtil
import com.amazing.zhizhi.utils.blur.BitmapUtil
import com.amazing.zhizhi.utils.blur.ScreenCaptureUtil
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户主界面
 *
 * @author zzzz
 */
class MainActivity : AppCompatActivity() {

    // 应用的Bmob key
    private val APPKEY = "0609b5cda2401bf3d1c4bae43b834950"
    // 高斯模糊指数
    private val BLUR_LEVEL = 25f
    // 默认背景颜色
    private val DEFAULT_COLOR = 0x77000000
    // 动画时长
    private val DURATION = 250L

    // 记录点击返回的时间
    private var first = 0L

    companion object {
        // DisplayMetrics
        val dm = DisplayMetrics()
    }

    // viewpager适配器
    private lateinit var adapter: ViewPagerAdapter
    // 导航栏适配器
    private lateinit var navigationAdapter: AHBottomNavigationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        // 注册EventBus
        EventBus.getDefault().register(this)
        // 初始化Bmob
        Bmob.initialize(this, APPKEY)

        // 初始化BmobPush
        BmobInstallationManager.getInstance().initialize(object: InstallationListener<BmobInstallation>(){
            override fun done(p0: BmobInstallation?, p1: BmobException?) {
                if(p1 == null){

                } else {
                    Toasty.error(this@MainActivity, p1.message!!).show()
                }
            }
        })
        // 开始BmobPush服务
        BmobPush.startWork(this)

        windowManager.defaultDisplay.getMetrics(dm)

        // 初始化导航栏适配器
        navigationAdapter = AHBottomNavigationAdapter(this, R.menu.main_tab)
        // 关联导航栏
        navigationAdapter.setupWithBottomNavigation(activity_main_navigation)

        activity_main_navigation.isTranslucentNavigationEnabled = true
        activity_main_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        activity_main_navigation.isBehaviorTranslationEnabled = false
        activity_main_navigation.inactiveColor = ContextCompat.getColor(this, R.color.colorText)
        activity_main_navigation.accentColor = ContextCompat.getColor(this, R.color.colorPrimary)
        activity_main_navigation.setOnTabSelectedListener(AHBottomNavigation.OnTabSelectedListener { position, _ ->
            // 如果是中间大图标，忽略操作
            if(position == 2){
                return@OnTabSelectedListener false
            }

            activity_main_view_pager.currentItem = position
            true
        })

        // 设置ViewPager属性
        activity_main_view_pager.offscreenPageLimit = 6
        activity_main_view_pager.layoutParams = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                dm.heightPixels - ScreenCaptureUtil.getStatusBarHeight(this) - resources.getDimension(com.aurelhubert.ahbottomnavigation.R.dimen.bottom_navigation_height).toInt())

        activity_main_add.setOnClickListener {
            val bitmap = ScreenCaptureUtil.getScreenshot(this)

            if(bitmap != null) {
                activity_main_blur.setImageBitmap(bitmap)
                BitmapUtil.blurImageView(this, activity_main_blur, BLUR_LEVEL, DEFAULT_COLOR)
            } else {
                activity_main_blur.setBackgroundColor(DEFAULT_COLOR)
            }
            AnimUtil.show(activity_main_cancel, activity_main_blur, DURATION)
        }

        activity_main_blur.setOnClickListener {
            AnimUtil.hide(activity_main_cancel, activity_main_blur, DURATION)
        }

        refresh()
    }

    /**
     * EventBus发放事件
     *
     * @param event 消息对象
     */
    @Subscribe
    fun onEvent(event: MessageEvent){
        refreshCount()
    }

    /**
     * 刷新未读消息数
     */
    private fun refreshCount() {
        activity_main_navigation.setNotification(BmobIM.getInstance().allUnReadCount.toString(), 3)
    }

    /**
     * 界面刷新
     */
    private fun refresh() {
        // 获取当前用户
        val user = BmobUser.getCurrentUser(User::class.java)
        if(user != null){
            // 连接BmobIM服务器
            BmobIM.connect(user.objectId, object: ConnectListener(){
                override fun done(p0: String?, p1: BmobException?) {
                    if(p1 == null){
                        BmobIM.getInstance().updateUserInfo(BmobIMUserInfo(user.objectId, user.username, user.icon))
                    } else {
                        Toasty.error(this@MainActivity, p1.message!!).show()
                    }
                }
            })
            // 设置BmobIM状态监听器
            BmobIM.getInstance().setOnConnectStatusChangeListener(object: ConnectStatusChangeListener(){
                override fun onChange(p0: ConnectionStatus?) {
                    when(p0){
                        ConnectionStatus.CONNECTED -> {
                            Toasty.info(this@MainActivity, p0.msg).show()
                        }
                        ConnectionStatus.CONNECTING -> {
                            Toasty.info(this@MainActivity, p0.msg).show()
                        }
                        ConnectionStatus.DISCONNECT -> {
                            Toasty.info(this@MainActivity, p0.msg).show()
                            // fragments[0]
                        }
                        else -> return
                    }
                }
            })
        }

        adapter = ViewPagerAdapter(supportFragmentManager)
        // 绑定viewpager适配器
        activity_main_view_pager.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val login = requestCode == MyCode.REQUEST_LOGIN && resultCode == RESULT_OK
        val icon = requestCode == MyCode.REQUEST_ICON && resultCode == RESULT_OK
        if(login || icon) {
            refresh()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val second = System.currentTimeMillis()
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(second - first < 2000){
                // 如果来两秒内连续点击，退出程序
                BmobIM.getInstance().disConnect()
                System.exit(0)
            } else {
                // 提示连续点击退出程序
                Toasty.warning(this, getString(R.string.once_more_for_exit)).show()
                first = System.currentTimeMillis()
            }
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放资源
        EventBus.getDefault().unregister(this)
        BmobIM.getInstance().clear()
        BmobIM.getInstance().disConnect()
    }
}
