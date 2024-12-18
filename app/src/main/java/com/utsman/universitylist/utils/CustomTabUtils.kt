package com.utsman.universitylist.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun launchCustomTab(context: Context, url: String) {
    val tabIntent = CustomTabsIntent.Builder().build()
    tabIntent.launchUrl(context, Uri.parse(url))
}