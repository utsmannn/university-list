package com.utsman.universitylist

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.utsman.universitylist.data.UniversityEntity
import com.utsman.universitylist.data.mapToDto
import com.utsman.universitylist.domain.GetUniversityUseCase
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

class UniversityViewModelTest {

    private val useCase = mockk<GetUniversityUseCase>()
    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Should fetch paging data from repository`() = runTest {
        viewModel = HomeViewModel(useCase)

        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "bagus.com", "https://bagus.com", "https://image.png"),
            UniversityEntity(2, "Univ keren", "keren.com", "https://keren.com", "https://image.png")
        ).map { it.mapToDto() }

        val paging = PagingData.from(expectedData)

        coEvery { useCase.refreshUniversity() } returns Unit
        coEvery { useCase.getUniversities() } returns flowOf(paging)

        val result = flowOf(viewModel.universities.first()).asSnapshot()
        assertEquals(expectedData, result)
    }

    @Test
    fun `Should search university paging data`() = runTest {
        val query = "bagus"
        viewModel = HomeViewModel(useCase)

        val expectedData = listOf(
            UniversityEntity(1, "Univ bagus", "bagus.com", "https://bagus.com", "https://image.png"),
        ).map { it.mapToDto() }

        val paging = PagingData.from(expectedData)

        viewModel.search(query)
        coEvery { useCase.refreshUniversity() } returns Unit
        coEvery { useCase.searchUniversityByName(query) } returns flowOf(paging)

        val result = flowOf(viewModel.universities.first()).asSnapshot()
        assertEquals(expectedData, result)
    }
}
