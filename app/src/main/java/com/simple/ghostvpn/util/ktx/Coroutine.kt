package com.simple.ghostvpn.util.ktx

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

fun ioScope(action: suspend CoroutineScope.() -> Unit): Job {
    return MainScope().launch(Dispatchers.IO) {
        action.invoke(this)
    }
}

fun mainScope(action: suspend CoroutineScope.() -> Unit): Job {
    return MainScope().launch(Dispatchers.Main) {
        action.invoke(this)
    }
}

fun defaultScope(action: suspend CoroutineScope.() -> Unit): Job {
    return MainScope().launch(Dispatchers.Default) {
        action.invoke(this)
    }
}

suspend fun withIo(action: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
        action.invoke()
    }
}

suspend fun withMain(action: suspend () -> Unit) {
    withContext(Dispatchers.Main) {
        action.invoke()
    }
}

fun FragmentActivity.launch(action: suspend () -> Unit) {
    lifecycleScope.launch { action() }
}