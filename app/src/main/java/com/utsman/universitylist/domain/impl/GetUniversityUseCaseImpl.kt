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


class GetUniversityUseCaseImpl @Inject constructor(
    private val universityRepository: UniversityRepository
) : GetUniversityUseCase {

    override fun getUniversities(): Flow<PagingData<University>> {
       return universityRepository.getUniversityPaging()
           .map { paging ->
               paging.map { entity -> entity.mapToDto() }
           }
    }

    override fun searchUniversityByName(query: String): Flow<PagingData<University>> {
        return universityRepository.searchUniversityPaging(query)
            .map { paging ->
                paging.map { entity -> entity.mapToDto() }
            }
    }

    override suspend fun refreshUniversity() = universityRepository.fetchAndSaveUniversities()
}