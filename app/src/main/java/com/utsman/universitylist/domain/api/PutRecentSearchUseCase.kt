package com.utsman.universitylist.domain.api

interface PutRecentSearchUseCase {

    suspend fun putRecentSearch(query: String)
}