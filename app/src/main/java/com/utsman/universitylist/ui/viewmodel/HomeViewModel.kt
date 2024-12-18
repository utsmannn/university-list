package com.utsman.universitylist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.utsman.universitylist.domain.GetUniversityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUniversityUseCase: GetUniversityUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            getUniversityUseCase.refreshUniversity()
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

    fun search(query: String) = viewModelScope.launch {
        _searchQuery.update { query }
    }
}