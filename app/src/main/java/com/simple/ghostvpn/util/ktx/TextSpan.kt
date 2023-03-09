package com.simple.ghostvpn.util.ktx

import android.graphics.Color
import android.text.NoCopySpan
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView.setSpannableClick(vararg targets: Pair<String, ClickableSpan>) {
    try {
        val content = text.toString()
        if (content.isNotBlank()) {
            val builder = SpannableStringBuilder(content)
            targets.forEach {
                if (content.contains(it.first)) {
                    val startIndex = content.indexOf(it.first, ignoreCase = true)
                    val endIndex = startIndex + it.first.length
                    if (startIndex >= 0 && endIndex < content.length && startIndex < endIndex) {
                        val colorFlags = Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        builder.setSpan(it.second, startIndex, endIndex, colorFlags)
                    }
                }
            }
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            text = builder
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

abstract class NoCopyClickableSpan : ClickableSpan(), NoCopySpan {
    override fun onClick(p0: View) = Unit
}

fun colorClickableSpan(color: Int, clickRequest: () -> Unit) = object : NoCopyClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.isUnderlineText = false
        ds.clearShadowLayer()
    }

    override fun onClick(p0: View) = clickRequest()
}
