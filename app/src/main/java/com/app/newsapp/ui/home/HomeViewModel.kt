package com.app.newsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.newsapp.data.dataModals.Post
import com.app.newsapp.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: ApiRepository

    private var page = 0

    private var getInProgress = false

    private val _list = MutableLiveData<List<Post>>()
    val list: LiveData<List<Post>> = _list

    private fun updateList(list: List<Post>) {
        val oldList = _list.value ?: listOf()
        val newList = mutableListOf<Post>().apply {
            addAll(oldList)
            addAll(list)
        }
        _list.value = newList
        getInProgress = false
    }

    fun getPosts() {
        if (getInProgress.not()) {
            getInProgress = true
            repository.getPosts(page) { list ->
                if (!list.isNullOrEmpty()) {
                    updateList(list)
                    page += 1
                } else getInProgress = false
            }
        }
    }
}