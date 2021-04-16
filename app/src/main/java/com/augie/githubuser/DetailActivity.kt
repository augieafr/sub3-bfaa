package com.augie.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.augie.githubuser.adapter.SectionsPagerAdapter
import com.augie.githubuser.database.FavoriteDatabase
import com.augie.githubuser.databinding.ActivityDetailBinding
import com.augie.githubuser.entity.FavoriteEntity
import com.augie.githubuser.repository.DetailUserRepository
import com.augie.githubuser.viewmodel.DetailViewModel
import com.augie.githubuser.viewmodel.DetailViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userPhoto: String
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get username from main activity and get detail user from API then set it to the view
        val userName = intent.getStringExtra(EXTRA_USERNAME)

        showLoading(true)
        supportActionBar?.title = userName
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tvUsername.text = userName

        // view model setup
        val favoriteDao = FavoriteDatabase.getInstance(application).favoriteDao
        val detailRepo = DetailUserRepository(favoriteDao)
        val detailFactory = DetailViewModelFactory(detailRepo)
        detailViewModel = ViewModelProvider(this, detailFactory).get(DetailViewModel::class.java)

        backgroundTask(userName!!)
        observe()

        // section pager and tab layout setup
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = userName
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fabFavorite.setOnClickListener {
            val favorite = FavoriteEntity(userName, userPhoto)
            if (isFavorite){
                detailViewModel.delete(favorite)
                isFavorite = false
                binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            } else {
                detailViewModel.insert(favorite)
                isFavorite = true
                binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }
    }

    // load data and check if user already in favorite table (for fab icon purpose)
    private fun backgroundTask(userName: String) {
        showLoading(true)
        // load data
        detailViewModel.setUserDetail(userName, getString(R.string.no_info))

        // check user
        lifecycleScope.launch(Dispatchers.Default) {
            val count = detailViewModel.count(userName)
            withContext(Dispatchers.Main) {
                if (count > 0){
                    isFavorite = true
                    binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    isFavorite = false
                    binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
        }
    }

    private fun observe() {
        detailViewModel.getUserDetail().observe(this, { detailUser ->
            if (detailUser != null) {
                binding.tvNameDetail.text = detailUser.name
                binding.tvLocation.text = detailUser.location
                binding.tvCompany.text = detailUser.company
                binding.tvEmail.text = detailUser.email
                binding.tvFollowingQty.text = detailUser.following
                binding.tvFollowerQty.text = detailUser.follower
                binding.tvUsername.text = detailUser.userName
                binding.tvRepositoryQty.text = detailUser.repository
                userPhoto = detailUser.photo
                Glide.with(this@DetailActivity)
                    .load(userPhoto)
                    .into(binding.civProfileDetail)
                showLoading(false)
            }
        })
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            with(binding) {
                progressBarDetail.visibility = View.VISIBLE
                fabFavorite.visibility = View.GONE
                group.visibility = View.GONE
            }
        } else {
            with(binding) {
                progressBarDetail.visibility = View.GONE
                fabFavorite.visibility = View.VISIBLE
                group.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
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