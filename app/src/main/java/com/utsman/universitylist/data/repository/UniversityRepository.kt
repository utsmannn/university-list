package com.utsman.universitylist.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.utsman.universitylist.data.University
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.local.UniversityDao
import com.utsman.universitylist.data.mapToEntity
import com.utsman.universitylist.data.remote.UniversityApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UniversityRepository @Inject constructor(
    private val universityDao: UniversityDao,
    private val apiService: UniversityApiService,
    private val dataStore: DataStore<Preferences>
) {

    private val recentSearchKey = stringSetPreferencesKey("recent_search")

    fun getUniversityPaging(): Flow<PagingData<UniversityEntity>> {
        val pagingSourceFactory = { universityDao.getAllUniversities() }
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun searchUniversityPaging(query: String): Flow<PagingData<UniversityEntity>> {
        val pagingSourceFactory = { universityDao.searchUniversitiesByName(query) }
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun fetchAndSaveUniversities() {
        val dataFromDb = universityDao.getAllUniversities().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        if (dataFromDb.data.isEmpty()) {
            val universityApi = apiService.getUniversities()
            val universityEntities = universityApi.map { response -> response.mapToEntity() }
            universityDao.insertUniversities(universityEntities)
        }
    }

    fun getRecentSearch(): Flow<List<String>> {
        return dataStore.data.map { it[recentSearchKey] }.map { it?.toList().orEmpty() }
    }

    suspend fun addRecentSearch(query: String) {
        dataStore.edit { pref ->
            val current = pref[recentSearchKey].orEmpty()
            if (!current.contains(query)) {
                pref[recentSearchKey] = current.minus(query)
            }

            pref[recentSearchKey] = setOf(query) + current
        }
    }

}