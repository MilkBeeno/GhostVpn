package com.simple.ghostvpn.util.ktx

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

fun Context.color(@ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        resources.getColor(resId, theme)
    else
        resources.getColor(resId)
}

fun Context.string(@StringRes resId: Int): String {
    return resources.getString(resId)
}