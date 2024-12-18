package com.utsman.universitylist.ui.homepage

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.utsman.universitylist.data.University
import com.utsman.universitylist.ui.homepage.component.ToolbarWithSearchBar
import com.utsman.universitylist.ui.homepage.component.UniversityItemContent
import com.utsman.universitylist.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    homeViewModel: HomeViewModel = viewModel()
) {

    val context = LocalContext.current

    val universitiesPaged = homeViewModel.universities.collectAsLazyPagingItems()
    val recentSearch by homeViewModel.recentSearch.collectAsState()
    val isSearchResult by homeViewModel.isSearchResult.collectAsState()

    val lazyListState = rememberLazyListState()
    val isScrollReachTop by remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset == 0 }
    }

    val scope = rememberCoroutineScope()

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
                modifier = Modifier,
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
                                launchCustomTab(context, university.webPage)
                            }
                        }
                    }

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

private fun launchCustomTab(context: Context, url: String) {
    val tabIntent = CustomTabsIntent.Builder().build()
    tabIntent.launchUrl(context, Uri.parse(url))
}