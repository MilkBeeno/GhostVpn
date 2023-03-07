package com.simple.ghostvpn.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.simple.ghostvpn.R

class VpnListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn_list)
    }
    companion object{
        fun start(context: Context){

        }
    }
}