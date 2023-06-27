package com.app.newsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.newsapp.api.enqueue
import com.app.newsapp.data.dataModals.Post
import com.app.newsapp.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: ApiRepository

    private val _list = MutableLiveData<List<Post>>()
    val list: LiveData<List<Post>> = _list

    private fun updateList(list:List<Post>){
        val oldList = _list.value?: listOf()
        val newList = mutableListOf<Post>().apply {
            addAll(oldList)
            addAll(list)
        }
        _list.value = newList
    }

    fun getPosts(page:Int){
        repository.getPosts(page) {
            updateList(it)
        }
    }
}