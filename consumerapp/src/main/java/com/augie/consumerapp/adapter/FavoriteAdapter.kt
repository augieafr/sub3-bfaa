package com.augie.consumerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.augie.consumerapp.databinding.FavoriteItemsBinding
import com.augie.consumerapp.entity.FavoriteEntity
import com.bumptech.glide.Glide

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val mData = ArrayList<FavoriteEntity>()

    fun setData(items: List<FavoriteEntity>?){
        if (items != null) {
            mData.clear()
            mData.addAll(items)
        } else mData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val binding =
            FavoriteItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class FavoriteViewHolder(private val binding: FavoriteItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteItems: FavoriteEntity) {
            with(binding) {
                tvUsername.text = favoriteItems.userName
                Glide.with(itemView.context)
                    .load(favoriteItems.photo)
                    .into(civProfile)
            }
        }
    }

}