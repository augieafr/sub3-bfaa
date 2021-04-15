package com.augie.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.augie.githubuser.BuildConfig
import com.augie.githubuser.model.DetailUserModel
import com.augie.githubuser.model.RepositoryModel
import com.augie.githubuser.model.UserModel
import com.augie.githubuser.repository.DetailUserRepository
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.IllegalArgumentException

class DetailViewModel(private val detailRepo: DetailUserRepository) : ViewModel() {
    val detailUser = MutableLiveData<DetailUserModel>()
    val listFollowing = MutableLiveData<ArrayList<UserModel>>()
    val listFollower = MutableLiveData<ArrayList<UserModel>>()
    val listRepository = MutableLiveData<ArrayList<RepositoryModel>>()

    // setter
    fun setUserRepository(username: String?) {
        detailRepo.getUserRepository(username, listRepository)
    }

    fun setUserFollowing(username: String?) {
        detailRepo.getUserFollow(username, "following", listFollowing)
    }

    fun setUserFollower(username: String?) {
        detailRepo.getUserFollow(username, "followers", listFollower)
    }

    fun setUserDetail(username: String?, info: String) {
        detailRepo.getUserDetail(username, detailUser, info)
    }


    // getter
    fun getUserFollowing(): MutableLiveData<ArrayList<UserModel>> {
        return listFollowing
    }

    fun getUserFollower(): MutableLiveData<ArrayList<UserModel>> {
        return listFollower
    }

    fun getUserDetail(): MutableLiveData<DetailUserModel> {
        return detailUser
    }

    fun getUserRepository(): MutableLiveData<ArrayList<RepositoryModel>> {
        return listRepository
    }

}

class DetailViewModelFactory(private val detailRepo: DetailUserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(detailRepo) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }

}