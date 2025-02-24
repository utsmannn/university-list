package com.utsman.universitylist.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.local.UniversityDao
import com.utsman.universitylist.data.mapToEntity
import com.utsman.universitylist.data.remote.UniversityApiService
import com.utsman.universitylist.utils.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository class for managing university data.
 * Handles data operations from both local database and remote API.
 *
 * @property universityDao DAO for local database operations.
 * @property apiService Service for remote API operations.
 * @property dataStore DataStore for managing recent search preferences.
 */
class UniversityRepository @Inject constructor(
    private val universityDao: UniversityDao,
    private val apiService: UniversityApiService,
    private val dataStore: DataStore<Preferences>
) {

    // Key for storing recent search queries in DataStore
    private val recentSearchKey = stringSetPreferencesKey("recent_search")

    /**
     * Retrieves a Flow of PagingData containing all universities.
     *
     * @return Flow emitting [PagingData] of [UniversityEntity].
     */
    fun getUniversityPaging(): Flow<PagingData<UniversityEntity>> {
        val pagingSourceFactory = { universityDao.getAllUniversities() }
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    /**
     * Searches for universities by name and retrieves a Flow of [PagingData].
     *
     * @param query The search query.
     * @return Flow emitting [PagingData] of [UniversityEntity] matching the query.
     */
    fun searchUniversityPaging(query: String): Flow<PagingData<UniversityEntity>> {
        val pagingSourceFactory = { universityDao.searchUniversitiesByName(query) }
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    /**
     * Fetches universities from the remote API and saves them to the
     * local database if the database is empty.
     * @return [Result] of status the fetcher
     */
    suspend fun fetchAndSaveUniversities(): Result<Unit> {
        val isDbEmpty = universityDao.getCount() == 0

        if (isDbEmpty) {
            val response = apiService.getUniversities()
            return response.toResult().fold(
                onSuccess = { universityApi ->
                    val universityEntities = universityApi.map { response -> response.mapToEntity() }
                    try {
                        universityDao.insertUniversities(universityEntities)
                        Result.success(Unit)
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } else {
            return Result.success(Unit)
        }
    }

    /**
     * Retrieves a Flow of recent search queries.
     *
     * @return Flow emitting a list of recent search strings.
     */
    fun getRecentSearch(): Flow<List<String>> {
        return dataStore.data.map { it[recentSearchKey] }
            .filterNotNull()
            .map { set ->
                set.toList().filter { query -> query.isNotEmpty() }
            }
    }

    /**
     * Adds a new search query to the recent searches in [DataStore].
     *
     * @param query The search query to add.
     */
    suspend fun addRecentSearch(query: String) {
        if (query.isEmpty()) return

        // Remove the query if it already exists to avoid duplicates
        dataStore.edit { pref ->
            val current = pref[recentSearchKey].orEmpty()
            if (current.contains(query)) {
                pref[recentSearchKey] = current.minus(query)
            }
        }

        // Add the query to the beginning of the recent searches
        dataStore.edit { pref ->
            val current = pref[recentSearchKey].orEmpty()
            pref[recentSearchKey] = setOf(query) + current
        }
    }

}