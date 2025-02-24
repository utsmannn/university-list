package com.utsman.universitylist.data


/**
 * Extension function to map [UniversityResponse] to [UniversityEntity].
 * Ensures non-null values by providing default empty strings where necessary.
 */
fun UniversityResponse.mapToEntity(): UniversityEntity {
    return UniversityEntity(
        name = this.name.orEmpty(),
        domain = this.domains?.firstOrNull().orEmpty(),
        webPage = this.webPages?.firstOrNull().orEmpty(),
        imageUrl = generateImageUrl(this.name.orEmpty())
    )
}

/**
 * Extension function to map [UniversityEntity] to [University] DTO.
 */
fun UniversityEntity.mapToDto(): University {
    return University(name, domain, webPage, imageUrl)
}

/**
 * Generates a list of color combinations for background and text
 * for image generator.
 */
fun generateColorCombinations(): List<String> {
    val colors = listOf(
        "red", "blue", "green", "yellow", "orange", "purple", "pink", "black", "white", "gray"
    )

    val combinations = mutableListOf<String>()
    for (background in colors) {
        for (text in colors) {
            if (background != text) {
                combinations.add("$background/$text")
            }
        }
    }
    return combinations
}


/**
 * Generates a placeholder image URL with randomized background colors and text.
 */
fun generateImageUrl(text: String): String {
    val simplifyText = text.split(" ")
        .mapNotNull { it.firstOrNull() }
        .joinToString(" ") {
            it.uppercase()
        }

    val background = generateColorCombinations().random()

    return "https://placehold.co/600x400/$background?text=$simplifyText"
}