package com.simple.ghostvpn.util.ktx

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Insets
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets

/**
 *   获取屏幕的宽度
 * - [WindowInsetsCompat.Type.statusBars()]      状态栏
 * - [WindowInsetsCompat.Type.navigationBars()]  底部导航栏
 * - [WindowInsetsCompat.Type.captionBar()]      标题栏
 * - [WindowInsetsCompat.Type.systemBars()]      前三者全部
 */
fun Activity.obtainScreenWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val bounds: Rect = windowMetrics.bounds
        val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.systemBars()
        )
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            && resources.configuration.smallestScreenWidthDp < 600
        ) {
            // landscape and phone
            val navigationBarSize: Int = insets.right + insets.left
            bounds.width() - navigationBarSize
        } else {
            // portrait or tablet
            bounds.width()
        }
    } else {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        outMetrics.widthPixels
    }
}

/**
 *   获取屏幕的高度、不包括状态栏和导航栏高度
 * - [WindowInsetsCompat.Type.statusBars()]      状态栏
 * - [WindowInsetsCompat.Type.navigationBars()]  底部导航栏
 * - [WindowInsetsCompat.Type.captionBar()]      标题栏
 * - [WindowInsetsCompat.Type.systemBars()]      前三者全部
 */
fun Activity.obtainScreenHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val bounds: Rect = windowMetrics.bounds
        val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.systemBars()
        )
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            && resources.configuration.smallestScreenWidthDp < 600
        ) {
            // landscape and phone
            bounds.height()
        } else {
            // portrait or tablet
            val navigationBarSize: Int = insets.bottom
            val statusBarSize: Int = insets.top
            bounds.height() - navigationBarSize - statusBarSize
        }
    } else {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        outMetrics.heightPixels
    }
}

/**
 * 屏幕真实的高度、包括状态栏和导航栏
 */
fun Activity.obtainScreenRealHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val bounds: Rect = windowMetrics.bounds
        bounds.height()
    } else {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(outMetrics)
        outMetrics.heightPixels
    }
}

/**
 *   获取状态栏的高度
 * - [WindowInsetsCompat.Type.statusBars()]      状态栏
 */
fun Activity.obtainStatusBarHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())
        insets.top
    } else {
        val resourceId: Int = resources
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }
}

/**
 *   获取导航栏的高度
 * - [WindowInsetsCompat.Type.navigationBars()]  底部导航栏
 */
fun Activity.obtainNavigationBarHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())
        insets.bottom
    } else {
        val resourceId: Int = resources
            .getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }
}
