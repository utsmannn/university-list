package com.utsman.universitylist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import com.utsman.universitylist.domain.api.GetUniversityUseCase
import com.utsman.universitylist.domain.api.PutRecentSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for the HomePage UI.
 * Manages the state and handles user interactions related to university data and search.
 *
 * @property getUniversityUseCase Use case for retrieving universities.
 * @property getRecentSearchUseCase Use case for retrieving recent searches.
 * @property putRecentSearchUseCase Use case for adding a recent search.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUniversityUseCase: GetUniversityUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val putRecentSearchUseCase: PutRecentSearchUseCase
) : ViewModel() {

    // MutableStateFlow for managing the current search query
    private val _searchQuery = MutableStateFlow("")

    // MutableStateFlow for managing the list of recent searches
    private val _recentSearch = MutableStateFlow(emptyList<String>())

    // MutableStateFlow indicating whether the current view is showing search results
    private val _isSearchResult = MutableStateFlow(false)

    // MutableStateFlow for managing error messages.
    private val _errorMessage = MutableStateFlow<String?>(null)

    /**
     * [CoroutineExceptionHandler] to handle uncaught exceptions in coroutines.
     * Updates the [_errorMessage] StateFlow with the exception message.
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val message = throwable.message ?: "Error"
        _errorMessage.value = message
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            // Refresh university data on initialization
            val result = getUniversityUseCase.refreshUniversity()
            result.fold(
                onSuccess = {
                    _errorMessage.value = null
                },
                onFailure = {
                    val message = it.message ?: "Error"
                    _errorMessage.value = message
                }
            )
        }

        viewModelScope.launch {
            // Collect recent searches and update the state
            getRecentSearchUseCase.getRecentSearch()
                .filter { it.isNotEmpty() }
                .stateIn(viewModelScope)
                .collect(_recentSearch)
        }
    }

    /**
     * Flow of universities, either all or filtered by search query.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val universities = _searchQuery
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                getUniversityUseCase.searchUniversityByName(query)
                    .catch { e ->
                        _errorMessage.value =
                            "Failure: ${e.localizedMessage}"
                        emit(PagingData.empty())
                    }
            } else {
                getUniversityUseCase.getUniversities()
                    .catch { e ->
                        _errorMessage.value =
                            "Failure: ${e.localizedMessage}"
                        emit(PagingData.empty())
                    }
            }
        }
        .cachedIn(viewModelScope)

    // Publicly exposed StateFlow for recent searches
    val recentSearch: StateFlow<List<String>> get() = _recentSearch

    // Publicly exposed StateFlow indicating if search results are displayed
    val isSearchResult: StateFlow<Boolean> get() = _isSearchResult

    // Publicly exposed StateFlow for error messages. The UI can observe this to display error notifications.
    val errorMessage: StateFlow<String?> get() = _errorMessage

    /**
     * Updates the search query and adds it to recent searches.
     *
     * @param query The new search query.
     */
    fun search(query: String) = viewModelScope.launch {
        _searchQuery.update { query }
        putRecentSearchUseCase.putRecentSearch(query)
        if (query.isNotEmpty()) {
            putRecentSearchUseCase.putRecentSearch(query)
        }
    }

    /**
     * Sets the flag indicating whether search results are being displayed.
     *
     * @param isSearchResult The new value for the search result flag.
     */
    fun setIsSearchResult(isSearchResult: Boolean) {
        _isSearchResult.value = isSearchResult
    }
}