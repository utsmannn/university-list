package com.utsman.universitylist

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.mapToDto
import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import com.utsman.universitylist.domain.api.PutRecentSearchUseCase
import com.utsman.universitylist.domain.impl.GetRecentSearchUseCaseImpl
import com.utsman.universitylist.domain.impl.GetUniversityUseCaseImpl
import com.utsman.universitylist.domain.impl.PutRecentSearchUseCaseImpl
import com.utsman.universitylist.ui.viewmodel.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


/**
 * Unit tests for the [HomeViewModel] class.
 */
class UniversityViewModelTest {

    // Test dispatcher for controlling coroutine execution
    private val dispatcher = StandardTestDispatcher()

    // Mocked use cases
    private val mockGetUniversityUseCase: GetUniversityUseCaseImpl = mockk<GetUniversityUseCaseImpl>()
    private val mockGetRecentSearchUseCase: GetRecentSearchUseCase = mockk<GetRecentSearchUseCaseImpl>()
    private val mockkPutRecentSearchUseCase: PutRecentSearchUseCase = mockk<PutRecentSearchUseCaseImpl>()

    // Instance of HomeViewModel under test
    private lateinit var viewModel: HomeViewModel

    /**
     * Sets up the test environment by setting the main dispatcher.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    /**
     * Tears down the test environment by resetting the main dispatcher.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests that the [HomeViewModel] fetches and exposes the correct paging data from the repository.
     */
    @Test
    fun `Should fetch paging data from repository`() = runTest {
        // Initialize ViewModel with mocked use cases
        viewModel = HomeViewModel(mockGetUniversityUseCase, mockGetRecentSearchUseCase, mockkPutRecentSearchUseCase)

        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "bagus.com", "https://bagus.com", "https://image.png"),
            UniversityEntity(2, "Univ keren", "keren.com", "https://keren.com", "https://image.png")
        ).map { it.mapToDto() }

        val paging = PagingData.from(expectedData)

        // Mock the use case methods
        coEvery { mockGetUniversityUseCase.refreshUniversity() } returns Result.success(Unit)
        coEvery { mockGetUniversityUseCase.getUniversities() } returns flowOf(paging)
        coEvery { mockGetRecentSearchUseCase.getRecentSearch() } returns flowOf(listOf())

        // Collect the universities PagingData on Flow as snapshot
        val result = flowOf(viewModel.universities.first()).asSnapshot()

        // Assert that the universities data matches expected data
        assertEquals(expectedData, result)
    }

    /**
     * Tests that the [HomeViewModel] correctly handles searching for universities.
     */
    @Test
    fun `Should search university paging data`() = runTest {
        val query = "bagus"
        viewModel = HomeViewModel(mockGetUniversityUseCase, mockGetRecentSearchUseCase, mockkPutRecentSearchUseCase)

        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "bagus.com", "https://bagus.com", "https://image.png"),
        ).map { it.mapToDto() }

        val paging = PagingData.from(expectedData)

        // Invoke the search method
        viewModel.search(query)

        // Mock the use case methods
        coEvery { mockGetUniversityUseCase.refreshUniversity() } returns Result.success(Unit)
        coEvery { mockGetUniversityUseCase.searchUniversityByName(query) } returns flowOf(paging)
        coEvery { mockGetRecentSearchUseCase.getRecentSearch() } returns flowOf(listOf())
        coEvery { mockkPutRecentSearchUseCase.putRecentSearch(query) } returns Unit

        // Collect the universities after search
        val result = flowOf(viewModel.universities.first()).asSnapshot()
        assertEquals(expectedData, result)
    }
}
