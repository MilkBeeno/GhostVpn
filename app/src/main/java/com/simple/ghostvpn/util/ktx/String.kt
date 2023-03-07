package com.simple.ghostvpn.util.ktx

fun String.replaceString(targetString: String, other: String = "XX"): String {
    if (!this.contains(other)) return this
    return try {
        val index = this.indexOf(other, ignoreCase = true)
        val startStr = this.substring(0, index)
        val endStr = this.substring(index + other.length, this.length)
        startStr.plus(targetString).plus(endStr)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}