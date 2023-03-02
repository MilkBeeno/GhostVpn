package com.simple.ghostvpn.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simple.ghostvpn.R

class LottieActivity : AppCompatActivity() {
    private val fromLauncher by lazy { intent?.getBooleanExtra(FROM_LAUNCHER, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie)
    }

    companion object {
        private const val FROM_LAUNCHER = "FROM_LAUNCHER"
        fun start(context: Context, fromLauncher: Boolean) {
            val intent = Intent(context, LottieActivity::class.java)
            intent.putExtra(FROM_LAUNCHER, fromLauncher)
            context.startActivity(intent)
        }
    }
}