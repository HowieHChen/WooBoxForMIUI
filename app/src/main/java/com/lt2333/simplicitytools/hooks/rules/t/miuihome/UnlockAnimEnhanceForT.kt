package com.lt2333.simplicitytools.hooks.rules.t.miuihome

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.github.kyuubiran.ezxhelper.utils.paramCount
import com.lt2333.simplicitytools.utils.callMethod
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object UnlockAnimEnhanceForT : HookRegister() {
    override fun init() {
        hasEnable("unlock_anim_enhance", false) {
            findMethod("com.miui.home.launcher.compat.UserPresentAnimationCompatV12Phone") {
                name == "getSpringAnimator" && paramCount == 6
            }.hookBefore {
                it.args[4]  = 0.5f
                it.args[5]  = 0.5f
            }
        }
    }
}