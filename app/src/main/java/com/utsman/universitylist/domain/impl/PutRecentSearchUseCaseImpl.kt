package com.utsman.universitylist.domain.impl

import com.utsman.universitylist.data.repository.UniversityRepository
import com.utsman.universitylist.domain.api.PutRecentSearchUseCase
import javax.inject.Inject

class PutRecentSearchUseCaseImpl @Inject constructor(
    private val universityRepository: UniversityRepository
) : PutRecentSearchUseCase {

    override suspend fun putRecentSearch(query: String) {
        universityRepository.addRecentSearch(query)
    }
}