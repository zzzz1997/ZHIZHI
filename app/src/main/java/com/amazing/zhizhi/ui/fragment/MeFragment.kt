package com.amazing.zhizhi.ui.fragment

import android.content.Intent
import android.net.Uri
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.DownloadFileListener
import com.amazing.zhizhi.R
import com.amazing.zhizhi.ui.LoginActivity
import com.amazing.zhizhi.ui.UserActivity
import com.amazing.zhizhi.ui.base.BaseFragment
import com.amazing.zhizhi.utils.MyCode
import es.dmoral.toasty.Toasty
import java.io.File

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 主页Fragment
 *
 * @author zzzz
 */
class MeFragment : BaseFragment() {

    // 声明控件
    private lateinit var layout: CollapsingToolbarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var image: ImageView
    private lateinit var editInfo: TextView
    private lateinit var login: Button
    private lateinit var exit: Button

    override fun setContentView(): Int {
        return R.layout.fragment_me
    }

    override fun initView() {
        // 获取控件对象
        layout = findViewById(R.id.fragment_me_toolbar_layout) as CollapsingToolbarLayout
        toolbar = findViewById(R.id.fragment_me_toolbar) as Toolbar
        image = findViewById(R.id.fragment_me_image) as ImageView
        editInfo = findViewById(R.id.fragment_me_edit_info) as TextView
        login = findViewById(R.id.fragment_me_start_login) as Button
        exit = findViewById(R.id.fragment_me_exit_login) as Button

        // 点击事件
        image.setOnClickListener {
            activity!!.startActivityForResult(Intent(activity, UserActivity::class.java), MyCode.REQUEST_ICON)
        }

        editInfo.setOnClickListener {
            activity!!.startActivityForResult(Intent(activity, UserActivity::class.java), MyCode.REQUEST_ICON)
        }

        login.setOnClickListener {
            activity!!.startActivityForResult(Intent(activity, LoginActivity::class.java), MyCode.REQUEST_LOGIN)
        }

        exit.setOnClickListener {
            BmobUser.logOut()
            user = null
            refresh()
        }

        refresh()
    }

    override fun loadView() {

    }

    override fun stopLoad() {

    }

    override fun refresh() {
        if(user != null){
            // 用户信息不为空
            image.visibility = View.VISIBLE
            editInfo.visibility = View.VISIBLE
            login.visibility = View.GONE
            exit.visibility = View.VISIBLE

            layout.title = user!!.username

            val icon = if(user!!.icon == getString(R.string.default_icon)){
                File(activity!!.cacheDir.absolutePath + "/bmob/default.png")
            } else {
                File(activity!!.cacheDir.absolutePath + "/bmob/${user!!.username}.png")
            }
            if(!icon.exists()){
                BmobFile(icon.name, null, user!!.icon)
                        .download(icon, object: DownloadFileListener(){
                            override fun onProgress(p0: Int?, p1: Long) {}

                            override fun done(p0: String?, p1: BmobException?) {
                                if(p1 == null){
                                    image.setImageURI(Uri.fromFile(icon))
                                } else {
                                    Toasty.error(context!!, getString(R.string.load_icon_failed)).show()
                                }
                            }
                        })
            } else {
                image.setImageURI(Uri.fromFile(icon))
            }
        } else {
            layout.title = getString(R.string.me)
            image.visibility = View.GONE
            editInfo.visibility = View.GONE
            login.visibility = View.VISIBLE
            exit.visibility = View.GONE
        }
    }
}