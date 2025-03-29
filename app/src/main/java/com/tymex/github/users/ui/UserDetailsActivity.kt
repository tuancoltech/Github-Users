package com.tymex.github.users.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil3.load
import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.UserDetails
import com.tymex.github.data.core.viewmodel.UserViewModel
import com.tymex.github.data.core.viewmodel.UserViewModelImpl
import com.tymex.github.users.R
import com.tymex.github.users.databinding.ActivityUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsActivity : BaseActivity() {

    private lateinit var usersViewModel: UserViewModel
    private lateinit var binding: ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val login = intent.getStringExtra(UsersActivity.BUNDLE_KEY_LOGIN)
                    ?: return@repeatOnLifecycle
                usersViewModel.getUserDetails(login).collect { flowState ->
                    handleUserDetailsFlow(flowState)
                }
            }
        }
    }

    private fun initViewModel() {
        usersViewModel = ViewModelProvider(this)[UserViewModelImpl::class.java]
    }

    private fun handleUserDetailsFlow(flowState: FlowState) {
        when (flowState) {
            is FlowState.Loading -> {
                binding.pbLoading.root.visible()
            }

            is FlowState.Success<*> -> {
                binding.pbLoading.root.gone()
                val userDetails = flowState.data as? UserDetails ?: return
                Log.i(TAG, "getUserDetails success: $userDetails")
                binding.layoutUserCard.tvName.text = userDetails.login
                binding.layoutUserCard.ivAvatar.load(userDetails.avatarUrl)

                if (userDetails.location?.isNotEmpty() == true) {
                    binding.layoutUserCard.tvUrl.text = userDetails.location
                    binding.layoutUserCard.tvUrl.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        ContextCompat.getDrawable(this@UserDetailsActivity,
                            R.drawable.ic_location), null,  null, null
                    )
                }
                binding.tvFollowers.text = getString(R.string.followers_count, userDetails.followers)
                binding.tvFollowing.text = getString(R.string.following_count, userDetails.following)
                binding.tvBlog.text = userDetails.blog
            }

            is FlowState.Error -> {
                binding.pbLoading.root.gone()
                Log.e(TAG, "getUsers error: " + flowState.message)
                Toast.makeText(
                    this@UserDetailsActivity,
                    getString(R.string.toast_error_message, flowState.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.toolbar_title_user_details)
    }

    companion object {
        private val TAG by lazy { UsersActivity::class.java.simpleName }
    }
}