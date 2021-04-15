package com.augie.githubuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.augie.githubuser.model.DetailUserModel
import com.augie.githubuser.model.RepositoryModel
import com.augie.githubuser.model.UserModel
import com.augie.githubuser.repository.DetailUserRepository

class DetailViewModel(private val detailRepo: DetailUserRepository) : ViewModel() {
    private val detailUser = MutableLiveData<DetailUserModel>()
    private val listFollowing = MutableLiveData<ArrayList<UserModel>>()
    private val listFollower = MutableLiveData<ArrayList<UserModel>>()
    private val listRepository = MutableLiveData<ArrayList<RepositoryModel>>()

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