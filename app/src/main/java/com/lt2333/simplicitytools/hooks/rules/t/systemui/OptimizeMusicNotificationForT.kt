package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.graphics.drawable.Icon;
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.callMethod
import com.lt2333.simplicitytools.utils.getObjectFieldAs
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedBridge

object OptimizeMusicNotificationForT : HookRegister() {

    private var lastArtwork:Icon? = null

    override fun init() {
        hasEnable("optimize_music_notification") {
            try {
                findMethod("com.android.systemui.media.MediaControlPanel") {
                    name == "bindArtworkAndColors"
                }.hookBefore {
                    val icon = it.args[0].callMethod("getArtwork") as Icon
                    if ((icon?.equals(lastArtwork))?:false) {
                        return@hookBefore
                    }
                    lastArtwork = icon
                    it.args[2] = true
                }
            }
            catch (tout: Throwable) {
                XposedBridge.log("Hook bindArtworkAndColors failed!\n${tout.toString()}")
            }

            findMethod("com.android.systemui.statusbar.notification.mediacontrol.MiuiMediaControlPanel") {
                name == "setInfoText"
            }.hookBefore {
                val mediaViewHolderAppName = it.args[1].callMethod("getAppName") as TextView
                val mediaDataApp = it.args[0].callMethod("getApp") as String
                mediaViewHolderAppName.text = mediaDataApp
                it.result = 0;
            }
        }
    }
}