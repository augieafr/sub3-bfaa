package com.augie.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.augie.githubuser.DetailActivity
import com.augie.githubuser.adapter.UserAdapter
import com.augie.githubuser.model.UserModel
import com.augie.githubuser.databinding.FragmentFollowerBinding
import com.augie.githubuser.adapter.UserAdapter.OnItemClickCallback
import com.augie.githubuser.viewmodel.UserViewModel

class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private lateinit var followerViewModel: UserViewModel

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
        @JvmStatic
        fun newInstance(username: String?) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USERNAME, username)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get data from sectionpager
        val username = arguments?.getString(EXTRA_USERNAME)

        //set recycler view
        adapter = UserAdapter()
        binding.rvFollower.layoutManager = LinearLayoutManager(context)
        binding.rvFollower.adapter = adapter

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        showLoading(true)
        followerViewModel.setFollow(username, "followers")
        followerViewModel.getUser().observe(viewLifecycleOwner, {listUser ->
            showLoading(false)
            adapter.setData(listUser)
        })

        // recyclerview on click callback
        adapter.setOnItemClickCallback(object : OnItemClickCallback{
            override fun onItemClicked(data: UserModel) {
                val mIntent = Intent(view.context, DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.name)
                startActivity(mIntent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBarFollower.visibility = View.VISIBLE
        } else {
            binding.progressBarFollower.visibility = View.GONE
        }
    }

}