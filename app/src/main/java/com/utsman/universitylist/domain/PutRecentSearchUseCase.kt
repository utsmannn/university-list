package com.utsman.universitylist.domain

import com.utsman.universitylist.data.repository.UniversityRepository
import javax.inject.Inject

class PutRecentSearchUseCase @Inject constructor(
    private val universityRepository: UniversityRepository
) {

    suspend fun putRecentSearch(query: String) {
        universityRepository.addRecentSearch(query)
    }
}