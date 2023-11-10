package com.lt2333.simplicitytools.hooks.rules.t.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable

object DisableFixedOrientation : HookRegister() {
    override fun init() {
        val shouldDisableFixedOrientationList = XSPUtils.getStringSet("should_disable_fixed_orientation_list", mutableSetOf())

        hasEnable("disable_fixed_orientation") {
            findMethod("com.android.server.wm.MiuiFixedOrientationController") {
                name == "shouldDisableFixedOrientation"
            }.hookBefore {
                if (it.args[0] in shouldDisableFixedOrientationList) {
                    it.result = true
                }
            }
        }
    }
}