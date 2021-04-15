package com.augie.githubuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.augie.githubuser.model.UserModel
import com.augie.githubuser.repository.MainUserRepository
import java.lang.IllegalArgumentException

class MainViewModel(private val mainRepo: MainUserRepository) : ViewModel() {
    val listSearchUser = MutableLiveData<ArrayList<UserModel>>()


    fun setSearchUser(username: String?) {
        mainRepo.getSearchUser(username, listSearchUser)
    }

    fun getSearchUser(): MutableLiveData<ArrayList<UserModel>> {
        return listSearchUser
    }
}

class MainViewModelFactory(private val mainRepo: MainUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepo) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}