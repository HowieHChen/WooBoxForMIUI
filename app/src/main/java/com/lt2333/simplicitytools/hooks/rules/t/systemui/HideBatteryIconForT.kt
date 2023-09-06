package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.content.res.Resources
import android.content.res.XResources
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedBridge

object HideBatteryIconForT : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.views.MiuiBatteryMeterView") {
            name == "updateResources"
        }.hookAfter {
            //隐藏电池图标
            hasEnable("hide_battery_icon") {
                (it.thisObject.getObjectAs<ImageView>("mBatteryIconView")).visibility = View.GONE
                if (it.thisObject.getObject("mBatteryStyle") == 1) {
                    (it.thisObject.getObjectAs<FrameLayout>("mBatteryDigitalView")).visibility =
                        View.GONE
                }
            }
            //修改电池百分号大小
            hasEnable("change_battery_percentage_icon_size") {
                val batteryPercentView = it.thisObject.getObjectAs<TextView>("mBatteryPercentView")
                val batteryPercentMarkView = it.thisObject.getObjectAs<TextView>("mBatteryPercentMarkView")
                batteryPercentMarkView.layoutParams = batteryPercentView.layoutParams
                batteryPercentMarkView.typeface = batteryPercentView.typeface
                batteryPercentMarkView.setTextSize(0, batteryPercentView.textSize)
            }
            //隐藏电池内的百分比
            hasEnable("hide_battery_percentage_icon") {
                (it.thisObject.getObjectAs<TextView>("mBatteryPercentMarkView")).textSize = 0F
            }
        }

        findMethod("com.android.systemui.statusbar.views.MiuiBatteryMeterView") {
            name == "updateChargeAndText"
        }.hookAfter {
            //隐藏电池充电图标
            hasEnable("hide_battery_charging_icon") {
                (it.thisObject.getObjectAs<ImageView>("mBatteryChargingInView")).visibility =
                    View.GONE
                (it.thisObject.getObjectAs<ImageView>("mBatteryChargingView")).visibility =
                    View.GONE
            }
        }
    }

}