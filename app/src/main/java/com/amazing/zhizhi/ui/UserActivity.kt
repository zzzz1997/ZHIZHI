package com.amazing.zhizhi.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import com.amazing.zhizhi.R
import com.amazing.zhizhi.entity.User
import com.amazing.zhizhi.utils.MyCode
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_user.*
import java.io.File
import java.io.FileOutputStream

/**
 * Project ZhiZhi
 * Date 2018/4/4
 *
 * 用户信息界面
 *
 * @author zzzz
 */
class UserActivity : AppCompatActivity() {

    // 默认裁剪图片缓存地址
    private val UPLOAD_CACHE_PYTH = Uri.parse("file:///" + Environment.getExternalStorageDirectory().absolutePath + "/head_portrait.png")

    // 用户对象
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        initView()
    }

    /**
     * 初始化操作
     */
    private fun initView() {
        // 获取当前用户
        user = BmobUser.getCurrentUser(User::class.java)

        // 设置icon
        val icon = if(user.icon == getString(R.string.default_icon)){
            File(cacheDir.absolutePath + "/bmob/default.png")
        } else {
            File(cacheDir.absolutePath + "/bmob/${user.username}.png")
        }
        if (icon.exists()){
            activity_user_icon.setImageURI(Uri.fromFile(icon))
        } else {
            Toasty.error(this@UserActivity, getString(R.string.load_icon_failed)).show()
        }

        // 设置基础信息
        activity_user_name.text = user.username
        activity_user_age.text = user.age.toString().plus("岁")
        activity_user_phone.text = user.mobilePhoneNumber
        activity_user_email.text = user.email
        activity_user_address.text = user.address
        activity_user_create.text = user.createdAt

        activity_user_toolbar.setNavigationOnClickListener {
            finish()
        }

        // 请更改图片并更新服务器存储
        activity_user_icon_layout.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, MyCode.REQUEST_ICON)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == MyCode.REQUEST_ICON){
            if(data != null){
                val intent = Intent("com.android.camera.action.CROP")
                intent.setDataAndType(data.data, "image/*")
                intent.putExtra("crop", "true")
                intent.putExtra("aspectX", 1)
                intent.putExtra("aspectY", 1)
                intent.putExtra("outputX", 300)
                intent.putExtra("outputY", 300)
                intent.putExtra("return-data", false)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, UPLOAD_CACHE_PYTH)
                intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
                intent.putExtra("noFaceDetection", true)
                startActivityForResult(intent, MyCode.REQUEST_UPLOAD)
            }
        } else if(requestCode == MyCode.REQUEST_UPLOAD) {
            val image = File(UPLOAD_CACHE_PYTH.path)
            if (image.exists()){
                val file = BmobFile(image)
                file.uploadblock(object: UploadFileListener(){
                    override fun done(p0: BmobException?) {
                        if (p0 == null){
                            user.icon = file.fileUrl
                            user.update(object: UpdateListener(){
                                override fun done(p0: BmobException?) {
                                    if(p0 == null){
                                        val bitmap = BitmapFactory.decodeFile(UPLOAD_CACHE_PYTH.path)
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(cacheDir.absolutePath + "/bmob/${user.username}.png"))
                                        activity_user_icon.setImageURI(Uri.parse(cacheDir.absolutePath + "/bmob/${user.username}.png"))
                                        setResult(AppCompatActivity.RESULT_OK)
                                    } else {
                                        Toasty.error(this@UserActivity, p0.message!!).show()
                                    }
                                }
                            })
                        } else {
                            Toasty.error(this@UserActivity, p0.message!!).show()
                        }
                    }
                })
            }
        }
    }
}