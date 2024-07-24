package com.app.adstertimes.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.adstertimes.data.dataModals.Post
import com.app.adstertimes.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: ApiRepository

    private var page = 0

    private val _getInProgress = MutableLiveData(false)
    val getInProgress = _getInProgress

    private val _list = MutableLiveData<List<Post>>()
    val list: LiveData<List<Post>> = _list

    private fun updateList(newList: List<Post>) {
        _list.value = newList
        _getInProgress.postValue(false)
    }

    fun getPosts() {
        if (_getInProgress.value?.not()==true) {
            _getInProgress.postValue(true)
            repository.getPosts(page) { list ->
                if (!list.isNullOrEmpty()) {
                    updateList(list.filter { it -> !it.media?.firstOrNull()?.url.isNullOrEmpty() })
                    page += 1
                } else {
                    if(page == 0) getPosts()
                    _getInProgress.postValue(false)
                }
            }
        }
    }
}