package com.augie.githubuser.viewmodel

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.augie.githubuser.entity.FavoriteEntity
import kotlinx.coroutines.launch
import com.augie.githubuser.repository.FavoriteRepository

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    private val listFavorite = MutableLiveData<List<FavoriteEntity>>()

    fun setListFavorite() = viewModelScope.launch {
        val favorite = repository.getFavorite()
        listFavorite.postValue(favorite)
    }

    fun getListFavorite(): MutableLiveData<List<FavoriteEntity>> {
        return listFavorite
    }

}