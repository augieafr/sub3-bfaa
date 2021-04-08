package com.augie.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import com.augie.githubuser.adapter.SectionsPagerAdapter
import com.augie.githubuser.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get username from main activity and get detail user from API then set it to the view
        val userName = intent.getStringExtra(EXTRA_USERNAME)
        showLoading(true)
        supportActionBar?.title = userName
        binding.tvUsername.text = userName
        setDetailUser(userName)

        // section pager and tab layout setup
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = userName
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    // get data from API and set it to the view
    private fun setDetailUser(query: String?){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$query"
        val token = "token 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"
        val check = {text: String -> if (text != "null") text else getString(R.string.no_info)}
        client.addHeader("Authorization", token)
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    with(binding){
                        tvNameDetail.text = if (responseObject.getString("name") != "null")
                            responseObject.getString("name") else responseObject.getString("login")
                        tvEmail.text = check(responseObject.getString("email"))
                        tvLocation.text = check(responseObject.getString("location"))
                        tvCompany.text = check(responseObject.getString("company"))
                        tvFollowerQty.text = responseObject.getString("followers")
                        tvFollowingQty.text = responseObject.getString("following")
                        tvRepositoryQty.text = responseObject.getString("public_repos")
                        Glide.with(this@DetailActivity)
                            .load(responseObject.getString("avatar_url"))
                            .into(civProfileDetail)
                    }
                    showLoading(false)
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    private fun showLoading(state: Boolean){
        if (state){
            with(binding){
                progressBarDetail.visibility = View.VISIBLE
                group.visibility = View.GONE
            }
        } else{
            with(binding){
                progressBarDetail.visibility = View.GONE
                group.visibility = View.VISIBLE
            }
        }
    }
}