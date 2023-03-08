package com.simple.ghostvpn.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simple.ghostvpn.R
import com.simple.ghostvpn.ad.view.VpnListNativeAdView
import com.simple.ghostvpn.repository.AppRepository
import com.simple.ghostvpn.util.log.Logger

class VpnListActivity : AppCompatActivity() {
    private lateinit var nativeView: VpnListNativeAdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn_list)
        initView()
        initNativeAd()
    }

    private fun initView() {
        nativeView = findViewById(R.id.nativeView)
    }

    private fun initNativeAd() {
        if (AppRepository.showVpnListNativeAd) {
            Logger.d("开始请求 VPN List Native 广告", "nativeAdLog")
            nativeView.setLoadFailureRequest {
                Logger.d("请求失败 VPN List Native 广告", "nativeAdLog")
            }
            nativeView.setLoadSuccessRequest {
                Logger.d("请求成功 VPN List Native 广告", "nativeAdLog")
            }
            nativeView.setClickRequest {
                Logger.d("点击了 VPN List Native 广告", "nativeAdLog")
            }
            nativeView.loadNativeAd()
        }
    }

    companion object {
        private const val VPN_NODE_ID = "VPN_NODE_ID"
        private const val VPN_CONNECT = "VPN_CONNECT"
        fun start(context: Context, vpnNodeId: Long, vpnConnect: Boolean) {
            val intent = Intent(context, VpnListActivity::class.java)
            intent.putExtra(VPN_NODE_ID, vpnNodeId)
            intent.putExtra(VPN_CONNECT, vpnConnect)
            context.startActivity(intent)
        }
    }
}