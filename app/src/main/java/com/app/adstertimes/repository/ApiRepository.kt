package com.app.adstertimes.repository

import com.app.adstertimes.api.ApiService
import com.app.adstertimes.api.enqueue
import com.app.adstertimes.data.dataModals.Book
import com.app.adstertimes.data.dataModals.Post
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class ApiRepository @Inject constructor(
    private val apiService: ApiService
){
    fun getPosts(page:Int,onComplete:(List<Post>?)->Unit) = apiService.getPosts(page.toString()).enqueue {
        onComplete(it?.response?.docs)
    }

    fun getBooks(onComplete:(List<Book>?)->Unit) = apiService.getBooks().enqueue {
        val list = mutableListOf<Book>()
        it?.results?.lists?.forEach { l -> list.addAll(l.books) }
        onComplete(list)
    }
}