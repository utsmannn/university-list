package com.utsman.universitylist.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Extension property to create a [DataStore] instance for storing preferences.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "recent_search"
)