package com.lt2333.simplicitytools.hooks.rules.all.browser

import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.DexKit
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.query.enums.StringMatchType

object DisableUpdateCheck : HookRegister() {
    private val miMarketUpdateClass by lazy {
        DexKit.dexKitBridge.findClass {
            matcher {
                addUsingString("MarketUpdateAgent", StringMatchType.Equals)
                addUsingString("packageName", StringMatchType.Equals)
            }
        }
    }
    private val miMarketDoInBackground by lazy {
        DexKit.dexKitBridge.findMethod {
            matcher {
                name = "doInBackground"
                returnType = "java.lang.Integer"
            }
            searchClasses = miMarketUpdateClass
        }.firstOrNull()
    }
    private val miMarketOnPostExecute by lazy {
        DexKit.dexKitBridge.findMethod {
            matcher {
                name = "onPostExecute"
            }
            searchClasses = miMarketUpdateClass
        }.firstOrNull()
    }

    override fun init() {
        hasEnable("browser_disable_update") {
            try {
                miMarketDoInBackground?.getMethodInstance(getDefaultClassLoader())?.hookBefore {
                    it.result = Integer.valueOf(1)
                }
                miMarketOnPostExecute?.getMethodInstance(getDefaultClassLoader())?.hookBefore {
                    it.result = null
                }
            }
            catch (tout: Throwable) {
                XposedBridge.log("Hook browser update failed!")
            }
        }
    }
}