package com.app.newsapp.data.dataModals

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
    @SerializedName(value = "_id") val id: String = "",
    @SerializedName(value = "pub_date") val publishedDate: String = "",
    @SerializedName(value = "abstract") val abstract: String = "",
    @SerializedName(value = "web_url") var webUrl: String = "",
    @SerializedName(value = "headline") var headline: Headline? = null,
    @SerializedName(value = "multimedia") var media: List<Multimedia>? = null,
    @SerializedName(value = "lead_paragraph") var paragraph: String = "",
)


@JsonClass(generateAdapter = true)
data class Headline(
    @SerializedName(value = "main") val main: String = "",
    @SerializedName(value = "print_headline") val printHeadline: String = "",
)

@JsonClass(generateAdapter = true)
data class Multimedia(
    @SerializedName(value = "type") val type: String = "",
    @SerializedName(value = "url") val url: String = "",
    @SerializedName(value = "width") val width: String = "",
    @SerializedName(value = "height") val height: String = "",
)