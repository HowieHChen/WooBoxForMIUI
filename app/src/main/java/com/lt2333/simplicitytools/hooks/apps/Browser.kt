package com.lt2333.simplicitytools.hooks.apps

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Browser : AppRegister()  {
    override val packageName: String = "com.android.browser"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("browser_debug_mode") {
            try {
                findMethod("com.android.browser.j1") {
                    name == "getDebugMode"
                }.hookBefore {
                    it.result = true
                }
            }
            catch (tout: Throwable) {
                Log.i("Hook browser failed!")
            }

        }
    }
}