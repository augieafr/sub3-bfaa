package com.augie.githubuser.dao

import android.database.Cursor
import androidx.room.*
import com.augie.githubuser.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_table")
    fun getAllFavoriteList(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_table")
    fun getAllFavoriteCursor(): Cursor

    @Query("SELECT * FROM favorite_table WHERE favorite_table.user_name = :username")
    fun getFavoriteCursorById(username: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity): Long

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity): Int

    @Query("SELECT COUNT() FROM favorite_table WHERE user_name = :userName")
    fun countFavorite(userName: String): Int

}