package com.simple.ghostvpn.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.jeremyliao.liveeventbus.LiveEventBus
import com.simple.ghostvpn.R
import com.simple.ghostvpn.ad.view.VpnListNativeAdView
import com.simple.ghostvpn.constant.KvKey
import com.simple.ghostvpn.data.VpnGroup
import com.simple.ghostvpn.data.VpnNode
import com.simple.ghostvpn.repository.AppRepository
import com.simple.ghostvpn.repository.VpnRepository
import com.simple.ghostvpn.util.ktx.ioScope
import com.simple.ghostvpn.util.ktx.withMain
import com.simple.ghostvpn.util.log.Logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class VpnListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var ivBack: AppCompatImageView
    private lateinit var ivRefresh: AppCompatImageView
    private lateinit var nativeView: VpnListNativeAdView
    private lateinit var rvNode: RecyclerView

    private val random by lazy { Random() }
    private val vpnNodeId by lazy { intent.getLongExtra(VPN_NODE_ID, 0) }
    private val vpnConnect by lazy { intent.getBooleanExtra(VPN_CONNECT, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn_list)
        initView()
        initNativeAd()
        loadVpnList()
    }

    private fun initView() {
        ivBack = findViewById(R.id.ivBack)
        ivBack.setOnClickListener(this)
        ivRefresh = findViewById(R.id.ivRefresh)
        ivRefresh.setOnClickListener(this)
        nativeView = findViewById(R.id.nativeView)
        rvNode = findViewById(R.id.rvNode)
        ivBack.setOnClickListener(this)
        ivRefresh.setOnClickListener(this)
    }

    private fun loadVpnList() {
        ioScope {
            val response = VpnRepository.getVpnListInfo()
            val result = response.data
            if (response.code == 2000 && result != null) {
                val groups = arrayListOf<VpnGroup>()
                groups.add(VpnGroup().apply {
                    if (vpnNodeId <= 0) {
                        isSelect = true
                    }
                })
                val map = result.groupBy { it.areaCode }
                map.forEach {
                    val vpnListModels = it.value
                    if (vpnListModels.isNotEmpty()) {
                        val group = VpnGroup()
                        group.areaImage = vpnListModels[0].areaImage
                        group.areaName = vpnListModels[0].areaName
                        val nodes = arrayListOf<VpnNode>()
                        vpnListModels.forEachIndexed { index, vpnListModel ->
                            val node = VpnNode()
                            node.nodeId = vpnListModel.nodeId
                            node.areaImage = vpnListModel.areaImage
                            node.areaName = vpnListModel.areaName
                            ioScope { node.ping = ping(vpnListModel.nodeDns) }
                            node.isSelect = vpnListModel.nodeId == vpnNodeId
                            node.itemSize = vpnListModels.size
                            node.position = index
                            // 有一个匹配上表示已经连接过
                            if (vpnListModel.nodeId == vpnNodeId) {
                                group.isSelect = true
                            }
                            nodes.add(node)
                        }
                        group.itemSublist = nodes
                        groups.add(group)
                    }
                }
                withMain {
                    updateVpnList(groups)
                }
            }
        }
    }

    private fun ping(ip: String = "54.67.15.250"): Int {
        val r = Runtime.getRuntime().exec("ping -c 1 $ip")
        val bufferedReader = BufferedReader(InputStreamReader(r.inputStream))
        while (true) {
            val line: String = bufferedReader.readLine() ?: break
            if (!line.startsWith("rtt")) continue
            val speed = line.split("=")[1].split("/")[1]
            return speed.toFloat().toInt()
        }
        return 0
    }

    private fun updateVpnList(vpnList: ArrayList<VpnGroup>) {
        rvNode.linear().setup {
            addType<VpnGroup>(R.layout.item_vpn_list_group)
            addType<VpnNode>(R.layout.item_vpn_list_node)
            R.id.ivGroupExpand.onClick {
                expandOrCollapse()
            }
            R.id.ivGroupSelect.onClick {
                val vpnGroup = this._data as VpnGroup
                if (vpnGroup.isSelect) return@onClick
                val nodes = vpnGroup.itemSublist
                when {
                    nodes != null && nodes.isNotEmpty() -> {
                        val index = random.nextInt(nodes.size)
                        val node = nodes[index] as VpnNode
                        LiveEventBus.get<ArrayList<String>>(KvKey.SWITCH_VPN_NODE)
                            .post(
                                arrayListOf(
                                    node.nodeId.toString(),
                                    node.areaName,
                                    node.areaImage,
                                    node.ping.toString()
                                )
                            )
                    }
                    else -> {
                        LiveEventBus.get<ArrayList<String>>(KvKey.SWITCH_VPN_NODE)
                            .post(arrayListOf("0", "", "", "0"))
                    }
                }
                finish()
            }
            R.id.ivNodeSelect.onClick {
                val node = this._data as VpnNode
                if (node.isSelect) return@onClick
                LiveEventBus.get<ArrayList<String>>(KvKey.SWITCH_VPN_NODE)
                    .post(
                        arrayListOf(
                            node.nodeId.toString(),
                            node.areaName,
                            node.areaImage,
                            node.ping.toString()
                        )
                    )
                finish()
            }
        }.models = vpnList
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

    override fun onClick(p0: View?) {
        when (p0) {
            ivBack -> {
                finish()
            }
            ivRefresh -> {
                loadVpnList()
            }
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