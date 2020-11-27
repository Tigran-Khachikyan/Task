package com.example.task.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

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