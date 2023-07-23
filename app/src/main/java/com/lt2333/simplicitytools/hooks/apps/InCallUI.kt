package com.lt2333.simplicitytools.hooks.apps

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object InCallUI : AppRegister()   {
    override val packageName: String = "com.android.incallui"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("incallui_hide_crbt") {
            findMethod("com.android.incallui.Call") {
                name == "setPlayingVideoCrbt"
            }.hookBefore {
                it.args[0] = 0
                it.args[1] = false
            }
        }
    }
}