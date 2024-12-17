package com.utsman.universitylist

import androidx.paging.PagingSource
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.local.UniversityDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

@Suppress("UNCHECKED_CAST")
class UniversityDaoTest {

    private val mockDao = mockk<UniversityDao>()

    @Test
    fun `Should getAllUniversities successfully`() = runBlocking {
        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "bagus.com","https://bagus.com", "https://image.png"),
            UniversityEntity(2, "Univ keren", "keren.com", "https://keren.com", "https://image.png")
        )

        val mockPagingSource = PagingSource.LoadResult.Page(
            data = expectedData,
            prevKey = null,
            nextKey = null
        ) as PagingSource.LoadResult<Int, UniversityEntity>

        coEvery { mockDao.getAllUniversities().load(any()) } returns mockPagingSource

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
    fun `Should return matching result on searchUniversityByName`() = runBlocking {
        val searchQuery = "bagus"
        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "bagus.com","https://bagus.com", "https://image.png")
        )

        val mockPagingSource = PagingSource.LoadResult.Page(
            data = expectedData,
            prevKey = null,
            nextKey = null
        ) as PagingSource.LoadResult<Int, UniversityEntity>

        coEvery { mockDao.searchUniversitiesByName(searchQuery).load(any()) } returns mockPagingSource

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
    fun `Should return empty result on searchUniversityByName with no matches`() = runBlocking {
        val searchQuery = "unknown"
        val expectedData = emptyList<UniversityEntity>()

        val mockPagingSource = PagingSource.LoadResult.Page(
            data = expectedData,
            prevKey = null,
            nextKey = null
        ) as PagingSource.LoadResult<Int, UniversityEntity>

        coEvery { mockDao.searchUniversitiesByName(searchQuery).load(any()) } returns mockPagingSource

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
        assertTrue(actualData.isEmpty())
    }

    @Test
    fun `Should insertUniversities successfully`() = runBlocking {
        val universities = listOf(
            UniversityEntity(1, "Univ bagus", "bagus.com","https://bagus.com", "https://image.png"),
            UniversityEntity(2, "Univ keren", "keren.com", "https://keren.com", "https://image.png")
        )

        coEvery { mockDao.insertUniversities(universities) } returns Unit

        mockDao.insertUniversities(universities)
        coVerify { mockDao.insertUniversities(universities) }
    }
}