package com.simple.ghostvpn.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.milk.simple.ktx.*
import com.simple.ghostvpn.R
import com.simple.ghostvpn.ad.view.MainNativeAdView
import com.simple.ghostvpn.repository.AppRepository

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var flBackground: FrameLayout
    private lateinit var flHeader: FrameLayout
    private lateinit var ivMenu: AppCompatImageView
    private lateinit var ivShare: AppCompatImageView
    private lateinit var clSwitch: ConstraintLayout
    private lateinit var ivCountry: AppCompatImageView
    private lateinit var tvCountry: AppCompatTextView
    private lateinit var ivConnectBackground: AppCompatImageView
    private lateinit var ivConnect: AppCompatImageView
    private lateinit var tvConnect: AppCompatTextView
    private lateinit var nativeAdView: MainNativeAdView

    private var vpnNodeId: Int = 0
    private var vpnImage: String = ""
    private var vpnName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initNativeAd()
        updateCountryInfo()
        connected()
    }

    private fun initView() {
        immersiveStatusBar()
        flBackground = findViewById(R.id.flBackground)
        flHeader = findViewById(R.id.flHeader)
        flHeader.statusBarPadding()
        ivMenu = findViewById(R.id.ivMenu)
        ivMenu.setOnClickListener(this)
        ivShare = findViewById(R.id.ivShare)
        ivShare.setOnClickListener(this)
        clSwitch = findViewById(R.id.clSwitch)
        clSwitch.setOnClickListener(this)
        ivCountry = findViewById(R.id.ivCountry)
        tvCountry = findViewById(R.id.tvCountry)
        ivConnectBackground = findViewById(R.id.ivConnectBackground)
        ivConnect = findViewById(R.id.ivConnect)
        ivConnect.setOnClickListener(this)
        tvConnect = findViewById(R.id.tvConnect)
        tvConnect.setOnClickListener(this)
        nativeAdView = findViewById(R.id.nativeAdView)
    }

    private fun initNativeAd() {
        if (AppRepository.showMainNativeAd) {
            nativeAdView.setLoadFailureRequest {
            }
            nativeAdView.setLoadSuccessRequest {
            }
            nativeAdView.setClickRequest {
            }
            nativeAdView.loadNativeAd()
        }
    }

    private fun updateCountryInfo() {
        if (vpnNodeId > 0) {
            Glide.with(this)
                .load(vpnImage)
                .placeholder(R.drawable.common_vpn_default)
                .into(ivCountry)
            tvCountry.text = vpnName
        } else {
            ivCountry.setImageResource(R.drawable.common_vpn_default)
            tvCountry.text = string(R.string.common_auto_connect)
        }
    }

    private fun connected() {
        flBackground.visible()
        ivMenu.setImageResource(R.drawable.main_menu_connected)
        ivShare.setImageResource(R.drawable.main_share_connected)
        clSwitch.setBackgroundResource(R.drawable.shape_main_switch_vpn_connected_background)
        ivConnectBackground.setImageResource(R.drawable.main_connected_background)
        ivConnect.setImageResource(R.drawable.main_connected_image)
        tvConnect.setBackgroundResource(R.drawable.shape_main_connected_background)
        tvConnect.text = string(R.string.main_disconnect)
        tvConnect.setTextColor(color(R.color.FF1754FD))
    }

    private fun disconnect() {
        flBackground.gone()
        ivMenu.setImageResource(R.drawable.main_menu_disconnect)
        ivShare.setImageResource(R.drawable.main_share_disconnect)
        clSwitch.setBackgroundResource(R.drawable.shape_main_switch_vpn_disconnect_background)
        ivConnectBackground.setImageResource(R.drawable.main_disconnect_background)
        ivConnect.setImageResource(R.drawable.main_disconnect_image)
        tvConnect.setBackgroundResource(R.drawable.shape_main_disconnect_background)
        tvConnect.text = string(R.string.main_connect)
        tvConnect.setTextColor(color(R.color.white))
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            ivMenu -> {
                MyActivity.start(this)
            }
            ivShare -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, AppRepository.shareAppUrl)
                intent.type = "text/plain"
                startActivity(intent)
            }
            ivConnect, tvConnect -> {

            }
        }
    }
}