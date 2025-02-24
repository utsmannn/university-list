package com.utsman.universitylist.ui.homepage

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.utsman.universitylist.ui.homepage.component.ToolbarWithSearchBar
import com.utsman.universitylist.ui.homepage.component.UniversityItemContent
import com.utsman.universitylist.ui.theme.UniversityListTheme
import com.utsman.universitylist.ui.viewmodel.HomeViewModel
import com.utsman.universitylist.utils.FakeGetRecentSearchUseCase
import com.utsman.universitylist.utils.FakeGetUniversityUseCase
import com.utsman.universitylist.utils.FakePutRecentSearchUseCase
import com.utsman.universitylist.utils.launchCustomTab
import kotlinx.coroutines.launch


/**
 * [HomePage] composable that displays the list of universities and handles search functionality.
 *
 * @param homeViewModel The ViewModel managing the UI state and data.
 */
@Composable
fun HomePage(
    homeViewModel: HomeViewModel = viewModel()
) {

    val context = LocalContext.current

    // Collect the paginated list of universities
    val universitiesPaged = homeViewModel.universities.collectAsLazyPagingItems()

    // Collect the list of recent search queries
    val recentSearch by homeViewModel.recentSearch.collectAsState()

    // Flag to indicate if the current view is showing search results
    val isSearchResult by homeViewModel.isSearchResult.collectAsState()

    val errorMessage by homeViewModel.errorMessage.collectAsState()

    // State of the lazy list to manage scroll position
    val lazyListState = rememberLazyListState()

    // Determine if the list has scrolled away from the top
    val isScrollReachTop by remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset == 0 }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = errorMessage) {
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    // Handle back press when in search result mode
    BackHandler(isSearchResult) {
        scope.launch {
            homeViewModel.setIsSearchResult(false)
            homeViewModel.search("")
            if (universitiesPaged.itemCount > 0) {
                lazyListState.scrollToItem(0)
            }
        }

    }

    Scaffold(
        topBar = {
            ToolbarWithSearchBar(
                modifier = Modifier.fillMaxWidth(),
                showShadow = !isScrollReachTop,
                showSearchResult = isSearchResult,
                recentSearch = recentSearch,
                onSearch = {
                    scope.launch {
                        homeViewModel.setIsSearchResult(it.isNotEmpty())
                        homeViewModel.search(it)
                        if (universitiesPaged.itemCount > 0) {
                            lazyListState.scrollToItem(0)
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            ConstraintLayout(
                modifier = Modifier.padding(innerPadding)
            ) {

                // Display loading or error state based on the refresh load state
                universitiesPaged.apply {
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            LoadingItem()
                        }

                        is LoadState.Error -> {
                            val e = universitiesPaged.loadState.refresh as LoadState.Error
                            ErrorItem(message = e.error.localizedMessage ?: "Error")
                        }

                        else -> {}
                    }
                }

                // LazyColumn to display the list of universities
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    state = lazyListState
                ) {

                    items(
                        count = universitiesPaged.itemCount
                    ) { index ->
                        val university = universitiesPaged[index]

                        university?.let {
                            UniversityItemContent(
                                modifier = Modifier.fillMaxWidth(),
                                university = university
                            ) {
                                // Launch the university's web page in a cutom tab
                                launchCustomTab(context, university.webPage)
                            }
                        }
                    }

                    // Display loading or error state based on the append load state
                    universitiesPaged.apply {
                        when (loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    LoadingItem()
                                }
                            }

                            is LoadState.Error -> {
                                val e = universitiesPaged.loadState.append as LoadState.Error
                                item {
                                    ErrorItem(message = e.error.localizedMessage ?: "Error")
                                }
                            }

                            is LoadState.NotLoading -> {}
                        }
                    }
                }

            }
        }
    )
}

/**
 * Composable displaying a loading indicator.
 */
@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Composable displaying an error message.
 *
 * @param message The error message to display.
 */
@Composable
fun ErrorItem(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Preview of the [HomePage] composable with fake use cases.
 */
@Composable
@Preview(showBackground = true)
fun HomePagePreview() {

    // Sample ViewModel for preview
    val sampleViewModel = HomeViewModel(
        getUniversityUseCase = FakeGetUniversityUseCase(),
        getRecentSearchUseCase = FakeGetRecentSearchUseCase(),
        putRecentSearchUseCase = FakePutRecentSearchUseCase()
    )

    UniversityListTheme {
        HomePage(homeViewModel = sampleViewModel)
    }
}