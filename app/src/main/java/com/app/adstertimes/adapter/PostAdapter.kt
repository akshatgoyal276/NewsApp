package com.app.adstertimes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.adstertimes.data.dataModals.Post
import com.app.adstertimes.utils.extensionFunctions.log
import com.app.adtertimes.databinding.ItemPostAdBinding
import com.app.adtertimes.databinding.ItemPostBinding
import javax.inject.Inject

class PostAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableSetOf<Post>()

    var onClick: (Post) -> Unit = { }
    var loadAd: ((ItemPostAdBinding) -> Unit)? = null

    fun updateList(newList: List<Post>?) {
        if (newList.isNullOrEmpty()) {
            val size = list.size+list.size/3
            list.clear()
            notifyItemRangeRemoved(0,size)
        }
        else {
            val index = list.size+list.size/3
            list.addAll(newList)
            notifyItemRangeInserted(index,newList.size+newList.size/3+1)
        }
    }

    inner class PostAdapterViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.apply {
                item.media?.firstOrNull()?.let {
                    imageView.load("https://www.nytimes.com/${it.url}")
                }
                headline.text = item.headline?.main
                snippet.text = item.snippet
                time.text = item.getHrsPassed()
                author.text = item.source
                binding.root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }

    inner class PostAdapterAdViewHolder(val binding:ItemPostAdBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(){
            loadAd?.let { it(binding) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> {
                val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return PostAdapterViewHolder(binding)
            }
            else -> {
                val binding = ItemPostAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return PostAdapterAdViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size+(list.size/3)
    }

    override fun getItemViewType(position: Int): Int {
        return if((position+1)%4==0) 2 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewType = getItemViewType(position)
        "onbind $position $viewType".log()
        when(viewType){
            1 -> {
                val article = list.toList()[position-position/4]
                (holder as PostAdapterViewHolder).bind(article)
            }
            2 -> {
                (holder as PostAdapterAdViewHolder).bind()
            }
        }
    }
}