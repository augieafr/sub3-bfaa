package com.augie.githubuser.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.augie.githubuser.BuildConfig
import com.augie.githubuser.R
import com.augie.githubuser.model.DetailUserModel
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailUserRepository {

    fun getUserDetail(username: String?, detailUser: MutableLiveData<DetailUserModel>) {
        val client = AsyncHttpClient()
        val user = DetailUserModel()
        val url = "https://api.github.com/users/$username"
        val token = BuildConfig.GITHUB_TOKEN
        val check = {text: String -> if (text != "null") text else "No info"}
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

}
