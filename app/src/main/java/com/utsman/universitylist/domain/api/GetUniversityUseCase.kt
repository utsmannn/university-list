package com.utsman.universitylist.domain.api

import androidx.paging.PagingData
import com.utsman.universitylist.data.University
import kotlinx.coroutines.flow.Flow

interface GetUniversityUseCase {

    fun getUniversities(): Flow<PagingData<University>>

    fun searchUniversityByName(query: String): Flow<PagingData<University>>

    suspend fun refreshUniversity()
}