package com.utsman.universitylist.ui.homepage.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.utsman.universitylist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarWithSearchBar(
    modifier: Modifier,
    showShadow: Boolean,
    onSearch: (String) -> Unit
) {
    var textFieldQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val searchBarPadding by animateDpAsState(
        targetValue = if (expanded) 0.dp else 12.dp,
        label = "Search bar padding"
    )

    val shadowDpAnimated by animateDpAsState(
        targetValue = if (showShadow) 2.dp else 0.dp,
        label = "animation_drop_shadow"
    )

    Column {
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
                        expanded = false
                        onSearch.invoke(textFieldQuery)
                    },
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = it
                    },
                    placeholder = { Text(stringResource(id = R.string.search_bar_hint)) },
                    leadingIcon = {
                        IconButton(
                            onClick = {
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

            // history search
        }

    }
}