package com.utsman.universitylist.domain.impl

import com.utsman.universitylist.data.repository.UniversityRepository
import com.utsman.universitylist.domain.api.PutRecentSearchUseCase
import javax.inject.Inject

/**
 * Implementation of [PutRecentSearchUseCase].
 *
 * @property universityRepository The repository for accessing university data.
 */
class PutRecentSearchUseCaseImpl @Inject constructor(
    private val universityRepository: UniversityRepository
) : PutRecentSearchUseCase {

    /**
     * Adds a new search query to the recent searches via the repository.
     *
     * @param query The search query to add.
     */
    override suspend fun putRecentSearch(query: String) {
        universityRepository.addRecentSearch(query)
    }
}