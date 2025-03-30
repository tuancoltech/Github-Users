package com.tymex.github.users.ui

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.tymex.github.users.databinding.ToolbarBinding

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayShowCustomEnabled(true)

            val titleBinding = ToolbarBinding.inflate(layoutInflater)
            titleBinding.tvTitle.text = getToolbarTitle()
            titleBinding.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            val actionBarParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL
            )
            it.setCustomView(titleBinding.root, actionBarParams)
        }

        window.apply {
            setLightStatusBar()
            setStatusBarColorCompat(android.R.color.white)
        }

    }

    /**
     * Override this to set Toolbar's title text
     */
    abstract fun getToolbarTitle(): String
}