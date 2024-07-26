package com.parth.crawlappsdemo.utils.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Parcelable
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.parth.crawlappsdemo.R
import java.io.Serializable
import kotlin.math.abs

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

fun Context.stringRes(id: Int) = resources.getString(id)

fun Context.getDrawableRes(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Any.showToast(context: Context) {
    Toast.makeText(context, "" + this.toString(), Toast.LENGTH_SHORT).show()
}

fun Context.getContactLetterIcon(name: String): Bitmap {
    val size = resources.getDimension(R.dimen.normal_icon_size).toInt()
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val view = TextView(this)
    view.layout(0, 0, size, size)

    val circlePaint = Paint().apply {
        color = letterBackgroundColors[abs(name.hashCode()) % letterBackgroundColors.size].toInt()
        isAntiAlias = true
    }

    val wantedTextSize = size / 2f
    val textPaint = Paint().apply {
        color = circlePaint.color.getContrastColor()
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = wantedTextSize
        style = Paint.Style.FILL
    }

    canvas.drawCircle(size / 2f, size / 2f, size / 2f, circlePaint)

    val xPos = canvas.width / 2f
    val yPos = canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2
    canvas.drawText(name, xPos, yPos, textPaint)
    view.draw(canvas)
    return bitmap
}

val letterBackgroundColors = arrayListOf(
    0xCCF255AB,
    0xCCF28836,
    0xCC4BAF65,
    0xCCE55C51,
    0xCCF2C338,
    0xCC3FC1DB,
    0xCCA550EF,
    0xCC7C48F1,
    0xCC698491,
    0xCCE6407D
)
