package com.augie.githubuser.viewmodel

import androidx.lifecycle.MutableLiveData
import com.augie.githubuser.model.DetailUserModel
import com.augie.githubuser.repository.DetailUserRepository

class DetailViewModel(private val detailRepo: DetailUserRepository) {
    val detailUser = MutableLiveData<DetailUserModel>()

    fun setUserDetal(username: String?){
        detailRepo.getUserDetail(username, detailUser)
    }

    fun getUserDetail(): MutableLiveData<DetailUserModel> {
       return detailUser
    }

}