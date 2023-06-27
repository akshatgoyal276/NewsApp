package com.app.newsapp.repository

import com.app.newsapp.api.ApiService
import com.app.newsapp.api.enqueue
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
}