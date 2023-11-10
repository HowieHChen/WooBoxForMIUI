package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.all.android.AllowUntrustedTouchesForAll
import com.lt2333.simplicitytools.hooks.rules.all.android.DeleteOnPostNotificationForAll
import com.lt2333.simplicitytools.hooks.rules.all.android.DisableFlagSecureForAll
import com.lt2333.simplicitytools.hooks.rules.all.corepatch.CorePatchMainHook
import com.lt2333.simplicitytools.hooks.rules.s.android.MaxWallpaperScaleForS
import com.lt2333.simplicitytools.hooks.rules.s.android.RemoveSmallWindowRestrictionsForS
import com.lt2333.simplicitytools.hooks.rules.s.android.SystemPropertiesHookForS
import com.lt2333.simplicitytools.hooks.rules.t.android.DisableFixedOrientation
import com.lt2333.simplicitytools.hooks.rules.t.android.MaxWallpaperScaleForT
import com.lt2333.simplicitytools.hooks.rules.t.android.SystemPropertiesHookForT
import com.lt2333.simplicitytools.hooks.rules.t.mishare.NoAutoTurnOff
import com.lt2333.simplicitytools.utils.DexKit
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MiShare: AppRegister()  {
    override val packageName: String = "com.miui.mishare.connectivity"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                DexKit.initDexKit(lpparam)
                autoInitHooks(
                    lpparam,
                    NoAutoTurnOff, // 禁用自动关闭
                )
                DexKit.closeDexKit()
            }
            /*
            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    DisableFlagSecureForAll, //允许截图
                    DeleteOnPostNotificationForAll, //上层显示
                    RemoveSmallWindowRestrictionsForS, //解除小窗限制
                    MaxWallpaperScaleForS, //壁纸缩放比例
                    SystemPropertiesHookForS, //SystemPropertiesHook
                    AllowUntrustedTouchesForAll, //允许不受信任的触摸
                )
            }

             */
        }
    }
}