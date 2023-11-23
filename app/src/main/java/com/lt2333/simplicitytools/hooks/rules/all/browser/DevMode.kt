package com.lt2333.simplicitytools.hooks.rules.all.browser

import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.DexKit
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.query.enums.StringMatchType

object DevMode : HookRegister() {
    private val oldDebugMethod by lazy {
        DexKit.dexKitBridge.findMethod {
            matcher {
                name = "getDebugMode"
                returnType = "boolean"
                addUsingString("pref_key_debug_mode", StringMatchType.StartsWith)
            }
        }.firstOrNull()
    }
    override fun init() {
        hasEnable("browser_debug_mode") {
            try {
                oldDebugMethod?.getMethodInstance(getDefaultClassLoader())?.hookBefore {
                    it.result = true
                }
            }
            catch (tout: Throwable) {
                XposedBridge.log("Hook browser debug mode failed!")
            }
        }
    }
}