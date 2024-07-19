package com.app.newsapp.api

import android.util.Log
import com.app.newsapp.data.dataModals.Book
import com.app.newsapp.data.dataModals.Post
import com.app.newsapp.data.responseModals.BasicResponseModal
import com.app.newsapp.data.responseModals.BasicResultsModal
import com.app.newsapp.data.responseModals.BookList
import com.app.newsapp.data.responseModals.OverView
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


    @GET("svc/books/v3/lists/full-overview.json")
    fun getBooks(): Call<BasicResultsModal<OverView<BookList<Book>>>>
}

fun <T> Call<T>.enqueue(onSuccess: (T?) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    onSuccess(it)
                }
            } else onSuccess(null)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.d("API Failure", "error: $t")
            onSuccess(null)
        }
    })
}