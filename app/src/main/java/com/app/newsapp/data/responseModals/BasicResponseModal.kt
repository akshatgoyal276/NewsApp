package com.app.newsapp.data.responseModals

data class BasicResponseModal<T>(
    val status: String? = null,
    val copyright: String? = null,
    val response: OutputResponseModal<T>,
)

data class OutputResponseModal<T>(
    val docs: List<T>,
)