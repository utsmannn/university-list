package com.utsman.universitylist.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.utsman.universitylist.data.University
import com.utsman.universitylist.data.mapToDto
import com.utsman.universitylist.data.repository.UniversityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetUniversityUseCase @Inject constructor(
    private val universityRepository: UniversityRepository
) {

    fun getUniversities(): Flow<PagingData<University>> {
       return universityRepository.getUniversityPaging()
           .map { paging ->
               paging.map { entity -> entity.mapToDto() }
           }
    }

    fun searchUniversityByName(query: String): Flow<PagingData<University>> {
        return universityRepository.searchUniversityPaging(query)
            .map { paging ->
                paging.map { entity -> entity.mapToDto() }
            }
    }

    suspend fun refreshUniversity() = universityRepository.fetchAndSaveUniversities()
}