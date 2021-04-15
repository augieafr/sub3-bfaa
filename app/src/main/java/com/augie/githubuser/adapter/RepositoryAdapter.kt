package com.augie.githubuser.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.augie.githubuser.R
import com.augie.githubuser.model.RepositoryModel
import com.augie.githubuser.databinding.RepositoryItemsBinding

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private val mData = ArrayList<RepositoryModel>()

    fun setData(items: ArrayList<RepositoryModel>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RepositoryItemsBinding.bind(itemView)

        fun bind(repoItems: RepositoryModel) {
            with(binding) {
                tvDescription.text = repoItems.description
                tvUsername.text = repoItems.name

                itemView.setOnClickListener {
                    val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(repoItems.url))
                    itemView.context.startActivity(intentBrowser)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_items, parent, false)
        return RepositoryViewHolder(mView)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}