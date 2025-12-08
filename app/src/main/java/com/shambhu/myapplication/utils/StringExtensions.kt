// utils/StringExtensions.kt
package com.example.numerologyapp.utils

object StringUtils {
    
    /**
     * Parses comma-separated numbers into individual pairs
     * Supports formats like: "13,31,69", "13, 31, 69", "13,31, 69"
     */
    fun parseCommaSeparatedPairs(input: String): List<String> {
        if (input.isBlank()) return emptyList()
        
        return input.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { pair ->
                // Handle individual numbers and ranges
                when {
                    pair.length == 2 && pair.all { it.isDigit() } -> pair
                    pair.length == 4 && pair.contains("-") -> {
                        // Handle ranges like "13-31"
                        val parts = pair.split("-")
                        if (parts.size == 2 && parts[0].length == 2 && parts[1].length == 2) {
                            listOf(parts[0], parts[1])
                        } else {
                            emptyList()
                        }
                    }
                    else -> null
                }
            }
            .filterNotNull()
            .flatMap { if (it is List<*>) it as List<String> else listOf(it as String) }
            .distinct()
    }
    
    /**
     * Validates comma-separated pairs input
     */
    fun isValidCommaSeparatedPairs(input: String): Boolean {
        val pairs = parseCommaSeparatedPairs(input)
        return pairs.isNotEmpty() && pairs.all { it.length == 2 && it.all { char -> char.isDigit() } }
    }
    
    /**
     * Formats pairs for display
     */
    fun formatPairsForDisplay(pairs: List<String>): String {
        return pairs.joinToString(", ")
    }
    
    /**
     * Cleans input by removing extra spaces and invalid characters
     */
    fun cleanCommaSeparatedInput(input: String): String {
        return input.replace("\\s+".toRegex(), " ") // Replace multiple spaces with single space
            .replace("[^0-9,\\-\\s]".toRegex(), "") // Remove non-digit, non-comma, non-dash characters
            .trim()
    }
}