package com.simple.ghostvpn.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.simple.ghostvpn.R
import com.simple.ghostvpn.util.NotificationManager

class NotificationDialog : DialogFragment(), View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var tvConfirm: AppCompatTextView
    private lateinit var tvRefuse: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rootView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_notification, null, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvConfirm = rootView.findViewById(R.id.tvConfirm)
        tvRefuse = rootView.findViewById(R.id.tvRefuse)
        tvConfirm.setOnClickListener(this)
        tvRefuse.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tvConfirm -> {
                NotificationManager.obtainNotification(v.context)
                dismiss()
            }
            tvRefuse -> {
                dismiss()
            }
        }
    }
}