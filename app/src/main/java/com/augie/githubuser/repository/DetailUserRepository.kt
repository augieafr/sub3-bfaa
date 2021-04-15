package com.augie.githubuser.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.augie.githubuser.BuildConfig
import com.augie.githubuser.model.DetailUserModel
import com.augie.githubuser.model.RepositoryModel
import com.augie.githubuser.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class DetailUserRepository {

    fun getUserDetail(
        username: String?,
        detailUser: MutableLiveData<DetailUserModel>,
        noInfo: String
    ) {
        val client = AsyncHttpClient()
        val user = DetailUserModel()
        val url = "https://api.github.com/users/$username"
        val token = BuildConfig.GITHUB_TOKEN
        val check = { text: String -> if (text != "null") text else noInfo }
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
                    user.name = if (responseObject.getString("name") != "null")
                        responseObject.getString("name") else responseObject.getString("login")
                    user.email = check(responseObject.getString("email"))
                    user.location = check(responseObject.getString("location"))
                    user.company = check(responseObject.getString("company"))
                    user.follower = responseObject.getString("followers")
                    user.following = responseObject.getString("following")
                    user.repository = responseObject.getString("public_repos")
                    user.photo = responseObject.getString("avatar_url")
                    detailUser.postValue(user)
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

    fun getUserFollow(
        username: String?,
        type: String,
        listUser: MutableLiveData<ArrayList<UserModel>>
    ) {
        val listItem = ArrayList<UserModel>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/$type"
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
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val user = responseArray.getJSONObject(i)
                        val follower = UserModel()
                        follower.name = user.getString("login")
                        follower.photo = user.getString("avatar_url")
                        listItem.add(follower)
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

    fun getUserRepository(
        username: String?,
        listRepository: MutableLiveData<ArrayList<RepositoryModel>>
    ) {
        val listItem = ArrayList<RepositoryModel>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/repos"
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
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val repo = RepositoryModel()
                        repo.description = item.getString("description")
                        repo.name = item.getString("name")
                        repo.url = item.getString("html_url")
                        listItem.add(repo)
                    }
                    listRepository.postValue(listItem)
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
