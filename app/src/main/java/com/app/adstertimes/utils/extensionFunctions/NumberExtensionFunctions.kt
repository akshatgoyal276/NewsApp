package com.app.adstertimes.utils.extensionFunctions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.getDateAndTime(pattern: String): String {
    val date = Date()
    date.time = this.times(1000).toLong()
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(date)
}

fun Long.getDateAndTime(pattern: String): String {
    val date = Date()
    date.time = this.times(1000)
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(date)
}

fun Double.getDate(): Date {
    val date = Date()
    date.time = this.times(1000).toLong()
    return date
}

fun Float.prettyFormat(): String {
    val str = this.toString()
    return if (str.split(".").last() == "0") str.substring(
        0, str.length - 2
    ) else String.format("%.2f", this)
}