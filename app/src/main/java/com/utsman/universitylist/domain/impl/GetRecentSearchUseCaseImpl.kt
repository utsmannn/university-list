package com.utsman.universitylist.domain.impl

import com.utsman.universitylist.data.repository.UniversityRepository
import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchUseCaseImpl @Inject constructor(
    private val universityRepository: UniversityRepository
) : GetRecentSearchUseCase {

    override fun getRecentSearch(): Flow<List<String>> = universityRepository.getRecentSearch()
}