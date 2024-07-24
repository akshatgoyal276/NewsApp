package com.app.adstertimes.data.dataModals

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar

@JsonClass(generateAdapter = true)
data class Post(
    @SerializedName(value = "_id") val id: String = "",
    @SerializedName(value = "pub_date") val publishedDate: String = "",
    @SerializedName(value = "snippet") val snippet: String? = "",
    @SerializedName(value = "source") val source: String? = "",
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

    fun getHrsPassed():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        val date = sdf.parse(publishedDate.slice(0..18))
        val cal = Calendar.getInstance()
        cal.time = date
        val timePassed = Calendar.getInstance().timeInMillis - cal.timeInMillis
        val mins = timePassed/(1000*60)
        val hrs = timePassed/(1000*60*60)
        return if(mins>60){
            if(hrs>24) "${hrs/24}d"
            else "${if (hrs==0L) 1 else hrs}hr"
        } else "${if (mins==0L) 1 else mins}m"
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