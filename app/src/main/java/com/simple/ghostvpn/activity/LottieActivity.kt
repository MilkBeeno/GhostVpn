package com.simple.ghostvpn.activity

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.simple.ghostvpn.R
import com.simple.ghostvpn.ad.AppOpenAdHelper
import com.simple.ghostvpn.repository.AppRepository
import com.simple.ghostvpn.util.MyTimer

class LottieActivity : AppCompatActivity() {
    private val appOpenAdHelper by lazy { AppOpenAdHelper() }
    private lateinit var lottieView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie)
        initView()
        if (AppRepository.showOpenAd) {
            loadInterstitialAd()
        } else {
            addAnimationListener()
        }
    }

    private fun initView() {
        lottieView = findViewById(R.id.lottieView)
        lottieView.setAnimation("open_loading.json")
        lottieView.playAnimation()
    }

    private fun loadInterstitialAd() {
        MyTimer.Builder()
            .setCountDownInterval(1000)
            .setMillisInFuture(12000)
            .setOnFinishedListener {
                if (!appOpenAdHelper.isShowSuccessfulAd()) {
                    end()
                }
            }
            .build()
            .run()
        appOpenAdHelper.load(
            context = this,
            failure = {
                end()
            },
            success = {
                showInterstitialAd()
            }
        )
    }

    private fun showInterstitialAd() {
        appOpenAdHelper.show(
            activity = this,
            failure = {
                end()
            },
            success = {

            },
            click = {

            },
            close = {
                end()
            }
        )
    }

    private fun addAnimationListener() {
        lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) = Unit
            override fun onAnimationEnd(p0: Animator) = end()
            override fun onAnimationCancel(p0: Animator) = Unit
            override fun onAnimationRepeat(p0: Animator) = Unit
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private const val FROM_LAUNCHER = "FROM_LAUNCHER"
        fun start(context: Context, fromLauncher: Boolean) {
            val intent = Intent(context, LottieActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(FROM_LAUNCHER, fromLauncher)
            context.startActivity(intent)
        }
    }
}