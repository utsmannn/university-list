package com.utsman.universitylist.data.remote

import com.utsman.universitylist.data.UniversityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UniversityApiService {
    @GET("/search")
    suspend fun getUniversities(
        @Query("country") country: String = "indonesia"
    ): List<UniversityResponse>
}