package com.simple.ghostvpn.activity

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.simple.ghostvpn.R

class LottieActivity : AppCompatActivity() {
    private lateinit var lottieView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie)
        initView()
        addAnimationListener()
    }

    private fun initView() {
        lottieView = findViewById(R.id.lottieView)
        lottieView.setAnimation("open_loading.json")
        lottieView.playAnimation()
    }

    private fun addAnimationListener() {
        lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) = end()
        })
    }

    private fun end() {
        val fromLauncher =
            intent?.getBooleanExtra(FROM_LAUNCHER, false) ?: false
        if (fromLauncher) {
            MainActivity.start(this)
        }
        finish()
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