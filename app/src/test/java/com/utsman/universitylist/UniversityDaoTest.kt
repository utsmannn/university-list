package com.utsman.universitylist

import androidx.paging.PagingSource
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.local.UniversityDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

@Suppress("UNCHECKED_CAST")
class UniversityDaoTest {

    private val mockDao = mockk<UniversityDao>()

    @Test
    fun testGetAllUniversities() = runBlocking {
        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "https://bagus.com", "https://image.png"),
            UniversityEntity(1, "Univ keren", "https://keren.com", "https://image.png"),
        )

        val mockPagingSources = PagingSource.LoadResult.Page(
            data = expectedData,
            prevKey = null,
            nextKey = null
        ) as PagingSource.LoadResult<Int, UniversityEntity>

        coEvery { mockDao.getAllUniversities().load(any()) } returns mockPagingSources

        val result = mockDao.getAllUniversities()
        val loadResult = result.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        coVerify { mockDao.getAllUniversities() }
        val actualData = (loadResult as PagingSource.LoadResult.Page).data
        assertEquals(expectedData, actualData)
    }

    @Test
    fun testSearchUniversityByName() = runBlocking {
        val searchQuery = "bagus"
        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "https://bagus.com", "https://image.png")
        )

        val mockPagingSources = PagingSource.LoadResult.Page(
            data = expectedData,
            prevKey = null,
            nextKey = null
        ) as PagingSource.LoadResult<Int, UniversityEntity>

        coEvery { mockDao.searchUniversitiesByName(searchQuery).load(any()) } returns mockPagingSources

        val result = mockDao.searchUniversitiesByName(searchQuery)
        val loadResult = result.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        coVerify { mockDao.searchUniversitiesByName(searchQuery) }
        val actualData = (loadResult as PagingSource.LoadResult.Page).data
        assertEquals(expectedData, actualData)
    }

    @Test
    fun testInsertUniversities() = runBlocking {
        val universities = listOf(
            UniversityEntity(1, "Univ bagus", "https://bagus.com", "https://image.png"),
            UniversityEntity(1, "Univ keren", "https://keren.com", "https://image.png"),
        )

        coEvery { mockDao.insertUniversities(universities) } returns Unit

        mockDao.insertUniversities(universities)
        coVerify { mockDao.insertUniversities(universities) }
    }
}