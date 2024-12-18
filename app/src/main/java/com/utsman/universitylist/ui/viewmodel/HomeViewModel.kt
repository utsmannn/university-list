package com.utsman.universitylist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import com.utsman.universitylist.domain.api.GetUniversityUseCase
import com.utsman.universitylist.domain.api.PutRecentSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
    private val _isSearchResult = MutableStateFlow(false)

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
    val isSearchResult: StateFlow<Boolean> get() = _isSearchResult

    fun search(query: String) = viewModelScope.launch {
        _searchQuery.update { query }
        putRecentSearchUseCase.putRecentSearch(query)
        if (query.isNotEmpty()) {
            putRecentSearchUseCase.putRecentSearch(query)
        }
    }

    fun setIsSearchResult(isSearchResult: Boolean) {
        _isSearchResult.value = isSearchResult
    }
}