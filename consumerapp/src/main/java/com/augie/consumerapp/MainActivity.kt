package com.augie.consumerapp

import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.augie.consumerapp.adapter.FavoriteAdapter
import com.augie.consumerapp.database.MappingHelper
import com.augie.consumerapp.databinding.ActivityMainBinding
import com.augie.consumerapp.entity.FavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FavoriteAdapter()
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        loadAsync()
    }

    private fun loadAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            binding.progressBar.visibility = View.GONE
            val favorite = deferredFavorite.await()

            if (favorite.size > 0){
                adapter.setData(favorite)
                binding.tvNoFavorite.visibility = View.GONE
            } else {
                binding.tvNoFavorite.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val AUTHORITY = "com.augie.githubuser"
        private const val SCHEME = "content"

        val CONTENT_URI: Uri = Uri.Builder().scheme((SCHEME))
            .authority(AUTHORITY)
            .appendPath(FavoriteEntity.TABLE_NAME)
            .build()
    }
}