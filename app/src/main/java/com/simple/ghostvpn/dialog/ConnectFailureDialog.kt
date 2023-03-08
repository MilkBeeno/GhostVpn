package com.simple.ghostvpn.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.simple.ghostvpn.R

class ConnectFailureDialog(private val act: FragmentActivity) : DialogFragment() {
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0)
        val params = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        rootView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_failure, null, false)
        return rootView
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView.setOnTouchListener { p0, p1 ->
            super.dismissAllowingStateLoss()
            true
        }
    }

    fun show() {
        try {
            val fm = act.supportFragmentManager
            val prev = fm.findFragmentByTag("ConnectFailure")
            val ft = fm.beginTransaction()
            if (prev != null) {
                ft.remove(prev)
            }
            ft.add(this, "ConnectFailure")
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}