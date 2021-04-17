package com.augie.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.augie.githubuser.dao.FavoriteDao
import com.augie.githubuser.database.FavoriteDatabase
import com.augie.githubuser.database.FavoriteDatabase.Companion.AUTHORITY
import com.augie.githubuser.entity.FavoriteEntity.Companion.TABLE_NAME

class FavoriteProvider: ContentProvider() {
    override fun onCreate(): Boolean {
        favoriteDao = FavoriteDatabase.getInstance(context!!).favoriteDao
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)){
            FAVORITE -> favoriteDao.getAllFavoriteCursor()
            FAVORITE_ID -> favoriteDao.getFavoriteCursorById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteDao: FavoriteDao

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
        }
    }
}