package com.utsman.universitylist.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

/**
 * Launches a URL in a custom tab using Chrome Custom Tabs.
 *
 * @param context The context from which to launch the custom tab.
 * @param url The URL to be opened in the custom tab.
 */
fun launchCustomTab(context: Context, url: String) {
    val tabIntent = CustomTabsIntent.Builder().build()
    tabIntent.launchUrl(context, Uri.parse(url))
}