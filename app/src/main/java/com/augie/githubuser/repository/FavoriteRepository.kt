package com.augie.githubuser.repository

import com.augie.githubuser.dao.FavoriteDao
import com.augie.githubuser.entity.FavoriteEntity

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    //val favoriteCursor = favoriteDao.getAllFavoriteCursor()

    suspend fun getFavorite(): List<FavoriteEntity> {
        return favoriteDao.getAllFavoriteList()
    }
}