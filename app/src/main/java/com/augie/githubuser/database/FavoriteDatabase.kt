package com.augie.githubuser.database

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.augie.githubuser.dao.FavoriteDao
import com.augie.githubuser.entity.FavoriteEntity
import com.augie.githubuser.entity.FavoriteEntity.Companion.TABLE_NAME

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract val favoriteDao: FavoriteDao

    companion object {
        const val AUTHORITY = "com.augie.githubuser"
        const val SCHEME = "content"

        val CONTENT_URI: Uri = Uri.Builder().scheme((SCHEME))
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        @Volatile
        private var INSTANCE: FavoriteDatabase? = null
        fun getInstance(context: Context): FavoriteDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteDatabase::class.java,
                        "favorite_database"
                    ).build()
                }
                return instance
            }
        }
    }
}