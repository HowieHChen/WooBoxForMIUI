package com.lt2333.simplicitytools.hooks.rules.t.mishare

import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.DexKit.dexKitBridge
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import org.luckypray.dexkit.query.enums.StringMatchType

object NoAutoTurnOff : HookRegister()  {
    private val nullMethod by lazy {
        dexKitBridge.findMethod {
            matcher {
                addUsingString("EnabledState", StringMatchType.Equals)
                addUsingString("mishare_enabled", StringMatchType.Equals)
            }
        }.map { it.getMethodInstance(this.getDefaultClassLoader()) }.toList()
    }

    private val toastMethod by lazy {
        dexKitBridge.findMethod {
            matcher {
                declaredClass {
                    addUsingString("null context", StringMatchType.Equals)
                    addUsingString("cta_agree", StringMatchType.Equals)
                }
                returnType = "boolean"
                paramTypes = listOf("android.content.Context", "java.lang.String")
                paramCount = 2
            }
        }.map { it.getMethodInstance(this.getDefaultClassLoader()) }.toList()
    }
    override fun init() {
        hasEnable("mishare_disable_auto_off"){
            nullMethod.hookBefore {
                it.result = null
            }
            toastMethod.hookBefore {
                if (it.args[1].equals("security_agree")) {
                    it.result = false
                }
            }
        }
    }
}