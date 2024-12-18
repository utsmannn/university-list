package com.utsman.universitylist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.utsman.universitylist.domain.GetRecentSearchUseCase
import com.utsman.universitylist.domain.GetUniversityUseCase
import com.utsman.universitylist.domain.PutRecentSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUniversityUseCase: GetUniversityUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val putRecentSearchUseCase: PutRecentSearchUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _recentSearch = MutableStateFlow(emptyList<String>())

    init {
        viewModelScope.launch {
            getUniversityUseCase.refreshUniversity()
        }

        viewModelScope.launch {
            getRecentSearchUseCase.getRecentSearch()
                .filter { it.isNotEmpty() }
                .collect(_recentSearch)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val universities = _searchQuery
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                getUniversityUseCase.searchUniversityByName(query)
            } else {
                getUniversityUseCase.getUniversities()
            }
        }.cachedIn(viewModelScope)

    val recentSearch: StateFlow<List<String>> get() = _recentSearch

    fun search(query: String) = viewModelScope.launch {
        _searchQuery.update { query }
        if (query.isNotEmpty()) {
            putRecentSearchUseCase.putRecentSearch(query)
        }
    }
}