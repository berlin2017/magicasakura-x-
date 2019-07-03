package cn.lonsun.magicasakura.theme

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * @Copyright © 2005-2019  安徽龙讯信息科技有限公司
 * @ClassName: ThemeHelper
 * @Description: ${DESCRIPTION}
 * @Author: berlin
 * @Date: 2019/2/20 0020 15:48
 */
class LSThemeHelper {

    companion object {

        val themeFileName = "theme_colors.json"

        private val CURRENT_THEME = "theme_current"

        fun getSharePreference(context: Context): SharedPreferences {
            return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE)
        }

        fun setTheme(context: Context, themeName: String) {
            getSharePreference(context).edit()
                .putString(CURRENT_THEME, themeName)
                .apply()
        }

        fun getThemeName(context: Context): String {
            return getSharePreference(context)
                .getString(CURRENT_THEME, "")
        }

        fun isDefaultTheme(context: Context): Boolean {
            return getThemeName(context) == ""
        }

        fun restoreTheme(context: Context) {
            setTheme(context, "")
        }

        fun isExitTheme(context: Context, name: String): Boolean {
            val result = readAssets(
                context,
                themeFileName
            )
            val type = object : TypeToken<List<ThemeItem>>() {}.type
            val list = Gson().fromJson<List<ThemeItem>>(result, type)
            if (list != null && !list.isEmpty()) {
                for (item in list) {
                    if (item.name == name) {
                        return true
                    }
                }
            }

            val downloadPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            val file = File(downloadPath, themeFileName)
            if (file.exists()) {
                val local_result = file.readText()
                val local_list = Gson().fromJson<List<ThemeItem>>(local_result, type)
                if (local_list != null && local_list.isNotEmpty()) {
                    for (item in local_list) {
                        if (item.name == name) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun getTheme(p0: Context): ThemeItem? {
            val result = readAssets(
                p0,
                themeFileName
            )
            val list = parseGson(p0, result)
                ?: return getThemeByFile(p0)
            return list
        }

        fun parseGson(p0: Context, result: String): ThemeItem? {
            val type = object : TypeToken<List<ThemeItem>>() {}.type
            try {
                val list = Gson().fromJson<List<ThemeItem>>(result, type)
                if (list.isNotEmpty()) {
                    for (item in list) {
                        if (item.name == getThemeName(p0)) {
                            return item
                        }
                    }
                }
            } catch (e: Exception) {
                return null
            }

            return null
        }

        fun getThemeByFile(context: Context): ThemeItem? {
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
    }
}