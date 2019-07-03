/*
 * *
 *  * @Copyright © 2005-2019 安徽龙讯信息科技有限公司
 *  * @FileName: BaseActivity.kt
 *  * @Description: ${DESCRIPTION}
 *  * @Author: berlin
 *  * @Date: 19-5-24 上午8:46
 *
 */

package cn.lonsun.themesample

import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import cn.lonsun.magicasakura.utils.ThemeUtils
import cn.lonsun.themesample.R


/**
 * @Copyright © 2005-2019  安徽龙讯信息科技有限公司
 * @ClassName: BaseActivity
 * @Description: 键盘隐藏和loadingDialog的显示或隐藏等
 * @Author: berlin
 * @Date: 2019/1/24 0024 11:47
 */


abstract class BaseActivity : AppCompatActivity() {

    /**
     * 状态栏颜色
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
            if (Build.VERSION.SDK_INT >= 21) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = ThemeUtils.getColorById(this, R.color.theme_color_primary_dark)
                val description = ActivityManager.TaskDescription(
                        null, null,
                        ThemeUtils.getThemeAttrColor(this, android.R.attr.colorPrimary)
                )
                setTaskDescription(description)
            }
    }

}