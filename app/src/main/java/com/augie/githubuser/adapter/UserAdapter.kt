package com.augie.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.augie.githubuser.databinding.UserItemsBinding
import com.augie.githubuser.model.UserModel
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            UserItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(private val binding: UserItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userItems: UserModel) {
            binding.apply {
                tvUsername.text = userItems.name
                Glide.with(itemView.context)
                    .load(userItems.photo)
                    .into(civProfile)

                root.setOnClickListener { onItemClickCallback?.onItemClicked(userItems) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }
}