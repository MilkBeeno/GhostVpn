package com.simple.ghostvpn.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.simple.ghostvpn.R
import com.simple.ghostvpn.ad.view.ConnectResultNativeAdView
import com.simple.ghostvpn.repository.AppRepository
import com.simple.ghostvpn.util.ktx.*

class ConnectResultActivity : AppCompatActivity() {
    private lateinit var ivBack: AppCompatImageView
    private lateinit var ivImage: AppCompatImageView
    private lateinit var tvResult: AppCompatTextView
    private lateinit var ivNetwork: ShapeableImageView
    private lateinit var tvNetwork: AppCompatTextView
    private lateinit var tvPing: AppCompatTextView
    private lateinit var nativeView: ConnectResultNativeAdView

    private val vpnImage by lazy { intent.getStringExtra(VPN_IMAGE).toString() }
    private val vpnName by lazy { intent.getStringExtra(VPN_NAME).toString() }
    private val vpnPing by lazy { intent.getLongExtra(VPN_PING, 0) }
    private val isConnected by lazy { intent.getBooleanExtra(IS_CONNECTED, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_result)
        initView()
        initNative()
    }

    private fun initView() {
        immersiveStatusBar()
        ivBack = findViewById(R.id.ivBack)
        ivBack.statusBarPadding()
        ivBack.setOnClickListener { finish() }

        ivImage = findViewById(R.id.ivImage)
        tvResult = findViewById(R.id.tvResult)
        ivNetwork = findViewById(R.id.ivNetwork)
        tvNetwork = findViewById(R.id.tvNetwork)
        tvPing = findViewById(R.id.tvPing)

        if (isConnected) {
            ivImage.setBackgroundResource(R.drawable.connect_result_connected)
            tvResult.setTextColor(color(R.color.FF4CB600))
            tvResult.text = string(R.string.connect_result_connected)
        } else {
            ivImage.setBackgroundResource(R.drawable.connect_result_disconnect)
            tvResult.setTextColor(color(R.color.FFE56E00))
            tvResult.text = string(R.string.connect_result_disconnect)
        }
        if (vpnImage.isNotBlank()) {
            Glide.with(this)
                .load(vpnImage)
                .placeholder(R.drawable.common_vpn_default)
                .into(ivNetwork)
        } else {
            ivNetwork.setImageResource(R.drawable.common_vpn_default)
        }
        tvNetwork.text =
            vpnName.ifBlank { string(R.string.common_auto_connect) }
        tvPing.text = vpnPing.toString().plus("ms")

    }

    private fun initNative() {
        // 原生广告展示和统计事件
        nativeView = findViewById(R.id.nativeView)
        nativeView.navigationBarPadding()
        nativeView.setLoadFailureRequest {
            if (isConnected) {

            } else {

            }
        }
        nativeView.setLoadSuccessRequest {
            if (isConnected) {

            } else {

            }
        }
        nativeView.setClickRequest {
            if (isConnected) {

            } else {

            }
        }
        when {
            isConnected && AppRepository.showConnectedNativeAd -> {
                nativeView.loadNativeAd()
            }
            !isConnected && AppRepository.showDisconnectNativeAd -> {
                nativeView.loadNativeAd()
            }
        }
    }

    companion object {
        private const val IS_CONNECTED = "IS_CONNECTED"
        private const val VPN_IMAGE = "VPN_IMAGE"
        private const val VPN_NAME = "VPN_NAME"
        private const val VPN_PING = "VPN_PING"
        fun start(
            context: Context,
            isConnected: Boolean,
            vpnImage: String,
            vpnName: String,
            vpnPing: Long
        ) {
            val intent = Intent(context, ConnectResultActivity::class.java)
            intent.putExtra(IS_CONNECTED, isConnected)
            intent.putExtra(VPN_IMAGE, vpnImage)
            intent.putExtra(VPN_NAME, vpnName)
            intent.putExtra(VPN_PING, vpnPing)
            context.startActivity(intent)
        }
    }
}