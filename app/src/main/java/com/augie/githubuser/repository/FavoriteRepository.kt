package com.augie.githubuser.repository

import com.augie.githubuser.dao.FavoriteDao

class FavoriteRepository(favoriteDao: FavoriteDao) {

    val allFavorite = favoriteDao.getAllFavoriteList()
}