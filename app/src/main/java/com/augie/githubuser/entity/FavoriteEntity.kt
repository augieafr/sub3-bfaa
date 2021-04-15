package com.augie.githubuser.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class FavoriteEntity(
    @ColumnInfo(name = "user_name")
    @PrimaryKey var userName: String ,

    var photo: String?
)
