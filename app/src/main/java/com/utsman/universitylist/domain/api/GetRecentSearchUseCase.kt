package com.utsman.universitylist.domain.api

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the use case for retrieving recent search queries.
 */
interface GetRecentSearchUseCase {

    /**
     * Retrieves a Flow of recent search queries.
     *
     * @return Flow emitting a list of recent search strings.
     */
    fun getRecentSearch(): Flow<List<String>>
}