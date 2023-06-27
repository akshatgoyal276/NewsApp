package com.app.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.newsapp.adapter.PostAdapter
import com.app.newsapp.data.dataModals.Post
import com.app.newsapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            adapter.onClick = { navigateToPostDetailsFragment(it) }
            postRecyclerView.adapter = adapter
            postRecyclerView.addOnScrollListener(ScrollListener())
        }

        viewModel.getPosts()
        viewModel.list.observe(viewLifecycleOwner) {
            adapter.setAdapterList(it.toList())
        }

        return binding.root
    }

    private fun navigateToPostDetailsFragment(post: Post) {
        findNavController().navigate(HomeFragmentDirections.actionNavHomeToPostDetailsFragment(post))
    }

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(rv, dx, dy)

            val layoutManager = rv.layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            if (lastVisibleItemPosition == totalItemCount - 1) viewModel.getPosts()

        }
    }

}