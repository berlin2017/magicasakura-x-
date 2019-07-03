package cn.lonsun.magicasakura.theme

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Environment
import cn.lonsun.magicasakura.utils.ThemeUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lonsun.magicasakura.R
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * @Copyright © 2005-2019  安徽龙讯信息科技有限公司
 * @ClassName: ThemeApplication
 * @Description: ${DESCRIPTION}
 * @Author: berlin
 * @Date: 2019/2/20 0020 15:44
 */
class ThemeApplication : Application(), ThemeUtils.switchColor {

    companion object {
        val themeFileName = "theme_colors.json"
    }

    override fun onCreate() {
        super.onCreate()
        ThemeUtils.mSwitchColor = this
    }

    override fun replaceColorById(p0: Context?, colorId: Int): Int {

        if (LSThemeHelper.isDefaultTheme(p0!!)) {
            return p0.resources.getColor(colorId)
        }
        val theme = getTheme(p0) ?: return p0.resources.getColor(colorId)
        return getThemeColorById(p0, colorId, theme)
    }

    override fun replaceColor(p0: Context?, originColor: Int): Int {
        if (LSThemeHelper.isDefaultTheme(p0!!)) {
            return originColor
        }
        val theme = getTheme(p0) ?: return originColor

        return getThemeColor(p0, originColor, theme)
    }

    fun getTheme(p0: Context): ThemeItem? {
        val result = readAssets(p0, themeFileName)
        val list = parseGson(p0, result) ?: return getThemeByFile(p0)
        return list
    }

    private fun parseGson(p0: Context, result: String): ThemeItem? {
        val type = object : TypeToken<List<ThemeItem>>() {}.type
        try {
            val list = Gson().fromJson<List<ThemeItem>>(result, type)
            if (list.isNotEmpty()) {
                for (item in list) {
                    if (item.name == LSThemeHelper.getTheme(p0)?.name) {
                        return item
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }

        return null
    }

    private fun getThemeByFile(context: Context): ThemeItem? {
        val downloadPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
        val file = File(downloadPath, themeFileName)
        if (file.exists()) {
            val result = file.readText()
            return parseGson(context, result)
        }

        return null
    }

    fun readAssets(context: Context, fileName: String): String {

        var result = ""
        try {
            val inputStreamReader = InputStreamReader(context.resources.assets.open(fileName))
            val bufferedReader = BufferedReader(inputStreamReader)
            var line: String? = null
            do {
                line = bufferedReader.readLine()
                if (line != null) {
                    result += line
                }
            } while (line != null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun getThemeColorById(p0: Context, colorId: Int, theme: ThemeItem): Int {
        when (colorId) {
            R.color.theme_color_primary -> return Color.parseColor(theme.color)
            R.color.theme_color_primary_dark -> return Color.parseColor(theme.dark)
            R.color.theme_color_primary_trans -> return Color.parseColor(theme.trans)
        }
        return p0.resources.getColor(colorId)
    }

    private fun getThemeColor(context: Context, color: Int, theme: ThemeItem): Int {
        when (color) {
            Color.parseColor("#1944a8") -> return Color.parseColor(theme.color)
            Color.parseColor("#1944a8") -> return Color.parseColor(theme.dark)
            Color.parseColor("#D81B60") -> return Color.parseColor(theme.trans)
        }
        return color
    }


}