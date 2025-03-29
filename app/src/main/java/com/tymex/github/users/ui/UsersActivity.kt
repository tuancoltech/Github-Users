package com.tymex.github.users.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tymex.github.data.core.data.model.FlowState
import com.tymex.github.data.core.data.model.GetUsersResult
import com.tymex.github.data.core.viewmodel.UserViewModel
import com.tymex.github.data.core.viewmodel.UserViewModelImpl
import com.tymex.github.users.R
import com.tymex.github.users.databinding.ActivityUsersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersActivity : BaseActivity() {

    private lateinit var usersViewModel: UserViewModel
    private lateinit var binding: ActivityUsersBinding
    private lateinit var userAdapter: UserAdapter

    private var currentPage = 1
    private var hasMore = false
    private var isLoadingMore = false

    companion object {
        private const val PAGE_SIZE = 20
        const val BUNDLE_KEY_LOGIN = "bundle_key_login"
        private val TAG by lazy { UsersActivity::class.java.simpleName }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initUsersList()
        initViewModel()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                usersViewModel.getUsers(PAGE_SIZE, currentPage).collect { flowState ->
                    handleUsersFlow(flowState)
                }
            }
        }
    }

    private fun initUsersList() {
        userAdapter = UserAdapter(onUserClicked = { user ->
            Log.d(TAG, "user clicked: " + user.login)
            val userDetailsIntent = Intent(this@UsersActivity,
                UserDetailsActivity::class.java
            )
            userDetailsIntent.putExtra(BUNDLE_KEY_LOGIN, user.login)
            startActivity(userDetailsIntent)
        })
        val layoutMgr = LinearLayoutManager(this@UsersActivity)
        binding.rvUsers.apply {
            layoutManager = layoutMgr
            adapter = userAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItem: Int =
                        layoutMgr.findLastCompletelyVisibleItemPosition()
                    if (lastVisibleItem == userAdapter.itemCount -1 && hasMore && !isLoadingMore) {
                        binding.rvUsers.post {
                            loadMore()
                        }
                    }
                }
            })
        }
    }

    private fun initViewModel() {
        usersViewModel = ViewModelProvider(this)[UserViewModelImpl::class.java]
    }

    private fun loadMore() {
        Log.d(TAG, "loadMore")
        isLoadingMore = true
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                usersViewModel.getUsers(PAGE_SIZE, ++ currentPage).collect { flowState ->
                    handleUsersFlow(flowState)
                }
            }
        }
    }

    private fun handleUsersFlow(flowState: FlowState) {
        when (flowState) {
            is FlowState.Loading -> {
                binding.pbLoading.root.visible()
            }

            is FlowState.Success<*> -> {
                binding.pbLoading.root.gone()
                val getUserResult = flowState.data as? GetUsersResult ?: return
                Log.i(TAG,
                    "getUsers success: " + getUserResult.users.size
                            + ". isFinal: " + getUserResult.isFinalPage + ". curPage: " + currentPage
                )
                hasMore = !getUserResult.isFinalPage
                isLoadingMore = false

                if (currentPage > 1) {
                    userAdapter.appendUsers(getUserResult.users)
                } else {
                    userAdapter.setData(getUserResult.users)
                }
            }

            is FlowState.Error -> {
                binding.pbLoading.root.gone()
                Log.e(TAG, "getUsers error: " + flowState.message)
                Toast.makeText(this@UsersActivity,
                    "Error: " + flowState.message, Toast.LENGTH_SHORT).show()
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
        return getString(R.string.app_name)
    }
}