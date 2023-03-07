package com.simple.ghostvpn.util.ktx

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.isVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}