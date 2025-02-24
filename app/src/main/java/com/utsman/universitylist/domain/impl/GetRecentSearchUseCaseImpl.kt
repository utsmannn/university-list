package com.utsman.universitylist.domain.impl

import com.utsman.universitylist.data.repository.UniversityRepository
import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [GetRecentSearchUseCase].
 *
 * @property universityRepository The repository for accessing university data.
 */
class GetRecentSearchUseCaseImpl @Inject constructor(
    private val universityRepository: UniversityRepository
) : GetRecentSearchUseCase {

    /**
     * Retrieves a Flow of recent search queries from the repository.
     *
     * @return Flow emitting a list of recent search strings.
     */
    override fun getRecentSearch(): Flow<List<String>> = universityRepository.getRecentSearch()
}