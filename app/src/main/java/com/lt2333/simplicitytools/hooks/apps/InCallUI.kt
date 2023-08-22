package com.lt2333.simplicitytools.hooks.apps

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.paramCount
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object InCallUI : AppRegister()   {
    override val packageName: String = "com.android.incallui"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("incallui_hide_crbt") {
            findMethod("com.android.incallui.Call") {
                name == "setPlayingVideoCrbt" && paramCount == 2
            }.hookBefore {
                it.result = null
            }
        }
    }
}