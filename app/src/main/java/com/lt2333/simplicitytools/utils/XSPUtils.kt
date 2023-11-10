package com.lt2333.simplicitytools.utils

import com.lt2333.simplicitytools.BuildConfig
import de.robv.android.xposed.XSharedPreferences

object XSPUtils {
    private var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getBoolean(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getInt(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getFloat(key, defValue)
    }

    fun getString(key: String, defValue: String): String? {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getString(key, defValue)
    }

    fun getStringSet(key: String, defValue: MutableSet<String>): MutableSet<String> {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getStringSet(key, defValue) ?: defValue
    }
}

inline fun hasEnable(
    key: String,
    default: Boolean = false,
    noinline extraCondition: (() -> Boolean)? = null,
    crossinline block: () -> Unit
) {
    val conditionResult = if (extraCondition != null) extraCondition() else true
    if (XSPUtils.getBoolean(key, default) && conditionResult) {
        block()
    }
}
