package com.app.newsapp.ui.postDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.app.newsapp.R
import com.app.newsapp.databinding.FragmentHomeBinding
import com.app.newsapp.databinding.FragmentPostDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding

    private lateinit var viewModel: PostDetailsViewModel

    private lateinit var args: PostDetailsFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[PostDetailsViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        args = PostDetailsFragmentArgs.fromBundle(requireArguments())
        binding.apply {
            val item = args.postItem
            item.media?.firstOrNull()?.let {
                imageView.load("https://www.nytimes.com/${it.url}")
            }
            headline.text = item.headline?.main
            snippet.text = item.snippet
            paragraph.text = item.paragraph
            time.text = item.getDate()
        }
        return binding.root
    }

}