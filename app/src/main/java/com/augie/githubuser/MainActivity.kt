package com.augie.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.augie.githubuser.adapter.UserAdapter
import com.augie.githubuser.model.UserModel
import com.augie.githubuser.databinding.ActivityMainBinding
import com.augie.githubuser.repository.MainUserRepository
import com.augie.githubuser.viewmodel.MainViewModel
import com.augie.githubuser.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // recycler view setup
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResult.adapter = adapter

        // view model setup
        val mainRepo = MainUserRepository()
        val mainFactory = MainViewModelFactory(mainRepo)
        mainViewModel = ViewModelProvider(this, mainFactory).get(MainViewModel::class.java)
        mainViewModel.getSearchUser().observe(this, { listUser ->
            if (listUser != null) {
                adapter.setData(listUser)
                showLoading(false)
                setPluralTextResult(listUser.size)
            }
        })

        // recyclerview on click callback
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                // intent to detail activity
                val mIntent = Intent(this@MainActivity, DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.name)
                startActivity(mIntent)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // show user search result
            override fun onQueryTextSubmit(query: String?): Boolean {
                showLoading(true)
                mainViewModel.setSearchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {

            }

            // return activity main's view to default when user go back from search view
            override fun onViewDetachedFromWindow(v: View?) {
                adapter.setData(null)
                binding.tvResult.visibility = View.GONE
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_setting ->{
                // intent to language setting
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                true
            }

            R.id.menu_favorite ->{
                val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> false
        }
    }

    // function to show or not progress bar and text view result
    private fun showLoading(state: Boolean) {
        if (state) {
            adapter.setData(null)
            binding.tvResult.visibility = View.GONE
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.tvResult.visibility = View.VISIBLE
            binding.progressBarMain.visibility = View.GONE
        }
    }

    // function to set text view result based on how many users found
    private fun setPluralTextResult(number: Int) {
        val pluralText = resources.getQuantityString(R.plurals.numberOfSearchResult, number, number)
        binding.tvResult.text = pluralText
    }

}