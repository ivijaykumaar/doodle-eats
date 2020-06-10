package com.doodleblue.doodleeats.general

import android.app.Activity
import android.content.Context
import com.doodleblue.doodleeats.general.Constants.PUBLIC_KEY


fun saveData(key: String, value: String, context: Context) {
    val editor = context.getSharedPreferences(PUBLIC_KEY, Activity.MODE_PRIVATE).edit()
    editor.putString(key, value)
    editor.apply()
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun getData(key: String, context: Context): String? {
    val prefs = context.getSharedPreferences(PUBLIC_KEY, Activity.MODE_PRIVATE)
    val value = prefs.getString(key, "")
    return if (value.isNullOrBlank()) {
        value
    } else {
        value
    }
}