package com.utsman.universitylist.domain.api

import androidx.paging.PagingData
import com.utsman.universitylist.data.University
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the use case for retrieving universities.
 */
interface GetUniversityUseCase {

    /**
     * Retrieves a Flow of [PagingData] containing all universities.
     *
     * @return Flow emitting [PagingData] of [University] DTOs.
     */
    fun getUniversities(): Flow<PagingData<University>>

    /**
     * Searches for universities by name and retrieves a Flow of [PagingData].
     *
     * @param query The search query.
     * @return Flow emitting [PagingData] of [University] DTOs matching the query.
     */
    fun searchUniversityByName(query: String): Flow<PagingData<University>>

    /**
     * Refreshes the university data by fetching from the remote API and
     * saving to the local database.
     */
    suspend fun refreshUniversity(): Result<Unit>
}