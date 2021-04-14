package com.augie.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.augie.githubuser.BuildConfig
import com.augie.githubuser.model.UserModel
import com.augie.githubuser.repository.MainUserRepository
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.IllegalArgumentException

class MainViewModel(private val mainRepo: MainUserRepository) : ViewModel() {
    val listUser = MutableLiveData<ArrayList<UserModel>>()

    // function to set follower or following
    fun setFollow(username: String?, type: String){
        val listItem = ArrayList<UserModel>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/$type"
        val token = BuildConfig.GITHUB_TOKEN
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
        mainRepo.getSearchUser(username, listUser)
    }

    fun getUser(): MutableLiveData<ArrayList<UserModel>>{
        return listUser
    }
}

class MainViewModelFactory(private val mainRepo: MainUserRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mainRepo) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}