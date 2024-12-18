package com.utsman.universitylist.data

fun UniversityResponse.mapToEntity(): UniversityEntity {
    return UniversityEntity(
        name = this.name.orEmpty(),
        domain = this.domains?.firstOrNull().orEmpty(),
        webPage = this.webPages?.firstOrNull().orEmpty(),
        imageUrl = generateImageUrl(this.name.orEmpty())
    )
}

fun UniversityEntity.mapToDto(): University {
    return University(name, domain, webPage, imageUrl)
}


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

fun generateImageUrl(text: String): String {
    val simplifyText = text.split(" ")
        .mapNotNull { it.firstOrNull() }
        .joinToString(" ") {
            it.uppercase()
        }

    val background = generateColorCombinations().random()

    return "https://placehold.co/600x400/$background?text=$simplifyText"
}