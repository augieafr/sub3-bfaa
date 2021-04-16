package com.augie.githubuser

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.augie.githubuser.adapter.FavoriteAdapter
import com.augie.githubuser.database.FavoriteDatabase
import com.augie.githubuser.databinding.ActivityFavoriteBinding
import com.augie.githubuser.repository.FavoriteRepository
import com.augie.githubuser.viewmodel.FavoriteViewModel
import com.augie.githubuser.viewmodel.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.my_favorite)

        // recycler view setup
        adapter = FavoriteAdapter()
        binding.rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
        binding.rvFavorite.adapter = adapter

        // viewModel setup
        val favoriteDao = FavoriteDatabase.getInstance(application).favoriteDao
        val favoriteRepository = FavoriteRepository(favoriteDao)
        val favoriteFactory = FavoriteViewModelFactory(favoriteRepository)
        favoriteViewModel = ViewModelProvider(this, favoriteFactory).get(FavoriteViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        // i observe the live data in onResume because there is a case when a user go to detail
        // activity from this activity and delete the github user from favorite table.
        showLoading(true)
        favoriteViewModel.listFavorite.observe(this, {favoriteList ->
            if (favoriteList.isEmpty()){
                adapter.setData(null)
                binding.tvNoFavorite.visibility = View.VISIBLE
            } else {
                adapter.setData(favoriteList)
                binding.tvNoFavorite.visibility = View.GONE
            }
            showLoading(false)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFavorite.visibility = View.VISIBLE
        } else {
            binding.progressBarFavorite.visibility = View.GONE
        }
    }
}