package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.content.res.ColorStateList
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.callMethod
import com.lt2333.simplicitytools.utils.getObjectField
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object OptimizeMusicColorForT : HookRegister() {

    private var mediaViewHolder: Any? = null

    override fun init() {
        hasEnable("optimize_music_color") {
            findMethod("com.android.systemui.statusbar.notification.mediacontrol.MiuiMediaControlPanel") {
                name == "setForegroundColors"
            }.hookBefore {
                it.result = 0
            }

            findMethod("com.android.systemui.media.ColorSchemeTransition\$accentPrimary\$2") {
                name == "invoke"
            }.hookBefore {
                mediaViewHolder = (it.thisObject.getObjectField("this$0"))?.getObjectField("mediaViewHolder")
                val action0 = mediaViewHolder?.callMethod("getAction0") as ImageView
                val action1 = mediaViewHolder?.callMethod("getAction1") as ImageView
                val action2 = mediaViewHolder?.callMethod("getAction2") as ImageView
                val action3 = mediaViewHolder?.callMethod("getAction3") as ImageView
                val action4 = mediaViewHolder?.callMethod("getAction4") as ImageView
                val colorSchemeTransition: Int = it.args[0] as Int
                action0.backgroundTintList = ColorStateList.valueOf(colorSchemeTransition)
                action1.backgroundTintList = ColorStateList.valueOf(colorSchemeTransition)
                action2.backgroundTintList = ColorStateList.valueOf(colorSchemeTransition)
                action3.backgroundTintList = ColorStateList.valueOf(colorSchemeTransition)
                action4.backgroundTintList = ColorStateList.valueOf(colorSchemeTransition)
            }

            findMethod("com.android.systemui.media.ColorSchemeTransition\$colorSeamless\$1") {
                name == "invoke"
            }.hookAfter {
                mediaViewHolder = (it.thisObject.getObjectField("this$0"))?.getObjectField("mediaViewHolder")
            }

            findMethod("com.android.systemui.media.ColorSchemeTransition\$colorSeamless\$2") {
                name == "invoke"
            }.hookAfter {
                val seamlessIcon = mediaViewHolder?.callMethod("getSeamlessIcon") as ImageView
                val obj = it.args[0] as Int
                seamlessIcon.imageTintList = ColorStateList.valueOf(obj)
            }

            findMethod("com.android.systemui.media.ColorSchemeTransition") {
                name == "updateColorScheme"
            }.hookBefore {
                try {
                    mediaViewHolder = (it.thisObject.getObjectField("this$0"))?.getObjectField("mediaViewHolder")
                    val colorSchemeTransition: Int = it.args[0] as Int
                    val objs = arrayOf(
                        it.thisObject.getObjectField("surfaceColor"),
                        it.thisObject.getObjectField("colorSeamless"),
                        it.thisObject.getObjectField("accentPrimary"),
                        it.thisObject.getObjectField("accentSecondary"),
                        it.thisObject.getObjectField("textPrimary"),
                        it.thisObject.getObjectField("textPrimaryInverse"),
                        it.thisObject.getObjectField("textSecondary"),
                        it.thisObject.getObjectField("textTertiary"),
                    )
                    for (obj in objs) {
                        obj?.callMethod("updateColorScheme", colorSchemeTransition)
                    }
                }
                catch (tout: Throwable) {
                    Log.i("pre updateColorScheme failed!")
                }
            }
        }
    }
}