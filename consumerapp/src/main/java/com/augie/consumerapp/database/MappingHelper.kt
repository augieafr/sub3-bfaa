package com.augie.consumerapp.database

import android.database.Cursor
import com.augie.consumerapp.entity.FavoriteEntity

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteEntity> {
        val favoriteList = ArrayList<FavoriteEntity>()

        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(FavoriteEntity.USERNAME))
                val photo = getString(getColumnIndexOrThrow(FavoriteEntity.PHOTO))
                favoriteList.add(FavoriteEntity(username, photo))
            }
        }
        return favoriteList
    }
}