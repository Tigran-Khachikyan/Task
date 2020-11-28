package com.example.task.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.example.task.R
import kotlinx.coroutines.delay

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun TextView.showStatus(res: Int) {
    setText(res)
    visibility = View.VISIBLE
}

fun TextView.hide() {
    visibility = View.GONE
}

fun Button.makeSaver() {
    setText(R.string.save)
    setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_star,0)
}

fun Button.makeRemoving() {
    setText(R.string.remove)
    setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_trash_mini,0)
}

fun View.onInitialized(onInit: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (isShown) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onInit()
            }
        }
    })
}

