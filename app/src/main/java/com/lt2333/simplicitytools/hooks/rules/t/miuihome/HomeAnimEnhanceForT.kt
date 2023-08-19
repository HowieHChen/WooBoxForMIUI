package com.lt2333.simplicitytools.hooks.rules.t.miuihome

import android.view.MotionEvent
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.paramCount
import com.lt2333.simplicitytools.utils.getObjectField
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.setObjectField
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedBridge

object HomeAnimEnhanceForT : HookRegister() {
    private var appToHomeAnim2Bak: Any? = null
    private var runnable = Runnable { }
    override fun init() {
        hasEnable("home_anim_enhance", false) {
            findMethod("com.miui.home.recents.NavStubView") {
                name == "onInputConsumerEvent"
            }.hookBefore {
                appToHomeAnim2Bak = it.thisObject.getObjectField("mAppToHomeAnim2")
                if (appToHomeAnim2Bak != null) {
                    it.thisObject.setObjectField("mAppToHomeAnim2", null)
                }
            }
            findMethod("com.miui.home.recents.NavStubView") {
                name == "onInputConsumerEvent"
            }.hookAfter {
                try {
                    var motionEvent = it.args[0] as MotionEvent
                    if (it.thisObject.getObjectField("mAppToHomeAnim2") != null || appToHomeAnim2Bak == null) {
                        return@hookAfter
                    }
                    else {
                        it.thisObject.setObjectField("mAppToHomeAnim2", appToHomeAnim2Bak)
                    }
                }
                catch (tout: Throwable) {
                    XposedBridge.log("Catch MotionEvent Failed!\n${tout.toString()}")
                }
            }

            findMethod("com.miui.home.launcher.ItemIcon") {
                name == "initPerformClickRunnable"
            }.hookBefore {
                it.result = runnable
            }
        }
    }
}