package com.belkanoid.weatherapp2.domain.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.showSnackBar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

