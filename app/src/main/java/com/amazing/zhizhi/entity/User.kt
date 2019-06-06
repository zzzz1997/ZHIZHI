package com.amazing.zhizhi.entity

import cn.bmob.v3.BmobUser

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户实体类
 *
 * @author zzzz
 */
class User : BmobUser() {

    // 用户头像地址
    var icon: String = ""

    // 用户地理位置
    var address: String = ""

    // 用户年龄
    var age: Int = 0

    // 用户个性签名
    var signature: String = ""
}