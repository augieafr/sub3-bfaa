package com.augie.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.augie.githubuser.adapter.RepositoryAdapter
import com.augie.githubuser.databinding.FragmentRepositoryBinding
import com.augie.githubuser.viewmodel.DetailViewModel

class RepositoryFragment : Fragment() {

    private var _binding: FragmentRepositoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RepositoryAdapter
    private lateinit var repoViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get data from sectionpager
        val username = arguments?.getString(EXTRA_USERNAME)

        // set recycler view
        adapter = RepositoryAdapter()
        binding.rvRepository.layoutManager = LinearLayoutManager(context)
        binding.rvRepository.adapter = adapter

        repoViewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
        showLoading(true)
        repoViewModel.setUserRepository(username)
        repoViewModel.getUserRepository().observe(viewLifecycleOwner, { listUser ->
            showLoading(false)
            adapter.setData(listUser)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarRepository.visibility = View.VISIBLE
        } else {
            binding.progressBarRepository.visibility = View.GONE
        }
    }


    companion object {
        private const val EXTRA_USERNAME = "extra_username"

        @JvmStatic
        fun newInstance(username: String?) =
            RepositoryFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USERNAME, username)
                }
            }
    }
}