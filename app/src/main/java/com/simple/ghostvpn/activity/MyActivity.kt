package com.simple.ghostvpn.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.simple.ghostvpn.R
import com.simple.ghostvpn.util.ktx.immersiveStatusBar
import com.simple.ghostvpn.util.ktx.statusBarPadding

class MyActivity : AppCompatActivity(), OnClickListener {
    private lateinit var ivBack: AppCompatImageView
    private lateinit var llPrivacy: LinearLayoutCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)
        immersiveStatusBar()

        ivBack = findViewById(R.id.ivBack)
        ivBack.statusBarPadding()
        ivBack.setOnClickListener(this)

        llPrivacy = findViewById(R.id.llPrivacy)
        llPrivacy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            ivBack -> {
                finish()
            }
            llPrivacy -> {
                WebViewActivity.start(this)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MyActivity::class.java))
        }
    }
}