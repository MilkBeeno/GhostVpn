package com.simple.ghostvpn.util.ktx

import android.content.Context
import android.os.Looper
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private var lastTime: Long = 0
private const val minInterval = 2000L

fun Context.showToast(message: String) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastTime > minInterval) {
        runMainThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            lastTime = currentTime
        }
    }
}

private fun runMainThread(request: () -> Unit) {
    if (Looper.getMainLooper() == Looper.getMainLooper())
        request()
    else
        MainScope().launch(Dispatchers.Main) {
            request()
        }
}
