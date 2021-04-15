package com.augie.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.augie.githubuser.R
import com.augie.githubuser.model.UserModel
import com.augie.githubuser.databinding.UserItemsBinding
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val mData = ArrayList<UserModel>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserModel>?) {
        if (items != null) {
            mData.clear()
            mData.addAll(items)
        } else mData.clear()
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)

        fun bind(userItems: UserModel) {
            binding.apply {
                tvUsername.text = userItems.name
                Glide.with(itemView.context)
                    .load(userItems.photo)
                    .into(civProfile)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(userItems) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }
}