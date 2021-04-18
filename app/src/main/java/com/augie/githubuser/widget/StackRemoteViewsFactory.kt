package com.augie.githubuser.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.augie.githubuser.R
import com.augie.githubuser.database.FavoriteDatabase
import com.augie.githubuser.database.MappingHelper
import com.bumptech.glide.Glide

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }


    override fun onDataSetChanged() {
        // get cursor directly from dao because it produce error when i use content resolver
        // + coroutine
        val dao = FavoriteDatabase.getInstance(context).favoriteDao
        val cursor = dao.getAllFavoriteCursor()
        val list = MappingHelper.mapCursorToArrayList(cursor)

        for (item in list) {
            val photo = Glide.with(context)
                .asBitmap()
                .load(item.photo)
                .submit(512, 512)
                .get()
            mWidgetItems.add(photo)
        }

    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}