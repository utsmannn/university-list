package com.utsman.universitylist.utils

import com.utsman.universitylist.data.UniversityResponse
import com.utsman.universitylist.data.remote.UniversityApiService

/**
 * Fake implementation of UniversityApiService for testing and preview purposes.
 */
class FakeUniversityApiService(private val universities: List<UniversityResponse> = emptyList()) : UniversityApiService {
    override suspend fun getUniversities(country: String): List<UniversityResponse> {
        // Simulate network delay if needed
        // delay(1000)
        return universities
    }
}