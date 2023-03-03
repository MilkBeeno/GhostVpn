package com.simple.ghostvpn.activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationManagerCompat
import com.milk.simple.ktx.*
import com.milk.simple.log.Logger
import com.milk.simple.mdr.KvManger
import com.simple.ghostvpn.R
import com.simple.ghostvpn.constant.KvKey
import com.simple.ghostvpn.dialog.NotificationDialog
import com.simple.ghostvpn.repository.AppRepository
import java.security.MessageDigest

class LauncherActivity : AppCompatActivity(), OnClickListener {
    private lateinit var rootView: ConstraintLayout
    private lateinit var ivSelect: AppCompatImageView
    private lateinit var tvStart: AppCompatTextView
    private lateinit var tvPrivacy: AppCompatTextView

    private val dialog by lazy { NotificationDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        AppRepository.getConfig()
        initView()
        getHasKey()
    }

    private fun initView() {
        if (KvManger.getBoolean(KvKey.FIRST_ENTER, true)) {
            immersiveStatusBar()

            rootView = findViewById(R.id.rootView)
            rootView.visible()

            ivSelect = findViewById(R.id.ivSelect)
            ivSelect.isSelected = true
            ivSelect.setOnClickListener(this)

            tvStart = findViewById(R.id.tvStart)
            tvStart.setOnClickListener(this)

            tvPrivacy = findViewById(R.id.tvPrivacy)
            tvPrivacy.setSpannableClick(
                Pair(string(R.string.launcher_privacy),
                    colorClickableSpan(color(R.color.FF1754FD)) {
                        WebViewActivity.start(this)
                    })
            )

            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                val ft = supportFragmentManager.beginTransaction()
                dialog.show(ft, "Notification")
            }

            KvManger.put(KvKey.FIRST_ENTER, false)
        } else {
            LottieActivity.start(this, true)
            finish()
        }
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun getHasKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val key = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Logger.d("包名是" + packageName + "密钥是：" + key, "KeyHash")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            ivSelect -> {
                ivSelect.isSelected = !ivSelect.isSelected
            }
            tvStart -> {
                if (ivSelect.isSelected) {
                    MainActivity.start(this)
                    finish()
                } else {
                    showToast(string(R.string.launch_privacy_agreement))
                }
            }
        }
    }
}