package com.augie.consumerapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.augie.consumerapp.entity.FavoriteEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class FavoriteEntity(
    @ColumnInfo(name = USERNAME)
    @PrimaryKey var userName: String ,

    @ColumnInfo(name = PHOTO)
    var photo: String
) {
    companion object {
        const val TABLE_NAME = "favorite_table"
        const val USERNAME = "user_name"
        const val PHOTO = "photo"
    }
}
