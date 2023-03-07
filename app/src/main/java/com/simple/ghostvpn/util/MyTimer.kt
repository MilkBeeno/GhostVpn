package com.simple.ghostvpn.util

import android.os.CountDownTimer
import com.simple.ghostvpn.util.ktx.mainScope

class MyTimer(private val builder: Builder) {

    private var milkCountDownTimer: MilkCountDownTimer? = null
    private var timeLeft: Long = 0

    fun run() {
        if (milkCountDownTimer == null) {
            milkCountDownTimer = MilkCountDownTimer()
        }
        milkCountDownTimer?.start()
    }

    fun stopped() {
        milkCountDownTimer?.cancel()
        milkCountDownTimer = null
    }

    fun stop() {
        mainScope { milkCountDownTimer?.onFinish() }
    }

    private inner class MilkCountDownTimer :
        CountDownTimer(builder.millisInFuture, builder.countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeft = millisUntilFinished
            builder.onTickListener?.invoke(this@MyTimer, millisUntilFinished)
        }

        override fun onFinish() {
            timeLeft = 0
            builder.onFinishedListener?.invoke()
            milkCountDownTimer?.cancel()
            milkCountDownTimer = null
        }
    }

    class Builder {
        internal var millisInFuture: Long = 0
        internal var countDownInterval: Long = 1000
        internal var onTickListener: ((MyTimer, Long) -> Unit)? = null
        internal var onFinishedListener: (() -> Unit)? = null

        fun setMillisInFuture(millisInFuture: Long): Builder {
            this.millisInFuture = millisInFuture
            return this
        }

        fun setCountDownInterval(countDownInterval: Long): Builder {
            this.countDownInterval = countDownInterval
            return this
        }

        fun setOnTickListener(onTickListener: (MyTimer, Long) -> Unit): Builder {
            this.onTickListener = onTickListener
            return this
        }

        fun setOnFinishedListener(onFinishedListener: () -> Unit): Builder {
            this.onFinishedListener = onFinishedListener
            return this
        }

        fun build(): MyTimer {
            return MyTimer(this)
        }
    }
}