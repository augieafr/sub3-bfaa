package com.augie.githubuser.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.augie.githubuser.BuildConfig
import com.augie.githubuser.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainUserRepository {

    fun getSearchUser(username: String?, listUser: MutableLiveData<ArrayList<UserModel>>) {
        val listItem = ArrayList<UserModel>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        val token = BuildConfig.GITHUB_TOKEN
        client.addHeader("Authorization", token)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val searchResult = UserModel()
                        searchResult.userName = user.getString("login")
                        searchResult.photo = user.getString("avatar_url")
                        listItem.add(searchResult)
                    }
                    listUser.postValue(listItem)
                } catch (e: Exception) {
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
}