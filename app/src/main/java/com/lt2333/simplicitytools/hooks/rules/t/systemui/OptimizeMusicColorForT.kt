package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.app.WallpaperColors
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.callMethod
import com.lt2333.simplicitytools.utils.getObjectField
import com.lt2333.simplicitytools.utils.getObjectFieldOrNull
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.setObjectField
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object OptimizeMusicColorForT : HookRegister() {

    override fun init() {
        hasEnable("optimize_music_color", extraCondition = {
            XSPUtils.getBoolean("optimize_music_notification", false)
        }) {
            findMethod("com.android.systemui.statusbar.notification.mediacontrol.MiuiMediaControlPanel") {
                name == "setForegroundColors"
            }.hookBefore {
                val result = it.args[0]
                val mediaViewHolder = it.thisObject.getObjectField("mMediaViewHolder")
                if (result == null || mediaViewHolder == null)
                    it.result = 0
                try {
                    val context: Context = it.thisObject.getObjectField("mContext") as Context
                    val isNight = (context.resources.configuration.uiMode and 48) == 32
                    val bitmap = result.getObjectField("bitmap") as Bitmap

                    val key = result.getObjectFieldOrNull("key")
                    if (key != null) {
                        it.thisObject.setObjectField("mCurrentKey", key as String)
                    }

                    val wallpaperColors = WallpaperColors.fromBitmap(bitmap)
                    val style = loadClass("com.android.systemui.monet.Style").enumConstants[6]
                    val colorScheme = findConstructor("com.android.systemui.monet.ColorScheme") {
                        paramCount == 3 && parameterTypes[0] == WallpaperColors::class.java
                    }.newInstance(wallpaperColors, true, style)
                    val accentList = (colorScheme.callMethod("getAccent1") as? List<Integer>)
                    val primaryColor = accentList?.get(2)?.toInt()
                    val seamlessColor =
                        if (isNight) accentList?.get(2)?.toInt()
                        else accentList?.get(3)?.toInt()
                    if (primaryColor != null && seamlessColor != null) {
                        val value: ColorStateList = ColorStateList.valueOf(primaryColor)
                        val valueAlpha1: ColorStateList = value.withAlpha(192)
                        val valueAlpha2: ColorStateList = valueAlpha1.withAlpha(128)
                        (mediaViewHolder?.callMethod("getTitleText") as TextView).setTextColor(primaryColor)
                        (mediaViewHolder.callMethod("getAppName") as TextView).setTextColor(primaryColor)
                        (mediaViewHolder.callMethod("getArtistText") as TextView).setTextColor(primaryColor)
                        (mediaViewHolder.callMethod("getElapsedTimeView") as TextView).setTextColor(primaryColor)
                        (mediaViewHolder.callMethod("getTotalTimeView") as TextView).setTextColor(primaryColor)
                        (mediaViewHolder.callMethod("getAction0") as ImageView).imageTintList = value
                        (mediaViewHolder.callMethod("getAction1") as ImageView).imageTintList = value
                        (mediaViewHolder.callMethod("getAction2") as ImageView).imageTintList = value
                        (mediaViewHolder.callMethod("getAction3") as ImageView).imageTintList = value
                        (mediaViewHolder.callMethod("getAction4") as ImageView).imageTintList = value
                        (mediaViewHolder.callMethod("getSeekBar") as SeekBar).thumbTintList = value
                        (mediaViewHolder.callMethod("getSeekBar") as SeekBar).progressTintList = valueAlpha1
                        (mediaViewHolder.callMethod("getSeekBar") as SeekBar).progressBackgroundTintList = valueAlpha2
                        (mediaViewHolder.callMethod("getSeamlessIcon") as ImageView).imageTintList = ColorStateList.valueOf(seamlessColor)
                    }
                }
                catch (tout: Throwable) {
                    XposedBridge.log("Hook setForegroundColors failed!\n${tout.toString()}")
                }

                it.result = 0
            }
        }
    }
}