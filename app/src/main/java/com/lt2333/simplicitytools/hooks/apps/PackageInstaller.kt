package com.lt2333.simplicitytools.hooks.apps

import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.getObjectField
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.setBooleanField
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PackageInstaller : AppRegister() {
    // 仅适配 4.3.9-202208016
    override val packageName: String = "com.miui.packageinstaller"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("pkg_installer_count_checking") {
            findMethod("com.miui.packageInstaller.model.RiskControlRules") {
                name == "getCurrentLevel"
            }.hookBefore { param ->
                XposedBridge.log("Hooked getCurrentLevel, param result = ${param.result}")
                param.result = 0
            }
        }

        hasEnable("pkg_installer_disable_ad") {
            findMethod("m2.a") {
                name == "m" && parameterCount == 0
            }.hookBefore {
                it.result = false
            }
            findMethod("m2.a") {
                name == "n" && parameterCount == 0
            }.hookBefore {
                it.result = false
            }
            findMethod("m2.a") {
                name == "o" && parameterCount == 0
            }.hookBefore {
                it.result = false
            }
        }

        hasEnable("pkg_installer_disable_risk") {
            findMethod("com.android.packageinstaller.compat.MiuiSettingsCompat") {
                name == "isInstallRiskEnabled"
            }.hookBefore {
                it.result = false
            }
            findMethod("com.android.packageinstaller.compat.MiuiSettingsCompat") {
                name == "isPersonalizedAdEnabled"
            }.hookBefore {
                it.result = false
            }
        }

        hasEnable("pkg_installer_disable_safemode_tip") {
            findMethod("com.miui.packageInstaller.ui.listcomponets.c0") {
                name =="a"
            }.hookAfter {
                it.thisObject.setBooleanField("l", false)
            }
        }
    }
}