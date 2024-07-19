package com.app.newsapp.utils.extensionFunctions

import android.os.Build
import android.text.Html
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.text.BidiFormatter
import androidx.fragment.app.Fragment
import com.app.newsapp.main.applicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getString(id: Int): String {
    return applicationContext.resources.getString(id)
}

fun Fragment.getBiDirectionalString(@StringRes id: Int, arg1: String): String {
    return getString(id, getBidiFormattedString(arg1))
}

fun Fragment.getBiDirectionalString(@StringRes id: Int, arg1: String, arg2: String): String {
    return getString(id, getBidiFormattedString(arg1), getBidiFormattedString(arg2))
}

fun getBidiFormattedString(str: String): String {
    return BidiFormatter.getInstance().unicodeWrap(str)
}

fun String.log(prefix: String = "") {
    Log.d("akg", "$prefix $this")
}

fun String.getDateFromFormat(pattern: String): Date? {
    return SimpleDateFormat(pattern, Locale.ENGLISH).parse(this)
}

fun String.parseHtml():CharSequence{
    val text = this.removeRange(this.indexOf("<style>")..this.indexOf("</style>")+7)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH).trim()
    } else {
        Html.fromHtml(text).trim()
    }
}

fun String.deSpacify():String{
    return this.trim().replace("\\s+".toRegex()," ")
}

fun JSONObject.getJsonRequestBody(): RequestBody {
    return this.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
}

fun Any.getJsonRequestBody(): RequestBody {
    return this.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
}