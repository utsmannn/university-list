package com.utsman.universitylist.domain.api

import kotlinx.coroutines.flow.Flow

interface GetRecentSearchUseCase {

    fun getRecentSearch(): Flow<List<String>>
}