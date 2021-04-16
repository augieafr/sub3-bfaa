package com.augie.githubuser.repository

import com.augie.githubuser.dao.FavoriteDao
import com.augie.githubuser.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    val allFavorite = favoriteDao.getAllFavoriteList()
}