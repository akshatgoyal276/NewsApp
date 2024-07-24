package com.app.adstertimes.utils.extensionFunctions

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

fun CheckBox.check() {
    isChecked = true
}

fun CheckBox.uncheck() {
    isChecked = false
}

fun List<View>.gone() = forEach { it.gone() }

fun List<View>.show() = forEach { it.show() }

fun List<View>.hide() = forEach { it.hide() }

/**
 * conditional visibility of view
 * @param condition condition which must be satisfied for this view to be visible
 * @param hide if true, view will be hidden in case condition fails, else view will be set to gone
 */
fun View.showIf(condition: Boolean?, hide: Boolean = false) {
    if (condition == true) show()
    else {
        if (hide) hide()
        else gone()
    }
}

/**
 * conditional enability of view
 * @param condition condition which must be satisfied for this view to be visible
 */
fun View.enableIf(condition: Boolean?) {
    if (condition == true) enable()
    else disable()
}

/**
 * Checks the text if it's not empty or null, then shows the text view and sets the text
 * else it'll be gone
 * */
fun TextView.showIf(text: String?) {
    if (text.isNullOrEmpty()) gone()
    else {
        show()
        this.text = text
    }
}

fun ViewDataBinding.hide() {
    this.root.visibility = View.INVISIBLE
}

fun ViewDataBinding.gone() {
    this.root.visibility = View.GONE
}

fun ViewDataBinding.show() {
    this.root.visibility = View.VISIBLE
}


fun View.click(debounceTime: Long = 500L, action: () -> Unit) {
    val listener = object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }

    }
    setOnClickListener(listener)
}

fun List<View>.click(debounceTime: Long = 300L, action: () -> Unit) {
    forEach { it.click(debounceTime, action) }
}

inline fun LiveData<Nothing>.observe(
    lifecycleOwner: LifecycleOwner, crossinline function: () -> Unit
) {
    observe(lifecycleOwner, Observer { function() })
}

@BindingAdapter("isVisible")
fun View.isVisible(boolean: Boolean) {
    isVisible = boolean
}

fun <T : ViewDataBinding> ViewGroup.inflate(
    @LayoutRes layoutRes: Int, attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, attachToParent)
}

@BindingAdapter("setImage")
fun ImageView.setImage(imageUrl: String?) {
    try {
        Glide.with(context).load(imageUrl).into(this)
    } catch (e: java.lang.IllegalArgumentException) {/*
            Happens if load is requested on a destroyed activity.
         */
    }
}

@BindingAdapter("setImage", "placeholder")
fun ImageView.setImageWithPlaceHolder(imageUrl: String?,drawableRes: Int) {
    try {
        Glide.with(context).load(imageUrl).placeholder(drawableRes).into(this)
    } catch (e: java.lang.IllegalArgumentException) {/*
            Happens if load is requested on a destroyed activity.
         */
    }
}

@BindingAdapter("setImageAsBitmap")
fun ImageView.setImageAsBitmap(imageUrl: String?) {
    Glide.with(context).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            this@setImageAsBitmap.setImageBitmap(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {}
    })
}

@BindingAdapter("getDisplayDate")
fun TextView.getDisplayDate(c: Double?) {
    if (c == null) this.gone()
    else {
        val dateFormat = Date((c * 1000).toLong())
        val ddMMM = SimpleDateFormat("dd MMM", Locale.ENGLISH).format(dateFormat)
        val yy = SimpleDateFormat("yy", Locale.ENGLISH).format(dateFormat)
        this.text = "$ddMMM'$yy"

    }
}

@BindingAdapter("setImageUri")
fun ImageView.setImage(uri: Uri) {
    Glide.with(context).load(File(uri.path!!)).into(this)
}


@BindingAdapter("setRoundImage")
fun ImageView.setRoundImage(imageUrl: String?) {
    Glide.with(context).load(imageUrl).circleCrop().into(this)
}

fun ImageView.setRoundImage(imageUrl: String?, drawable: Drawable) {
    if (imageUrl.isNullOrEmpty()) {
        Glide.with(context).load(drawable).circleCrop().into(this)
    } else {
        Glide.with(context).load(imageUrl).placeholder(drawable).circleCrop().into(this)
    }
}

fun ImageView.setRoundImage(drawableId: Int) {
    Glide.with(context).load(drawableId).circleCrop().into(this)
}


private fun isValidContextForGlide(context: Context?): Boolean {
    if (context == null) {
        return false
    }
    if (context is Activity) {
        if (context.isDestroyed || context.isFinishing) {
            return false
        }
    }
    return true
}

@BindingAdapter("setImageDrawable")
fun ImageView.setRoundImageDrawable(@RawRes @DrawableRes imageDrawable: Int?) {
    val isContextValid = isValidContextForGlide(context)
    if (!isContextValid) {
        return
    }
    Glide.with(context).load(imageDrawable).circleCrop().into(this)
}

@BindingAdapter("setRoundImage", "placeholder")
fun ImageView.setRoundImageWithPlaceHolder(imageUrl: String?, drawableRes: Int) {
    Glide.with(context).load(imageUrl).placeholder(drawableRes).circleCrop().into(this)
}

@BindingAdapter("setRoundImage")
fun ImageView.setRoundImage(imageResource: Drawable) {
    Glide.with(context).load(imageResource).circleCrop().into(this)
}

@BindingAdapter("setCachingImage")
fun ImageView.setCachingImage(url: String) {
    Glide.with(context).load(url).skipMemoryCache(false).into(this)
}


@BindingAdapter("setTimeText")
fun TextView.setTimeText(timeInMillis: String?) {
    if (timeInMillis.isNullOrEmpty()) this.gone()
    else {
        var lecDuration = ""
        val duration = timeInMillis.toFloat().toInt()
        val hrs = duration / (60 * 60)
        if (hrs > 9) lecDuration = "$hrs:"
        else if (hrs > 0) lecDuration = "0$hrs:"
        val mins = (duration - (hrs * 60 * 60)) / 60
        lecDuration += if (mins > 9) "$mins:" else "0$mins:"
        val seconds = duration - (hrs * 60 * 60) - (mins * 60)
        lecDuration += if (seconds > 9) "$seconds" else "0$seconds"
        this.text = lecDuration
    }
}

fun View.setHeightAndWidth(width:Double?=null,height:Double?=null){
    val layoutParams = this.layoutParams
    layoutParams.width = width?.toInt()?:layoutParams.width
    layoutParams.height = height?.toInt()?:layoutParams.height
    this.layoutParams = layoutParams
}

fun View.setHeightAndWidth(width:Int?=null,height:Int?=null){
    val layoutParams = this.layoutParams
    layoutParams.width = width?.let { this.context.dpToPx(width) }?:layoutParams.width
    layoutParams.height = height?.let { this.context.dpToPx(height) }?:layoutParams.height
    this.layoutParams = layoutParams
}


fun View.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    val layoutParams =
        (this.layoutParams as? LayoutParams)?:
        (this.layoutParams as? RecyclerView.LayoutParams)?:
        (this.layoutParams as? RelativeLayout.LayoutParams)?:
        (this.layoutParams as? ViewGroup.MarginLayoutParams)?:
        (this.layoutParams as? RelativeLayout.LayoutParams)
    start?.let { layoutParams?.marginStart = this.context.dpToPx(start) }
    top?.let { layoutParams?.topMargin = this.context.dpToPx(top) }
    end?.let { layoutParams?.marginEnd = this.context.dpToPx(end) }
    bottom?.let { layoutParams?.bottomMargin = this.context.dpToPx(bottom) }
    this.layoutParams = layoutParams?:this.layoutParams
}

@BindingAdapter("setImage", "placeholder")
fun ImageView.setImage(imageUrl: String?, placeholder: Drawable) {
    Glide.with(context).load(imageUrl).placeholder(placeholder).into(this)
}

@BindingAdapter("setImage", "placeholder", "error")
fun ImageView.setImage(imageUrl: String?, placeholder: Drawable, error: Drawable) {
    Glide.with(context).load(imageUrl).placeholder(placeholder).error(error).into(this)
}


@BindingAdapter("conditionalVisibility", "permissionList")
fun View.conditionalVisibility(entityPermission: String, permissionList: List<String>) {
    this.showIf(permissionList.contains(entityPermission))
}


@BindingAdapter("conditionalVisibility", "permissionList", "otherCondition")
fun View.conditionalVisibility(
    entityPermission: String, permissionList: List<String>, otherCondition: Boolean = true
) {
    this.showIf(permissionList.contains(entityPermission) && otherCondition)
}

fun <X, A, B> MediatorLiveData<X>.addSources(
    source1: LiveData<A>, source2: LiveData<B>, function: (A, B) -> LiveData<X>
) {
    var mSource: LiveData<X>? = null
    fun onSourceChanged() {
        val newLiveData = function(source1.value ?: return, source2.value ?: return)
        if (mSource === newLiveData) {
            return
        }
        if (mSource != null) {
            removeSource(mSource!!)
        }
        mSource = newLiveData
        if (mSource != null) {
            addSource(mSource!!) { y -> setValue(y) }
        }
    }

    addSource(source1) {
        onSourceChanged()
    }
    addSource(source2) {
        onSourceChanged()
    }
}

fun View.getHorizontalShakeAnimator(): ObjectAnimator {
    return ObjectAnimator.ofFloat(
        this, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f
    )
}


fun TextView.textChangeListener(
    beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
    onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null,
    afterTextChanged: ((s: String) -> Unit)? = null
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged?.invoke(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged?.invoke(s.toString())
        }
    })
}

fun Double.toReadableTime(): String {
    val difference = this.toInt()
    val days = difference / 86400
    val hours = (difference - days * 86400) / 3600
    val minutes = (difference - days * 86400 - hours * 3600) / 60
    val seconds = (difference - days * 86400 - hours * 3600 - minutes * 60)
    val timeBuilder = StringBuilder()
    val daysString = if (days > 0) "${days}d " else " "
    val hoursString = if (hours > 0) "${hours}h " else " "
    val minutesString = if (minutes > 0) "${minutes}m " else " "
    val secondsString =
        if (seconds > 0 && minutes == 0 && hours == 0 && days == 0) "${seconds}s " else " "
    timeBuilder.append(daysString)
    timeBuilder.append(hoursString)
    timeBuilder.append(minutesString)
    timeBuilder.append(secondsString)
    return timeBuilder.toString().trim()
}


fun TextView.setColor(clr: Int) {
    this.setTextColor(ContextCompat.getColor(this.context, clr))
}

fun TextView.setColor(clr: String) {
    this.setTextColor(Color.parseColor(clr))
}


fun Date.toFormat(format: String): String {
    return SimpleDateFormat(format, Locale.ENGLISH).format(this)
}


fun <T> isEqual(first: List<T>, second: List<T>): Boolean {

    if (first.size != second.size) {
        return false
    }

    return first.zip(second).all { (x, y) -> x == y }
}

@BindingAdapter("toggleVisibility")
fun toggleVisibility(inputLayout: TextInputEditText, toggleVisibility: LiveData<String>) {
    if (toggleVisibility.value.isNullOrEmpty()) {
        enableEditText(inputLayout)
    } else {
        disableEditText(inputLayout)
    }
}

private fun disableEditText(editText: EditText) {
    editText.isFocusable = false
    editText.isEnabled = false
    editText.isFocusableInTouchMode = false
//    editText.setBackgroundResource(R.drawable.rounded_bg_bluish_white_black_border)
}

private fun enableEditText(editText: EditText) {
    editText.isFocusable = true
    editText.isEnabled = true
    editText.isFocusableInTouchMode = true;
//    editText.setBackgroundResource(R.drawable.rounded_bg_solid_white_black_border)
}


fun <T> flowSingle(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    function: suspend () -> T,
): Flow<T> {
    return flow { emit(function.invoke()) }.flowOn(dispatcher)
}

fun <T : ViewModel> Fragment.obtainViewModel(
    owner: ViewModelStoreOwner,
    viewModelClass: Class<T>,
    viewmodelFactory: ViewModelProvider.Factory
) = ViewModelProvider(owner, viewmodelFactory).get(viewModelClass)

fun focusView(view: View, color: Int) {
    view.setBackgroundColor(color)
}

fun removeFocusFromView(view: View, color: Int) {
    view.setBackgroundColor(color)
}

fun ImageView.setImage(resourceId: Int) {
    setImageDrawable(ContextCompat.getDrawable(this.context, resourceId))
}

fun List<ImageView>.setImage(resourceId: List<Int>) {
    forEachIndexed { index, element ->
        element.setImageDrawable(ContextCompat.getDrawable(element.context, resourceId[index]))
    }
}

fun EditText.setMaxLength(maxLength: Int?) {
    if (maxLength != null) {
        this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }
}

fun List<EditText?>.setTextWatcher(action: () -> Unit) {
    forEachIndexed { index, element ->
        element?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                action()
            }
        })
    }
}

fun Button.enableIt(colorId: Int) {
    isClickable = true
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorId))
}

fun Button.disableIt(colorId: Int) {
    isClickable = false
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorId))
}

fun ImageView.setImageTint(color: String) {
    this.imageTintList = ColorStateList.valueOf(Color.parseColor(color))
}

fun ImageView.setImageTint(colorId: Int) {
    this.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, colorId))
}

fun TextView.textColor(colorId: Int) {
    this.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this.context, colorId)))
}

fun MaterialCardView.setCardBackgroundColorWithId(colorId: Int) {
    this.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this.context, colorId)))
}

fun MaterialCardView.setCardForegroundColorWithId(colorId: Int) {
    this.setCardForegroundColor(ColorStateList.valueOf(ContextCompat.getColor(this.context, colorId)))
}

fun MaterialCardView.setStrokeColorWithId(colorId: Int) {
    this.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this.context, colorId)))
}

fun View.setBackgroundColor(color: String) {
    this.setBackgroundColor(Color.parseColor(color))
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.setForegroundColor(color: String) {
    this.foregroundTintList = ColorStateList.valueOf(Color.parseColor(color))
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.setForegroundColor(colorId: Int) {
    this.foregroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, colorId))
}

fun CardView.setCardBackgroundColor(color: String) {
    this.setCardBackgroundColor(Color.parseColor(color))
}

fun TextView.setTextWithArgs(stringId: Int, vararg args: Any?) {
    this.text = this.getString(stringId, *args)
}

fun TextView.getString(stringId: Int, vararg args: Any?): String {
    return this.context.getString(stringId, *args)
}

fun TextView.setDateWithFormat(timeInSeconds: Double, pattern: String) {
    this.setDateWithFormat((timeInSeconds * 1000).toLong(), pattern)
}

fun TextView.setDateWithFormat(timeInMillis: Long, pattern: String) {
    val date = Date()
    date.time = timeInMillis
    this.text = SimpleDateFormat(pattern, Locale.ENGLISH).format(date)
}

fun TextView.showWithTextIfNotNullOrEmpty(text: String?, hide: Boolean = false) {
    if (text.isNullOrEmpty().not()) {
        show()
        this.text = text
    } else {
        if (hide) hide()
        else gone()
    }
}

fun TextView.strikeTextView() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.removeStrikeTextView() {
    this.paintFlags = 0
}

fun ImageView.setTint(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)


fun TextView.bold() {
    setTypeface(this.typeface, Typeface.BOLD)
}

fun TextView.normal() {
    this.typeface = null
}


