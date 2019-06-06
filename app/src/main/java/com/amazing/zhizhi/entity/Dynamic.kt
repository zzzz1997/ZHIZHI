package com.amazing.zhizhi.entity

import cn.bmob.v3.BmobObject

/**
 * Project ZhiZhi
 * Date 2018/4/9
 *
 * 动态实体类
 *
 * @author zzzz
 */
class Dynamic : BmobObject() {

    // 动态类型
    var dynamicType = 20

    // 动态作者
    var author: User? = null

    // 动态所属公司
    var company: Company? = null

    // 动态标题
    var title: String? = null

    // 动态内容
    var content: String = ""

    // 动态主图片
    var image: String? = null

    // 动态视频链接
    var video: String? = null

    // 动态视频微缩图
    var videoImg: String? = null
}