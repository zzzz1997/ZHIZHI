package com.amazing.zhizhi.ui.fragment

import com.amazing.zhizhi.R
import com.amazing.zhizhi.ui.base.BaseFragment

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 空Fragment
 *
 * @author zzzz
 */
class NullFragment : BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_message
    }

    override fun initView() {}

    override fun loadView() {}

    override fun stopLoad() {}

    override fun refresh() {}
}