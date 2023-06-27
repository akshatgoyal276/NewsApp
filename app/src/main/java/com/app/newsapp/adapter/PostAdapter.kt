package com.app.newsapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.newsapp.data.dataModals.Post
import com.app.newsapp.databinding.PostItemBinding
import javax.inject.Inject

class PostAdapter @Inject constructor() : RecyclerView.Adapter<PostAdapter.PostBindViewHolder>(){

    private var list = setOf<Post>()

    var onClick:(Post) -> Unit = { }

    fun setAdapterList(newList:List<Post>){
        list = newList.toSet()
        notifyItemRangeChanged(0,list.size)
    }

    override fun onViewAttachedToWindow(holder: PostBindViewHolder) {
        super.onViewAttachedToWindow(holder)
    }

    inner class PostBindViewHolder(private val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Post){
            binding.apply {
                item.media?.firstOrNull()?.let {
                    imageView.load("https://www.nytimes.com/${it.url}")
                    Log.d("TAG", "bind: https://www.nytimes.com/${it.url} ")
                }
                headline.text = item.headline?.main
                snippet.text = item.snippet
                time.text = item.getDate()
                binding.root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBindViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostBindViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PostBindViewHolder, position: Int) {
        holder.bind(list.toList()[position])
    }
}