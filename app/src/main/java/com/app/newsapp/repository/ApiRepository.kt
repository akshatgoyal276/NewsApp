package com.app.newsapp.repository

import com.app.newsapp.api.ApiService
import com.app.newsapp.api.enqueue
import com.app.newsapp.data.dataModals.Book
import com.app.newsapp.data.dataModals.Post
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