package com.lt2333.simplicitytools.hooks.apps

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.hooks.rules.all.browser.DevMode
import com.lt2333.simplicitytools.hooks.rules.all.browser.DisableUpdateCheck
import com.lt2333.simplicitytools.utils.DexKit
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.luckypray.dexkit.query.enums.StringMatchType

object Browser : AppRegister()  {
    override val packageName: String = "com.android.browser"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        DexKit.initDexKit(lpparam)
        autoInitHooks(
            lpparam,
            DevMode,    // 开发者模式
            DisableUpdateCheck      // 禁用更新检查
        )
        DexKit.closeDexKit()
    }
}