package com.augie.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.augie.githubuser.model.UserModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class UserViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<UserModel>>()

    // function to set follower or following
    fun setFollow(username: String?, type: String){
        val listItem = ArrayList<UserModel>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/$type"
        val token = "token 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"
        client.addHeader("Authorization", token)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()){
                        val user = responseArray.getJSONObject(i)
                        val follower = UserModel()
                        follower.name = user.getString("login")
                        follower.photo = user.getString("avatar_url")
                        listItem.add(follower)
                    }
                    listUser.postValue(listItem)
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

    fun setUser(username: String?){
        val listItem = ArrayList<UserModel>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        val token = "token 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"
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
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()){
                        val user = list.getJSONObject(i)
                        val searchResult = UserModel()
                        searchResult.name = user.getString("login")
                        searchResult.photo = user.getString("avatar_url")
                        listItem.add(searchResult)
                    }
                    listUser.postValue(listItem)
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

    fun getUser(): MutableLiveData<ArrayList<UserModel>>{
        return listUser
    }
}