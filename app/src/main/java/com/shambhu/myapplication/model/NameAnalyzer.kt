package com.example.myapplication

class NameAnalyzer {

    data class NameAnalysisResult(
        val mentalInspired: Int = 0,
        val mentalDual: Int = 0,
        val mentalBalanced: Int = 0,
        val physicalInspired: Int = 0,
        val physicalDual: Int = 0,
        val physicalBalanced: Int = 0,
        val emotionalInspired: Int = 0,
        val emotionalDual: Int = 0,
        val emotionalBalanced: Int = 0,
        val intuitiveInspired: Int = 0,
        val intuitiveDual: Int = 0,
        val intuitiveBalanced: Int = 0
    ) {
        val mentalTotal: Int get() = mentalInspired + mentalDual + mentalBalanced
        val physicalTotal: Int get() = physicalInspired + physicalDual + physicalBalanced
        val emotionalTotal: Int get() = emotionalInspired + emotionalDual + emotionalBalanced
        val intuitiveTotal: Int get() = intuitiveInspired + intuitiveDual + intuitiveBalanced

        val totalInspired: Int get() = mentalInspired + physicalInspired + emotionalInspired + intuitiveInspired
        val totalDual: Int get() = mentalDual + physicalDual + emotionalDual + intuitiveDual
        val totalBalanced: Int get() = mentalBalanced + physicalBalanced + emotionalBalanced + intuitiveBalanced

        val grandTotal: Int get() = mentalTotal + physicalTotal + emotionalTotal + intuitiveTotal
    }

    // Letter mappings based on the PDF grid
    private val mentalLetters = mapOf(
        'A' to "Inspired", 'E' to "Inspired",
        'H' to "Dual", 'J' to "Dual", 'N' to "Dual", 'P' to "Dual",
        'G' to "Balanced", 'L' to "Balanced"
    )

    private val physicalLetters = mapOf(
        'W' to "Dual",
        'D' to "Balanced", 'M' to "Balanced"
    )

    private val emotionalLetters = mapOf(
        'O' to "Inspired", 'R' to "Inspired", 'I' to "Inspired", 'Z' to "Inspired",
        'B' to "Dual", 'S' to "Dual", 'T' to "Dual", 'X' to "Dual"
    )

    private val intuitiveLetters = mapOf(
        'K' to "Inspired",
        'F' to "Dual", 'Q' to "Dual", 'U' to "Dual", 'Y' to "Dual",
        'C' to "Balanced", 'V' to "Balanced"
    )

    fun analyzeName(name: String): NameAnalysisResult {
        val cleanedName = name.uppercase().filter { it.isLetter() }

        var mentalInspired = 0
        var mentalDual = 0
        var mentalBalanced = 0
        var physicalInspired = 0
        var physicalDual = 0
        var physicalBalanced = 0
        var emotionalInspired = 0
        var emotionalDual = 0
        var emotionalBalanced = 0
        var intuitiveInspired = 0
        var intuitiveDual = 0
        var intuitiveBalanced = 0

        cleanedName.forEach { char ->
            when {
                mentalLetters.containsKey(char) -> {
                    when (mentalLetters[char]) {
                        "Inspired" -> mentalInspired++
                        "Dual" -> mentalDual++
                        "Balanced" -> mentalBalanced++
                    }
                }
                physicalLetters.containsKey(char) -> {
                    when (physicalLetters[char]) {
                        "Inspired" -> physicalInspired++
                        "Dual" -> physicalDual++
                        "Balanced" -> physicalBalanced++
                    }
                }
                emotionalLetters.containsKey(char) -> {
                    when (emotionalLetters[char]) {
                        "Inspired" -> emotionalInspired++
                        "Dual" -> emotionalDual++
                        "Balanced" -> emotionalBalanced++
                    }
                }
                intuitiveLetters.containsKey(char) -> {
                    when (intuitiveLetters[char]) {
                        "Inspired" -> intuitiveInspired++
                        "Dual" -> intuitiveDual++
                        "Balanced" -> intuitiveBalanced++
                    }
                }
            }
        }

        return NameAnalysisResult(
            mentalInspired = mentalInspired,
            mentalDual = mentalDual,
            mentalBalanced = mentalBalanced,
            physicalInspired = physicalInspired,
            physicalDual = physicalDual,
            physicalBalanced = physicalBalanced,
            emotionalInspired = emotionalInspired,
            emotionalDual = emotionalDual,
            emotionalBalanced = emotionalBalanced,
            intuitiveInspired = intuitiveInspired,
            intuitiveDual = intuitiveDual,
            intuitiveBalanced = intuitiveBalanced
        )
    }
}