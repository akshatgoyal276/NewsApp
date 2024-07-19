package com.app.newsapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.newsapp.data.dataModals.Book
import com.app.newsapp.databinding.ItemBookBinding
import com.app.newsapp.databinding.ItemPostAdBinding
import com.app.newsapp.utils.extensionFunctions.log
import com.app.newsapp.utils.extensionFunctions.setHeightAndWidth
import javax.inject.Inject

class BookAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var parentWidth = 0
    private val list = mutableSetOf<Book>()

    var onClick: (Book) -> Unit = { }
    var loadAd: ((ItemPostAdBinding) -> Unit)? = null

    fun updateList(newList: List<Book>?) {
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

    inner class BookAdapterViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Book) {
            binding.apply {
                imageView.setHeightAndWidth(width = parentWidth*.4)
                imageView.load(item.bookImage)
                headline.text = item.title
                snippet.text = item.description
//                time.text = item.getHrsPassed()
                author.text = item.author
                binding.root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }

    inner class PostAdapterAdViewHolder(val binding: ItemPostAdBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(){
            loadAd?.let { it(binding) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        parentWidth = parent.measuredWidth
        when (viewType) {
            1 -> {
                val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return BookAdapterViewHolder(binding)
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
                (holder as BookAdapterViewHolder).bind(article)
            }
            2 -> {
                (holder as PostAdapterAdViewHolder).bind()
            }
        }
    }
}