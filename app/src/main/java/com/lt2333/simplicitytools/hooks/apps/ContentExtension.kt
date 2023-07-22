package com.lt2333.simplicitytools.hooks.apps

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.callMethod
import com.lt2333.simplicitytools.utils.getObjectField
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ContentExtension : AppRegister()  {

    override val packageName: String = "com.miui.contentextension"

    private val searchEngine = XSPUtils.getInt("taplus_search_engine", 0)
    private val searchEngineUrl = XSPUtils.getString("taplus_search_engine_url", "")
    private val searchUrlValues = arrayOf(
        "",
        "https://www.baidu.com/s?wd=%s",
        "https://www.sogou.com/web?query=%s",
        "https://www.bing.com/search?q=%s",
        "https://www.google.com/search?q=%s",
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        hasEnable("taplus_use_browser") {
            findMethod("com.miui.contentextension.utils.AppsUtils") {
                name == "openGlobalSearch"
            }.hookBefore {
                val context = it.args[0] as Context
                val queryString = it.args[1] as String
                // val sourcePkg = it.args[2] as String
                val searchUrl =
                    when (searchEngine) {
                        in 1..4 -> searchUrlValues[searchEngine]?.replaceFirst("%s",queryString)
                        5 -> searchEngineUrl?.replaceFirst("%s",queryString)
                        else -> ""
                    }
                val intent = Intent()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                if (searchEngine == 0 || searchUrl.isNullOrBlank()) {
                    intent.action = Intent.ACTION_WEB_SEARCH
                    intent.putExtra("query", queryString)
                }
                else {
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(searchUrl)
                }
                context.callMethod("startActivity", intent)
                it.result = 0
            }
        }

        hasEnable("taplus_landscape") {
            findMethod("com.miui.contentextension.services.TextContentExtensionService") {
                name == "isScreenPortrait"
            }.hookBefore {
                it.result = true
            }
        }

        hasEnable("taplus_hide_shopping") {
            findMethod("com.miui.contentextension.text.cardview.TaplusRecognitionExpandedImageCard") {
                name == "updateLayout"
            }.hookAfter {
                val shopping = it.thisObject.getObjectField("mShopping") as TextView
                shopping.visibility = View.GONE
                val scanQR = it.thisObject.getObjectField("mScanQR") as TextView
                val layoutParams = scanQR.layoutParams as LinearLayout.LayoutParams
                layoutParams.marginEnd = layoutParams.marginEnd * 2
                scanQR.layoutParams = layoutParams
            }
        }
    }
}