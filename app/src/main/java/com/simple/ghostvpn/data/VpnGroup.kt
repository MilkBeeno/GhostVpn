package com.simple.ghostvpn.data

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.drake.brv.item.ItemExpand
import com.drake.brv.item.ItemHover
import com.drake.brv.item.ItemPosition
import com.google.android.material.imageview.ShapeableImageView
import com.simple.ghostvpn.R
import com.simple.ghostvpn.util.ktx.gone
import com.simple.ghostvpn.util.ktx.string
import com.simple.ghostvpn.util.ktx.visible

class VpnGroup : ItemExpand, ItemHover, ItemPosition, ItemBind {
    var areaImage: String = ""
    var areaName: String = ""
    var isSelect: Boolean = false
    var isAutoSelectItem: Boolean = false
    override var itemExpand: Boolean = false
    override var itemGroupPosition: Int = 0
    override var itemSublist: List<Any?>? = null
    override var itemHover: Boolean = false
    override var itemPosition: Int = 0

    override fun onBind(holder: BindingAdapter.BindingViewHolder) {

        val ivGroupImage = holder.findView<ShapeableImageView>(R.id.ivGroupImage)
        val tvGroupName = holder.findView<AppCompatTextView>(R.id.tvGroupName)
        val ivGroupExpand = holder.findView<AppCompatImageView>(R.id.ivGroupExpand)
        val ivGroupSelect = holder.findView<AppCompatImageView>(R.id.ivGroupSelect)

        holder.itemView.setBackgroundResource(
            if (itemExpand) {
                R.drawable.shape_vpn_list_group_expand_background
            } else {
                R.drawable.shape_vpn_list_group_close_background
            }
        )
        when {
            itemSublist != null -> {
                Glide.with(holder.context)
                    .load(areaImage)
                    .placeholder(R.drawable.common_vpn_default)
                    .into(ivGroupImage)
                tvGroupName.text = areaName
                    .plus("(")
                    .plus(itemSublist?.size.toString())
                    .plus(")")
                ivGroupExpand.setImageResource(
                    if (itemExpand) {
                        R.drawable.switch_node_packup
                    } else {
                        R.drawable.switch_node_expand
                    }
                )
                ivGroupExpand.visible()
            }
            else -> {
                tvGroupName.text =
                    holder.context.string(R.string.common_auto_connect)
                ivGroupImage.setImageResource(R.drawable.common_vpn_default)
                ivGroupExpand.gone()
            }
        }
        ivGroupSelect.isSelected = isSelect
    }
}