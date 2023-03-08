package com.simple.ghostvpn.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.simple.ghostvpn.R
import com.simple.ghostvpn.util.ktx.string

class ConnectingDialog(private val act: FragmentActivity) : DialogFragment() {
    private lateinit var rootView: View
    private lateinit var tvDesc: AppCompatTextView
    private lateinit var lottieView: LottieAnimationView

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
            .inflate(R.layout.dialog_waiting, null, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lottieView = rootView.findViewById(R.id.lottieView)
        lottieView.setAnimation("connect_loading.json")
        lottieView.playAnimation()
        tvDesc = rootView.findViewById(R.id.tvDesc)
        tvDesc.text = context?.string(R.string.dialog_connecting_now)
    }

    fun show() {
        try {
            val fm = act.supportFragmentManager
            val prev = fm.findFragmentByTag("Connecting")
            val ft = fm.beginTransaction()
            if (prev != null) {
                ft.remove(prev)
            }
            ft.add(this, "Connecting")
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}