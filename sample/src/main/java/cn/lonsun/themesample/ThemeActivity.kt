/*
 * *
 *  * @Copyright © 2005-2019 安徽龙讯信息科技有限公司
 *  * @FileName: ThemeActivity.kt
 *  * @Description: ${DESCRIPTION}
 *  * @Author: lpp
 *  * @Date: 19-3-15 上午10:53
 *
 */

package cn.lonsun.themesample

import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.lonsun.magicasakura.theme.LSThemeHelper
import cn.lonsun.magicasakura.theme.ThemeItem
import cn.lonsun.magicasakura.utils.ThemeUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_theme_layout.*
import java.io.File

/**
 * @Copyright © 2005-2019  安徽龙讯信息科技有限公司
 * @ClassName: ThemeActivity
 * @Description: ${DESCRIPTION}
 * @Author: berlin
 * @Date: 2019/2/20 0020 10:32
 */
class ThemeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_theme_layout)
        initViews()
    }

    private fun initViews() {

        day.setOnClickListener {
            LSThemeHelper.setTheme(this, "red")
            refreshTheme()
        }

        green.setOnClickListener {
            LSThemeHelper.setTheme(this, "green")
            refreshTheme()
        }

        night.setOnClickListener {
            if (!LSThemeHelper.isExitTheme(this, "black")) {
                Toast.makeText(this@ThemeActivity, "主题没有下载", Toast.LENGTH_SHORT).show()
                val dialog = AlertDialog.Builder(this).setTitle("提示").setMessage("是否下载该主题？")
                    .setPositiveButton("下载") { dialog, _ ->
                        dialog.dismiss()
                        downloadTheme("black")
                        LSThemeHelper.setTheme(this, "black")
                        refreshTheme()
                    }.setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }.create()
                dialog.show()
            } else {
                LSThemeHelper.setTheme(this, "black")
                refreshTheme()
            }
        }
    }


    private fun downloadTheme(name: String) {
        val list = ArrayList<ThemeItem>()
        val black = ThemeItem(name, "#212121", "#3B3B3B", "#99212121")
        list.add(black)
        val downloadPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
        val file = File(downloadPath, LSThemeHelper.themeFileName)
        if (file.exists()) {
            val result = file.readText()
            val type = object : TypeToken<ArrayList<ThemeItem>>() {}.type
            val data = Gson().fromJson<ArrayList<ThemeItem>>(result, type)
            if (data.size > 0) {
                list.addAll(data)
            }
        }
        file.writeText(Gson().toJson(list))
    }


    /**
     *
     */
    private var refreshable = object : ThemeUtils.ExtraRefreshable {
        override fun refreshSpecificView(view: View?) {

        }

        override fun refreshGlobal(activity: Activity?) {
            if (Build.VERSION.SDK_INT >= 21) {
                val context = this@ThemeActivity
                val taskDescription = ActivityManager.TaskDescription(
                    null, null,
                    ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary)
                )
                setTaskDescription(taskDescription)
                window.statusBarColor = ThemeUtils.getColorById(context, R.color.theme_color_primary_dark)
            }
        }
    }


    fun refreshTheme() {
        ThemeUtils.refreshUI(this, refreshable)
    }
}