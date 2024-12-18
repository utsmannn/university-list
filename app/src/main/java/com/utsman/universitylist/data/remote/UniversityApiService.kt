package com.utsman.universitylist.data.remote

import com.utsman.universitylist.data.UniversityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for fetching universities from the remote API.
 */
interface UniversityApiService {

    /**
     * Fetches a list of universities filtered by country.
     *
     * @param country The country to filter universities by (default is "indonesia").
     * @return A response of list of [UniversityResponse] objects.
     */
    @GET("/search")
    suspend fun getUniversities(
        @Query("country") country: String = "indonesia"
    ): Response<List<UniversityResponse>>
}