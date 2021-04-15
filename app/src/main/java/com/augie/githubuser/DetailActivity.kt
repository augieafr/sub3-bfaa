package com.augie.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.augie.githubuser.adapter.SectionsPagerAdapter
import com.augie.githubuser.databinding.ActivityDetailBinding
import com.augie.githubuser.repository.DetailUserRepository
import com.augie.githubuser.viewmodel.DetailViewModel
import com.augie.githubuser.viewmodel.DetailViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get username from main activity and get detail user from API then set it to the view
        val userName = intent.getStringExtra(EXTRA_USERNAME)
        showLoading(true)
        supportActionBar?.title = userName
        binding.tvUsername.text = userName

        // view model setup
        val detailRepo = DetailUserRepository()
        val detailFactory = DetailViewModelFactory(detailRepo)
        detailViewModel = ViewModelProvider(this, detailFactory).get(DetailViewModel::class.java)
        loadData(userName!!)

        detailViewModel.getUserDetail().observe(this, { detailUser ->
            if (detailUser != null) {
                binding.tvNameDetail.text = detailUser.name
                binding.tvLocation.text = detailUser.location
                binding.tvCompany.text = detailUser.company
                binding.tvEmail.text = detailUser.email
                binding.tvFollowingQty.text = detailUser.following
                binding.tvFollowerQty.text = detailUser.follower
                binding.tvUsername.text = userName
                binding.tvRepositoryQty.text = detailUser.repository
                Glide.with(this@DetailActivity)
                    .load(detailUser.photo)
                    .into(binding.civProfileDetail)
                showLoading(false)
            }

        })

        // section pager and tab layout setup
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = userName
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun loadData(userName: String) {
        detailViewModel.setUserDetail(userName, getString(R.string.no_info))
        showLoading(true)
        Log.d("DetailActivity", "setUserDetail")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            with(binding) {
                progressBarDetail.visibility = View.VISIBLE
                group.visibility = View.GONE
            }
        } else {
            with(binding) {
                progressBarDetail.visibility = View.GONE
                group.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
        )
    }
}