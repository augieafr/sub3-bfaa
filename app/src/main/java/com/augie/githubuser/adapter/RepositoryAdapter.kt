package com.augie.githubuser.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.augie.githubuser.databinding.RepositoryItemsBinding
import com.augie.githubuser.model.RepositoryModel

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private val mData = ArrayList<RepositoryModel>()

    fun setData(items: ArrayList<RepositoryModel>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RepositoryViewHolder {
        val binding =
            RepositoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class RepositoryViewHolder(private val binding: RepositoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(repoItems: RepositoryModel) {
            with(binding) {
                tvDescription.text = repoItems.description
                tvUsername.text = repoItems.name

                root.setOnClickListener {
                    val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(repoItems.url))
                    it.context.startActivity(intentBrowser)
                }
            }
        }
    }
}