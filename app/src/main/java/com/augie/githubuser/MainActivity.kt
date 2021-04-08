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
import com.augie.githubuser.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResult.adapter = adapter

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        userViewModel.getUser().observe(this, { listUser ->
            if (listUser != null){
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
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            // show user search result
            override fun onQueryTextSubmit(query: String?): Boolean {
                showLoading(true)
                userViewModel.setUser(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener{
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
        return if (item.itemId == R.id.menu_language){
            // intent to language setting
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        } else true
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
    private fun setPluralTextResult(number: Int){
        val pluralText = resources.getQuantityString(R.plurals.numberOfSearchResult, number, number)
        binding.tvResult.text = pluralText
    }

}