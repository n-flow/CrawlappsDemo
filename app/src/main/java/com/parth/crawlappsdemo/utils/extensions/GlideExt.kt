package com.parth.crawlappsdemo.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.utils.logger
import com.parth.crawlappsdemo.utils.views.imageViews.textdrawable.TextDrawable

fun makeTextDrawable(context: Context, dr: (Any) -> Unit = {}) {

    val fName = "C"
    val lName = "A"
    val name = "${fName}${lName}"

    logger("makeTextDrawable:  ", "name:  $name")

    val drawable = TextDrawable.builder().beginConfig()
        .textColor(ContextCompat.getColor(context, R.color.profile_name_initial))
        .bold().toUpperCase().endConfig()
        .buildRound(name, ContextCompat.getColor(context, R.color.profile_pic_background))
    dr.invoke(drawable)
}

@Synchronized
fun getColor(context: Context, resourceId: Int): Int {
    return ContextCompat.getColor(context, resourceId)
}

fun ImageView.loadImage(drawable: Any?) {
    val glide = when (drawable) {
        is String -> Glide.with(context).load(drawable)
        is Drawable -> Glide.with(context).load(drawable)
        else -> Glide.with(context).load(drawable)
    }

    glide.transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .apply(RequestOptions.circleCropTransform())
        .circleCrop()
        .placeholder(context.getDrawableRes(R.drawable.default_profile_new))
        .error(context.getDrawableRes(R.drawable.default_profile_new))
        .into(this)
}