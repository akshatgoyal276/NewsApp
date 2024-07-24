package com.app.adstertimes.data.dataModals

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Book(
    @SerializedName(value = "title") val title: String = "",
    @SerializedName(value = "description") val description: String = "",
    @SerializedName(value = "author") val author: String? = "",
    @SerializedName(value = "book_image") val bookImage: String? = "",
    @SerializedName(value = "amazon_product_url") val productUrl: String? = "",
)
