package com.app.newsapp.data.dataModals

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.text.SimpleDateFormat

@JsonClass(generateAdapter = true)
data class Post(
    @SerializedName(value = "_id") val id: String = "",
    @SerializedName(value = "pub_date") val publishedDate: String = "",
    @SerializedName(value = "snippet") val snippet: String? = "",
    @SerializedName(value = "web_url") var webUrl: String = "",
    @SerializedName(value = "headline") var headline: Headline? = null,
    @SerializedName(value = "multimedia") var media: List<Multimedia>? = null,
    @SerializedName(value = "lead_paragraph") var paragraph: String = "",
) :Serializable {
    fun getDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(publishedDate.slice(0..9))
        return SimpleDateFormat("dd LLLL").format(date)
    }
}


@JsonClass(generateAdapter = true)
data class Headline(
    @SerializedName(value = "main") val main: String = "",
)

@JsonClass(generateAdapter = true)
data class Multimedia(
    @SerializedName(value = "type") val type: String = "",
    @SerializedName(value = "url") val url: String = "",
    @SerializedName(value = "width") val width: String = "",
    @SerializedName(value = "height") val height: String = "",
)