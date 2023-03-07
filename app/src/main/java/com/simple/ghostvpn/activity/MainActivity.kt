package com.simple.ghostvpn.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.freetech.vpn.data.VpnProfile
import com.freetech.vpn.data.VpnType
import com.freetech.vpn.logic.VpnStateService
import com.simple.ghostvpn.R
import com.simple.ghostvpn.ad.InterstitialAdHelper
import com.simple.ghostvpn.ad.view.MainNativeAdView
import com.simple.ghostvpn.data.VpnModel
import com.simple.ghostvpn.dialog.ConnectFailureDialog
import com.simple.ghostvpn.dialog.ConnectingDialog
import com.simple.ghostvpn.dialog.DisconnectDialog
import com.simple.ghostvpn.repository.AppRepository
import com.simple.ghostvpn.repository.VpnRepository
import com.simple.ghostvpn.util.MyTimer
import com.simple.ghostvpn.util.ktx.*
import com.simple.ghostvpn.util.log.Logger

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

    private var vpnNodeId: Long = 0
    private var vpnName: String = ""
    private var vpnImage: String = ""
    private var vpnPing: Long = 0
    private var vpnIsConnected: Boolean = false

    private var isConnecting: Boolean = false
    private var notShowResultUi: Boolean = false

    private val connectingDialog by lazy { ConnectingDialog(this) }
    private val disconnectDialog by lazy { DisconnectDialog(this) }
    private val connectFailureDialog by lazy { ConnectFailureDialog(this) }

    private var vpnService: VpnStateService? = null
    private var vpnServiceConnection: ServiceConnection? = null
    private var vpnStateListener: VpnStateService.VpnStateListener? = null

    private val contract = ActivityResultContracts.StartActivityForResult()
    private val activityResultLauncher = registerForActivityResult(contract) {
        if (it.resultCode == Activity.RESULT_OK) {
            getVpnProfile()
        }
    }

    private val interstitialAdHelper by lazy { InterstitialAdHelper() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initNativeAd()
        updateCountryInfo()
        bindVpnService()
        disconnect()
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

    private fun bindVpnService() {
        vpnServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
                vpnService = (service as VpnStateService.LocalBinder).service
                vpnService?.registerListener(vpnStateListener)
                vpnService?.setUserTimeout(10)
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                vpnService = null
            }
        }
        vpnStateListener = VpnStateService.VpnStateListener {
            when (vpnService?.errorState) {
                VpnStateService.ErrorState.NO_ERROR ->
                    when (vpnService?.state) {
                        VpnStateService.State.DISABLED -> {
                            vpnIsConnected = false
                            isConnecting = false
                            disconnect()
                            if (notShowResultUi) {
                                notShowResultUi = false
                                Logger.d("VPN 断开不显示UI", "main_vpn")
                            } else {
                                showConnectResult(false)
                                Logger.d("VPN 断开显示UI", "main_vpn")
                            }
                        }
                        VpnStateService.State.CONNECTED -> {
                            vpnIsConnected = true
                            isConnecting = false
                            connected()
                            showConnectResult(true)
                            Logger.d("VPN 连接成功", "main_vpn")
                        }
                        VpnStateService.State.CONNECTING -> {
                            isConnecting = true
                            Logger.d("VPN 正在连接", "main_vpn")
                        }
                        else -> Unit
                    }
                else -> {
                    vpnIsConnected = false
                    notShowResultUi = false
                    isConnecting = false
                    disconnect()
                    connectFailureDialog.show()
                    connectingDialog.dismissAllowingStateLoss()
                    Logger.d("VPN 连接错误", "main_vpn")
                }
            }
        }
        vpnServiceConnection?.let {
            val intent = Intent(this, VpnStateService::class.java)
            bindService(intent, it, VpnStateService.BIND_AUTO_CREATE)
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

    private fun showConnectResult(isConnected: Boolean) {
        when {
            isConnected && AppRepository.showConnectedInsertAd -> {
                loadInterstitialAd(this) {
                    showInsertAd(true)
                }
            }
            !isConnected && AppRepository.showDisconnectInsertAd -> {
                loadInterstitialAd(this) {
                    showInsertAd(false)
                }
            }
            else -> {
                showInsertAd(isConnected)
            }
        }
    }

    private fun loadInterstitialAd(activity: Activity, finishRequest: () -> Unit) {
        MyTimer.Builder()
            .setMillisInFuture(12000)
            .setOnFinishedListener {
                if (!interstitialAdHelper.isShowSuccessfulAd()) {
                    finishRequest()
                }
            }
            .build()
            .run()

        interstitialAdHelper.load(
            context = activity,
            failure = {
                finishRequest()
            },
            success = {
                interstitialAdHelper.show(
                    activity = activity,
                    failure = {
                        finishRequest()
                    },
                    success = {

                    },
                    click = {

                    },
                    close = {
                        finishRequest()
                    }
                )
            }
        )
    }

    private fun showInsertAd(isConnected: Boolean) {
        if (isConnected) {
            connectingDialog.dismissAllowingStateLoss()
        } else {
            disconnectDialog.dismissAllowingStateLoss()
        }
        ConnectResultActivity.start(this, isConnected, vpnImage, vpnName, vpnPing)
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
                if (vpnIsConnected) {
                    disconnectDialog.show()
                    tvConnect.postDelayed({
                        vpnService?.disconnect()
                    }, 4000)
                } else {
                    tryOpenVpn()
                }
            }
        }
    }

    private fun tryOpenVpn() {
        val prepare = VpnService.prepare(this)
        if (prepare == null) {
            getVpnProfile()
        } else {
            activityResultLauncher.launch(prepare)
        }
    }

    private fun getVpnProfile() {
        connectingDialog.show()
        launch {
            val response = VpnRepository.getVpnInfo(vpnNodeId)
            val result = response.data
            withMain {
                if (response.code == 2000 && result != null) {
                    if (vpnIsConnected) {
                        notShowResultUi = true
                        vpnService?.disconnect()
                    }
                    connectVpn(convertVpnProfile(result))
                } else {
                    tvConnect.postDelayed({
                        connectFailureDialog.show()
                        connectingDialog.dismissAllowingStateLoss()
                    }, 4000)
                }
            }
        }
    }

    private fun convertVpnProfile(vpnModel: VpnModel): VpnProfile {
        val vpnProfile = VpnProfile()
        vpnProfile.id = vpnModel.nodeId
        vpnProfile.name = vpnModel.nodeName
        vpnProfile.gateway = vpnModel.dns
        vpnProfile.username = vpnModel.userName
        vpnProfile.password = vpnModel.password
        vpnProfile.mtu = 1400
        vpnProfile.vpnType = VpnType.fromIdentifier("ikev2-eap")
        return vpnProfile
    }

    private fun connectVpn(vpnProfile: VpnProfile) {
        if (isConnecting) return
        isConnecting = true
        // 设置默认一分钟未连上为超时操作
        MyTimer.Builder()
            .setCountDownInterval(1000)
            .setMillisInFuture(15 * 1000)
            .setOnFinishedListener {
                if (isConnecting) {
                    notShowResultUi = true
                    vpnService?.disconnect()
                    connectFailureDialog.show()
                    connectingDialog.dismissAllowingStateLoss()
                }
            }
            .build()
            .run()
        val profileInfo = Bundle()
        profileInfo.putSerializable(PROFILE, vpnProfile)
        profileInfo.putInt(G_ID, vpnProfile.id.toInt())
        vpnService?.connect(profileInfo, true)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        vpnService?.unregisterListener(vpnStateListener)
        vpnServiceConnection?.let { unbindService(it) }
    }

    companion object {
        private const val G_ID = "b01"
        private const val PROFILE = "profile"
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}