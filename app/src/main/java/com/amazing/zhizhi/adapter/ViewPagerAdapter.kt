package com.amazing.zhizhi.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.amazing.zhizhi.ui.base.BaseFragment
import com.amazing.zhizhi.ui.fragment.*

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * viewpager适配器
 *
 * @author zzzz
 */
class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    // fragment列表
    private val fragments = ArrayList<BaseFragment>()

    init {
        // 初始化添加列表
        fragments.clear()
        fragments.add(HomeFragment())
        fragments.add(FindFragment())
        fragments.add(NullFragment())
        fragments.add(MessageFragment())
        fragments.add(MeFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}