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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.utsman.universitylist.data.University
import com.utsman.universitylist.ui.homepage.component.ToolbarWithSearchBar
import com.utsman.universitylist.ui.homepage.component.UniversityItemContent

@Composable
fun HomePage() {

    val dummyListUniversity = remember {
        listOf(
            University("Univ bagus", "bagus.com","https://bagus.com", "https://placehold.co/600x400/orange/white"),
            University("Univ keren", "keren.com", "https://keren.com", "https://placehold.co/600x400/orange/blue"),
            University("Univ cakep", "cakep.com", "https://keren.com", "https://placehold.co/600x400/orange/red"),
            University( "Univ bagus 1", "bagus.com","https://bagus.com", "https://placehold.co/600x400/orange/yellow"),
            University("Univ keren 1", "keren.com", "https://keren.com", "https://placehold.co/600x400/orange/pink"),
            University("Univ cakep 1", "cakep.com", "https://keren.com", "https://placehold.co/600x400/orange/orange"),
            University("Univ bagus 2", "bagus.com","https://bagus.com", "https://placehold.co/600x400/orange/white"),
            University( "Univ keren 2", "keren.com", "https://keren.com", "https://placehold.co/600x400/orange/blue"),
            University("Univ cakep 2", "cakep.com", "https://keren.com", "https://placehold.co/600x400/orange/red")
        )
    }

    val context = LocalContext.current

    val lazyListState = rememberLazyListState()
    val isScrollReachTop by remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset == 0 }
    }

    Scaffold(
        topBar = {
            ToolbarWithSearchBar(
                modifier = Modifier,
                showShadow = !isScrollReachTop,
                onSearch = {
                    // search here
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
                    items(dummyListUniversity) { item ->
                        UniversityItemContent(
                            modifier = Modifier.fillMaxWidth(),
                            university = item,
                            onClick = {
                                launchCustomTab(context, item.webPage)
                            }
                        )
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