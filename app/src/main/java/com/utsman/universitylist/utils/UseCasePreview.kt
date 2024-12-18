package com.utsman.universitylist.utils

import androidx.paging.PagingData
import com.utsman.universitylist.data.University
import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import com.utsman.universitylist.domain.api.GetUniversityUseCase
import com.utsman.universitylist.domain.api.PutRecentSearchUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake implementation of [GetRecentSearchUseCase] for preview.
 */
class FakeGetRecentSearchUseCase : GetRecentSearchUseCase {
    override fun getRecentSearch(): Flow<List<String>> {
        return flowOf(listOf("Universitas A", "Universitas B", "Universitas C"))
    }
}

/**
 * Fake implementation of [GetUniversityUseCase] for preview.
 */
class FakeGetUniversityUseCase : GetUniversityUseCase {
    private val universities = listOf(
        University(
            name = "University A",
            domain = "universitya.edu",
            webPage = "https://www.universitya.edu",
            imageUrl = "https://placehold.co/600x400/red/white?text=UA"
        ),
        University(
            name = "University B",
            domain = "universityb.edu",
            webPage = "https://www.universityb.edu",
            imageUrl = "https://placehold.co/600x400/blue/white?text=UB"
        )
    )

    override fun getUniversities(): Flow<PagingData<University>> {
        return flowOf(PagingData.from(universities))
    }

    override fun searchUniversityByName(query: String): Flow<PagingData<University>> {
        return flowOf(PagingData.from(universities))
    }

    override suspend fun refreshUniversity() = Result.success(Unit)
}


/**
 * Fake implementation of [PutRecentSearchUseCase] for preview.
 */
class FakePutRecentSearchUseCase : PutRecentSearchUseCase {
    override suspend fun putRecentSearch(query: String) {}
}