package com.utsman.universitylist.domain.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.utsman.universitylist.data.University
import com.utsman.universitylist.data.mapToDto
import com.utsman.universitylist.data.repository.UniversityRepository
import com.utsman.universitylist.domain.api.GetUniversityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [GetUniversityUseCase].
 *
 * @property universityRepository The repository for accessing university data.
 */
class GetUniversityUseCaseImpl @Inject constructor(
    private val universityRepository: UniversityRepository
) : GetUniversityUseCase {

    /**
     * Retrieves a Flow of PagingData containing all universities, mapped to DTOs.
     *
     * @return Flow emitting [PagingData] of [University] DTOs.
     */
    override fun getUniversities(): Flow<PagingData<University>> {
       return universityRepository.getUniversityPaging()
           .map { paging ->
               paging.map { entity -> entity.mapToDto() }
           }
    }

    /**
     * Searches for universities by name and retrieves a Flow of [PagingData], mapped to DTOs.
     *
     * @param query The search query.
     * @return Flow emitting PagingData of [University] DTOs matching the query.
     */
    override fun searchUniversityByName(query: String): Flow<PagingData<University>> {
        return universityRepository.searchUniversityPaging(query)
            .map { paging ->
                paging.map { entity -> entity.mapToDto() }
            }
    }

    /**
     * Refreshes the university data by fetching from the remote API and saving to the local database.
     * @return [Result] of status fetcher
     */
    override suspend fun refreshUniversity() = universityRepository.fetchAndSaveUniversities()
}