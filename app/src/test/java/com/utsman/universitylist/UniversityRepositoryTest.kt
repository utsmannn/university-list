package com.utsman.universitylist

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.UniversityResponse
import com.utsman.universitylist.data.local.UniversityDao
import com.utsman.universitylist.data.mapToEntity
import com.utsman.universitylist.data.remote.UniversityApiService
import com.utsman.universitylist.data.repository.UniversityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response


/**
 * Unit tests for the [UniversityRepository] class.
 */
class UniversityRepositoryTest {

    private val mockDao = mockk<UniversityDao>()
    private val mockApiService = mockk<UniversityApiService>()
    private val mockDataStore = mockk<DataStore<Preferences>>()

    // Instance of UniversityRepository with mocked dependencies
    private val repository = UniversityRepository(mockDao, mockApiService, mockDataStore)

    /**
     * Tests that [UniversityRepository.getUniversityPaging] returns the correct paging data.
     */
    @Test
    fun `Should return paging data from getUniversityPaging`() = runTest {
        val expectedData = listOf(
            UniversityEntity(1, "Univ cakep", "cakep.com", "https://cakep.com", "https://image.png"),
            UniversityEntity(2, "Univ keren", "keren.com", "https://keren.com", "https://image.png")
        )

        // Mocking the PagingSource
        val mockPagingSource = object : PagingSource<Int, UniversityEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UniversityEntity> {
                return LoadResult.Page(
                    data = expectedData,
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, UniversityEntity>): Int? {
                return null
            }
        }

        coEvery { mockDao.getAllUniversities() } returns mockPagingSource

        // Create Pager instance
        val pager = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { mockDao.getAllUniversities() }
        )

        // Collect the first page of data
        val result = flowOf(pager.flow.first()).asSnapshot()

        // Assert that the returned data matches expected data
        assertEquals(expectedData, result)
    }

    /**
     * Tests that [UniversityRepository.searchUniversityPaging] returns the correct filtered paging data.
     */
    @Test
    fun `Should return paging data from searchUniversityPaging`() = runTest {
        val query = "cakep"
        val expectedData = listOf(
            UniversityEntity(1, "Univ cakep", "cakep.com", "https://cakep.com", "https://image.png")
        )

        val mockPagingSource = object : PagingSource<Int, UniversityEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UniversityEntity> {
                return LoadResult.Page(
                    data = expectedData,
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, UniversityEntity>): Int? {
                return null
            }
        }

        coEvery { mockDao.searchUniversitiesByName(query) } returns mockPagingSource

        val pager = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { mockDao.searchUniversitiesByName(query) }
        )

        val result = flowOf(pager.flow.first()).asSnapshot()

        assertEquals(expectedData.map { it.name }, result.map { it.name })
    }

    /**
     * Tests that [UniversityRepository.fetchAndSaveUniversities] fetches from API and saves to DB when DB is empty.
     */
    @Test
    fun `Should fetch and save universities when database is empty`() = runTest {
        // Mocking an empty database
        coEvery { mockDao.getAllUniversities() } returns mockk<PagingSource<Int, UniversityEntity>> {
            coEvery { load(any()) } returns PagingSource.LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        val apiResponse = listOf(
            UniversityResponse(
                stateProvince = null,
                alphaTwoCode = null,
                name = "Univ keren",
                domains = listOf("keren.com"),
                webPages = listOf("http://keren.com"),
                country = "Indonesia"
            )
        )

        val expectedEntities = apiResponse.map { it.mapToEntity() }

        // Mocking API response and DB insert
        coEvery { mockApiService.getUniversities() } returns Response.success(apiResponse)
        coEvery { mockDao.insertUniversities(any()) } returns Unit
        coEvery { mockDao.getCount() } returns 0

        // Invoke the method under test
        repository.fetchAndSaveUniversities()

        coVerify { mockApiService.getUniversities() }

        // Verify that the API was called and data was inserted into database
        coVerify { mockDao.insertUniversities(match { entities ->
            entities.zip(expectedEntities).all { (actual, expected) ->
                actual.name == expected.name &&
                        actual.domain == expected.domain &&
                        actual.webPage == expected.webPage
            }
        }) }
    }

    /**
     * Tests that [UniversityRepository.fetchAndSaveUniversities] does not fetch from API when DB is not empty.
     */
    @Test
    fun `Should not fetch from API when database is not empty`() = runTest {
        coEvery { mockDao.getAllUniversities() } returns mockk<PagingSource<Int, UniversityEntity>> {
            coEvery { load(any()) } returns PagingSource.LoadResult.Page(
                data = listOf(
                    UniversityEntity(1, "Univ cakep", "cakep.com", "https://cakep.com", "https://image.png")
                ),
                prevKey = null,
                nextKey = null
            )
        }
        coEvery { mockDao.getCount() } returns 1

        repository.fetchAndSaveUniversities()

        // Verify that the API was not called and data was not inserted
        coVerify(exactly = 0) { mockApiService.getUniversities() }
        coVerify(exactly = 0) { mockDao.insertUniversities(any()) }
    }
}