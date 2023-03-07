package com.simple.ghostvpn.util.ktx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.ioScope(action: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        action.invoke(this)
    }
}

fun ViewModel.mainScope(action: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(Dispatchers.Main) {
        action.invoke(this)
    }
}

fun ViewModel.defaultScope(action: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(Dispatchers.Default) {
        action.invoke(this)
    }
}