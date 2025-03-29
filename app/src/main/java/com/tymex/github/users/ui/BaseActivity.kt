package com.tymex.github.users.ui

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.tymex.github.users.databinding.CustomActionbarTitleBinding

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayShowCustomEnabled(true)

            val titleBinding = CustomActionbarTitleBinding.inflate(layoutInflater)
            titleBinding.root.text = getToolbarTitle()
            val actionBarParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL
            )
            it.setCustomView(titleBinding.root, actionBarParams)
        }
    }

    abstract fun getToolbarTitle(): String
}