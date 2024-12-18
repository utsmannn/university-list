package com.utsman.universitylist.data

/**
 * Data class representing a University DTO.
 *
 * @property name The name of the university.
 * @property domain The primary domain of the university.
 * @property webPage The official web page URL of the university.
 * @property imageUrl The URL of the university's image.
 */
data class University(
    val name: String,
    val domain: String,
    val webPage: String,
    val imageUrl: String
)