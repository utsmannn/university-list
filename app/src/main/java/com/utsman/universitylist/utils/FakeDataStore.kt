package com.utsman.universitylist.utils


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Fake implementation of DataStore<Preferences> for testing and preview purposes.
 */
class FakeDataStore : DataStore<Preferences> {
    private val _data = MutableStateFlow(emptyPreferences())
    override val data: Flow<Preferences>
        get() = _data

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        _data.update { transform(it) }
        return _data.value
    }
}