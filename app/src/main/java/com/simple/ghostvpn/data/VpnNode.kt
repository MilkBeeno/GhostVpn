package com.simple.ghostvpn.data

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.simple.ghostvpn.R

class VpnNode : ItemBind {
    var nodeId: Long = 0
    var areaImage: String = ""
    var areaName: String = ""
    var ping: Int = 0
    var isSelect: Boolean = false
    var itemSize: Int = 0
    var position: Int = 0
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {

        val ivNodeImage = holder.findView<AppCompatImageView>(R.id.ivNodeImage)
        val ivNodeSelect = holder.findView<AppCompatImageView>(R.id.ivNodeSelect)
        val tvNodeName = holder.findView<AppCompatTextView>(R.id.tvNodeName)
        val tvPing = holder.findView<AppCompatTextView>(R.id.tvPing)

        holder.itemView.setBackgroundResource(
            if (position == itemSize - 1)
                R.drawable.shape_vpn_list_node_footer_background
            else
                R.drawable.shape_vpn_list_node_background
        )
        Glide.with(holder.context)
            .load(areaImage)
            .placeholder(R.drawable.common_vpn_default)
            .into(ivNodeImage)
        ivNodeSelect.isSelected = isSelect
        tvNodeName.text = areaName
        tvPing.text = ping.toString().plus("ms")
    }
}