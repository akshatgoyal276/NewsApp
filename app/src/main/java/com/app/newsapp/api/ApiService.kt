package com.app.newsapp.api

import com.app.newsapp.data.dataModals.Post
import com.app.newsapp.data.responseModals.BasicResponseModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("svc/search/v2/articlesearch.json")
    fun getPosts(
        @Query("page") page: String
    ): Call<BasicResponseModal<Post>>
}

fun <T> Call<T>.enqueue(onFailure: () -> Unit = { }, onSuccess: (T) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let {
                onSuccess(it)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure()
        }
    })
}