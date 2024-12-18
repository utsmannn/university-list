package com.utsman.universitylist.domain

import com.utsman.universitylist.data.repository.UniversityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchUseCase @Inject constructor(
    private val universityRepository: UniversityRepository
) {

    fun getRecentSearch(): Flow<List<String>> = universityRepository.getRecentSearch()
}