package com.app.adstertimes.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.adstertimes.data.dataModals.Book
import com.app.adstertimes.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: ApiRepository

    private var page = 0

    private val _getInProgress = MutableLiveData(false)
    val getInProgress = _getInProgress

    private val _list = MutableLiveData<List<Book>>()
    val list: LiveData<List<Book>> = _list

    private fun updateList(newList: List<Book>) {
        _list.value = newList
        _getInProgress.postValue(false)
    }

    fun getBooks() {
        if (_getInProgress.value?.not()==true) {
            _getInProgress.postValue(true)
            repository.getBooks() { list ->
                if (!list.isNullOrEmpty()) {
                    updateList(list)
                    page += 1
                } else {
                    if(page == 0) getBooks()
                    _getInProgress.postValue(false)
                }
            }
        }
    }
}