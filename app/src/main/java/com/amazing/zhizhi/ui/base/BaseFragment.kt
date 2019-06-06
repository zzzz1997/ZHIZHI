package com.amazing.zhizhi.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobUser
import com.amazing.zhizhi.entity.User

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 基础fragment
 *
 * @author zzzz
 */
abstract class BaseFragment : Fragment() {

    private var v: View? = null

    //用户对象
    var user: User? = null

    private var isInit = false
    private var isLoad = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(setContentView(), container, false)
        user = BmobUser.getCurrentUser(User::class.java)
        initView()
        isInit = true
        toLoad()
        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        toLoad()
    }

    /**
     * 载入界面
     */
    private fun toLoad(){
        if(isInit){
            if(userVisibleHint){
                loadView()
                isLoad = true
            } else {
                if(isLoad){
                    stopLoad()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isInit = false
        isLoad = false
    }

    /**
     * 获取控件对象
     *
     * @param id 控件的id
     * @return 控件对象
     */
    fun findViewById(id: Int) : View{
        return v!!.findViewById(id)
    }

    /**
     * 设置界面资源
     * @return 界面资源
     */
    abstract fun setContentView() : Int

    /**
     * 初始化界面
     */
    abstract fun initView()

    /**
     * 载入界面
     */
    abstract fun loadView()

    /**
     * 结束载入
     */
    abstract fun stopLoad()

    /**
     * 刷新界面
     */
    abstract fun refresh()
}