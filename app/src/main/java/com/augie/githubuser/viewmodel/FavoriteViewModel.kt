package com.augie.githubuser.viewmodel

import androidx.lifecycle.*
import com.augie.githubuser.repository.FavoriteRepository
import java.lang.IllegalArgumentException

class FavoriteViewModel(repository: FavoriteRepository) : ViewModel() {

    val listFavorite = repository.allFavorite.asLiveData()

}

class FavoriteViewModelFactory(private val repository: FavoriteRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(repository) as  T
        }
        throw IllegalArgumentException("Unkown ViewModel class")
    }

}