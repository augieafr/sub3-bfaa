package com.augie.githubuser.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.augie.githubuser.entity.FavoriteEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class FavoriteEntity(
    @ColumnInfo(name = USERNAME)
    @PrimaryKey var userName: String ,

    var photo: String
) {
    companion object {
        const val TABLE_NAME = "favorite_table"
        const val USERNAME = "user_name"
        const val PHOTO = "photo"
    }
}
