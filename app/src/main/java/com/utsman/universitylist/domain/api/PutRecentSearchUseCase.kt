package com.utsman.universitylist.domain.api


/**
 * Interface defining the use case for adding a recent search query.
 */
interface PutRecentSearchUseCase {

    /**
     * Adds a new search query to the recent searches.
     *
     * @param query The search query to add.
     */
    suspend fun putRecentSearch(query: String)
}