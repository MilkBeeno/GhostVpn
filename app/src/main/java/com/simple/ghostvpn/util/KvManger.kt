package com.simple.ghostvpn.util

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV

object KvManger {
    private val kv by lazy { MMKV.defaultMMKV() }

    fun initialize(context: Context) {
        MMKV.initialize(context)
    }

    fun put(key: String, value: Any?) {
        when (value) {
            is Boolean -> kv.encode(key, value)
            is Int -> kv.encode(key, value)
            is Long -> kv.encode(key, value)
            is Float -> kv.encode(key, value)
            is Double -> kv.encode(key, value)
            is String -> kv.encode(key, value)
            is ByteArray -> kv.encode(key, value)
            is Parcelable -> kv.encode(key, value)
            is Set<*>? -> try {
                kv.encode(key, value as Set<String>)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            else -> return
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return kv.decodeBool(key, default)
    }

    fun getString(key: String, default: String = ""): String {
        return kv.decodeString(key, default) ?: default
    }

    fun getInt(key: String, default: Int = 0): Int {
        return kv.decodeInt(key, default)
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return kv.decodeFloat(key, default)
    }

    fun getDouble(key: String, default: Double = 0.0): Double {
        return kv.decodeDouble(key, default)
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return kv.decodeLong(key, default)
    }

    fun getByteArray(key: String, default: ByteArray = byteArrayOf()): ByteArray {
        return kv.decodeBytes(key, default) ?: default
    }
}