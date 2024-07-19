package com.app.newsapp.data.responseModals

import com.app.newsapp.data.dataModals.Book

data class BasicResponseModal<T>(
    val status: String? = null,
    val copyright: String? = null,
    val response: OutputResponseModal<T>,
)

data class OutputResponseModal<T>(
    val docs: List<T>,
)

data class BasicResultsModal<T>(
    val status: String? = null,
    val copyright: String? = null,
    val results: T,
)

data class OverView<T>(
    val lists : List<T>
)

data class BookList<T>(
    val books: List<T>
)