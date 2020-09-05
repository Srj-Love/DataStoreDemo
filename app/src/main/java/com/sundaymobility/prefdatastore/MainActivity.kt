package com.sundaymobility.prefdatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.sundaymobility.prefdatastore.utils.SettingManager
import com.sundaymobility.prefdatastore.utils.UiMode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

//    init
    private lateinit var mSettingManager:SettingManager
    private var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        mSettingManager = SettingManager(applicationContext)
        observeUiPreferences()
        initView()
    }

    private fun initView() {
        iv_mode.setOnClickListener {
            lifecycleScope.launch {
                when (isDarkMode) {
                    true -> mSettingManager.changeUiMode(UiMode.LIGHT)
                    false -> mSettingManager.changeUiMode(UiMode.DARK)
                }
            }
        }
    }

    // Observe the data store
    private fun observeUiPreferences() {
        mSettingManager.uiModeFlow.asLiveData().observe(this){
            when(it){
                UiMode.LIGHT->enableLight()
                UiMode.DARK->disableLight()
            }
        }
    }

    private fun enableLight() {
        isDarkMode = false
        root_view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
        iv_mode.setImageResource(R.drawable.ic_light)
    }

    private fun disableLight() {
        isDarkMode = true
        root_view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
        iv_mode.setImageResource(R.drawable.iv_night_mode)
    }
}