package com.utsman.universitylist.ui.homepage

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.compose.collectAsLazyPagingItems
import com.utsman.universitylist.data.University
import com.utsman.universitylist.ui.homepage.component.ToolbarWithSearchBar
import com.utsman.universitylist.ui.homepage.component.UniversityItemContent
import com.utsman.universitylist.ui.viewmodel.HomeViewModel

@Composable
fun HomePage(
    homeViewModel: HomeViewModel = viewModel()
) {

    val context = LocalContext.current

    val universitiesPaged = homeViewModel.universities.collectAsLazyPagingItems()
    val recentSearch by homeViewModel.recentSearch.collectAsState()

    val lazyListState = rememberLazyListState()
    val isScrollReachTop by remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset == 0 }
    }

    Scaffold(
        topBar = {
            ToolbarWithSearchBar(
                modifier = Modifier,
                showShadow = !isScrollReachTop,
                recentSearch = recentSearch,
                onSearch = {
                    homeViewModel.search(it)
                }
            )
        },
        content = { innerPadding ->
            ConstraintLayout(
                modifier = Modifier.padding(innerPadding)
            ) {

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
                }

            }
        }
    )
}

private fun launchCustomTab(context: Context, url: String) {
    val tabIntent = CustomTabsIntent.Builder().build()
    tabIntent.launchUrl(context, Uri.parse(url))
}