package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.getObjectFieldAs
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object HideCarrierLabelForT : HookRegister()  {


    override fun init() {

        findMethod("com.android.systemui.statusbar.policy.MiuiCarrierTextController") {
            name == "setSubs" && parameterTypes[0] == MutableList::class.java
        }.hookAfter {
            val cardDisableList = it.thisObject.getObjectFieldAs<BooleanArray>("mCardDisable")
            val size = cardDisableList.size
            hasEnable("hide_carrier_label_one", extraCondition = { size >= 1 }) {
                cardDisableList[0] = true
            }
            hasEnable("hide_carrier_label_two", extraCondition = { size == 2 }) {
                cardDisableList[1] = true
            }
        }

    }

}