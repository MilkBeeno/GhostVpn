package com.simple.ghostvpn.util.ktx

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.immersiveStatusBar(dark: Boolean = true) {
    setStatusBarDark(dark)
    setStatusBarVisible(true)
    setStatusBarColor(Color.TRANSPARENT)
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun Activity.setStatusBarVisible(isVisible: Boolean) {
    WindowCompat.setDecorFitsSystemWindows(window, isVisible)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        if (isVisible) {
            controller.show(WindowInsetsCompat.Type.statusBars())
        } else {
            controller.hide(WindowInsetsCompat.Type.statusBars())
        }
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

fun Activity.setStatusBarDark(dark: Boolean = true) {
    WindowInsetsControllerCompat(window, window.decorView)
        .isAppearanceLightStatusBars = dark
}


fun View.statusBarPadding() {
    try {
        if (context !is Activity) return
        val activity = context as Activity
        layoutParams = when (parent) {
            is FrameLayout -> {
                val params = layoutParams as FrameLayout.LayoutParams
                params.topMargin += activity.obtainStatusBarHeight()
                params
            }
            is LinearLayout -> {
                val params = layoutParams as LinearLayout.LayoutParams
                params.topMargin += activity.obtainStatusBarHeight()
                params
            }
            is LinearLayoutCompat -> {
                val params = layoutParams as LinearLayoutCompat.LayoutParams
                params.topMargin += activity.obtainStatusBarHeight()
                params
            }
            is ConstraintLayout -> {
                val params = layoutParams as ConstraintLayout.LayoutParams
                params.topMargin += activity.obtainStatusBarHeight()
                params
            }
            else -> layoutParams
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.navigationBarPadding() {
    try {
        if (context !is Activity) return
        val activity = context as Activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) setParams(activity) else {
            val differ = activity.obtainScreenRealHeight() - activity.obtainScreenHeight()
            val offSize = activity.obtainNavigationBarHeight() + activity.obtainStatusBarHeight()
            if (differ == offSize) setParams(activity)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun View.setParams(activity: Activity) {
    layoutParams = when (parent) {
        is FrameLayout -> {
            val params = layoutParams as FrameLayout.LayoutParams
            params.bottomMargin += activity.obtainNavigationBarHeight()
            params
        }
        is LinearLayout -> {
            val params = layoutParams as LinearLayout.LayoutParams
            params.bottomMargin += activity.obtainNavigationBarHeight()
            params
        }
        is LinearLayoutCompat -> {
            val params = layoutParams as LinearLayoutCompat.LayoutParams
            params.bottomMargin += activity.obtainNavigationBarHeight()
            params
        }
        is ConstraintLayout -> {
            val params = layoutParams as ConstraintLayout.LayoutParams
            params.bottomMargin += activity.obtainNavigationBarHeight()
            params
        }
        else -> layoutParams
    }
}
