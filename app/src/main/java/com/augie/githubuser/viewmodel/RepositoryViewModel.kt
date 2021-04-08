package com.augie.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.augie.githubuser.model.RepositoryModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class RepositoryViewModel : ViewModel() {
    val listRepo = MutableLiveData<ArrayList<RepositoryModel>>()

    fun setRepo(username: String?){
        val listItem = ArrayList<RepositoryModel>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/repos"
        val token = "token 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"
        client.addHeader("Authorization", token)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()){
                        val item = responseArray.getJSONObject(i)
                        val repo = RepositoryModel()
                        repo.description = item.getString("description")
                        repo.name = item.getString("name")
                        repo.url = item.getString("html_url")
                        listItem.add(repo)
                    }
                    listRepo.postValue(listItem)
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getRepo(): MutableLiveData<ArrayList<RepositoryModel>>{
        return listRepo
    }
}