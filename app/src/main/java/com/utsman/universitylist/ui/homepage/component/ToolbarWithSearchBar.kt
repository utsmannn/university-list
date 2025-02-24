package com.utsman.universitylist.ui.homepage.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.utsman.universitylist.R

/**
 * Composable for the toolbar containing a search bar.
 *
 * @param modifier Modifier for styling.
 * @param showShadow Boolean indicating whether to show a shadow.
 * @param recentSearch List of recent search queries.
 * @param showSearchResult Boolean indicating if search results are being displayed.
 * @param onSearch Callback invoked when a search is performed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarWithSearchBar(
    modifier: Modifier,
    showShadow: Boolean,
    recentSearch: List<String>,
    showSearchResult: Boolean,
    onSearch: (String) -> Unit
) {
    var textFieldQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Animate padding based on whether the search bar is expanded
    val searchBarPadding by animateDpAsState(
        targetValue = if (expanded) 0.dp else 12.dp,
        label = "Search bar padding"
    )

    // Animate shadow elevation based on showShadow flag
    val shadowDpAnimated by animateDpAsState(
        targetValue = if (showShadow) 2.dp else 0.dp,
        label = "animation_drop_shadow"
    )

    // Reset search bar state when search results are not being shown
    LaunchedEffect(key1 = showSearchResult) {
        if (!showSearchResult) {
            textFieldQuery = ""
            expanded = false
        }
    }


    SearchBar(
        modifier = Modifier
            .shadow(shadowDpAnimated, clip = showShadow)
            .background(color = MaterialTheme.colorScheme.background)
            .then(modifier)
            .padding(searchBarPadding),
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldQuery,
                onQueryChange = {
                    textFieldQuery = it
                },
                onSearch = {
                    onSearch.invoke(textFieldQuery)
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(id = R.string.search_bar_hint)) },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            if (expanded) {
                                textFieldQuery = ""
                                onSearch.invoke("")
                            }

                            expanded = !expanded
                        }
                    ) {
                        AnimatedContent(targetState = expanded, label = "icon animated") {
                            if (!it) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        }
    ) {

        // Display recent search queries in the dropdown
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            for (item in recentSearch) {
                ListItem(
                    headlineContent = {
                        Text(item)
                    },
                    supportingContent = {
                        Text("Lorem ipsum ...")
                    },
                    leadingContent = {
                        Icon(Icons.Outlined.Refresh, contentDescription = null)
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .clickable {
                            textFieldQuery = item
                            onSearch.invoke(item)
                            expanded = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * Preview of the [ToolbarWithSearchBar] composable.
 */
@Composable
@Preview(showBackground = true)
fun ToolbarWithSearchBarPreview() {
    ToolbarWithSearchBar(
        modifier = Modifier.fillMaxWidth(),
        showShadow = true,
        recentSearch = listOf("University A", "University B", "University C"),
        showSearchResult = false,
        onSearch = {}
    )
}