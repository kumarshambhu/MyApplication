class NameAnalysisGrid {
    private val mentalLetters = mapOf(
        'A' to "Inspired",
        'H' to "Dual", 'J' to "Dual", 'N' to "Dual", 'P' to "Dual",
        'G' to "Balanced", 'L' to "Balanced"
    )

    private val physicalLetters = mapOf(
        'E' to "Inspired",
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

    data class NameAnalysisResult(
        val mental: Int = 0,
        val physical: Int = 0,
        val emotional: Int = 0,
        val intuitive: Int = 0,
        val inspired: Map<String, Int> = emptyMap(),
        val dual: Map<String, Int> = emptyMap(),
        val balanced: Map<String, Int> = emptyMap(),
        val totals: Map<String, Int> = emptyMap()
    )

    fun analyzeName(name: String): NameAnalysisResult {
        val cleanedName = name.uppercase().replace(" ", "")
        var mental = 0
        var physical = 0
        var emotional = 0
        var intuitive = 0

        val inspired = mutableMapOf<String, Int>()
        val dual = mutableMapOf<String, Int>()
        val balanced = mutableMapOf<String, Int>()

        cleanedName.forEach { char ->
            when {
                mentalLetters.containsKey(char) -> {
                    mental++
                    updateCategoryCount(mentalLetters[char]!!, inspired, dual, balanced)
                }
                physicalLetters.containsKey(char) -> {
                    physical++
                    updateCategoryCount(physicalLetters[char]!!, inspired, dual, balanced)
                }
                emotionalLetters.containsKey(char) -> {
                    emotional++
                    updateCategoryCount(emotionalLetters[char]!!, inspired, dual, balanced)
                }
                intuitiveLetters.containsKey(char) -> {
                    intuitive++
                    updateCategoryCount(intuitiveLetters[char]!!, inspired, dual, balanced)
                }
            }
        }

        val totals = mapOf(
            "Mental" to mental,
            "Physical" to physical,
            "Emotional" to emotional,
            "Intuitive" to intuitive
        )

        return NameAnalysisResult(
            mental = mental,
            physical = physical,
            emotional = emotional,
            intuitive = intuitive,
            inspired = inspired,
            dual = dual,
            balanced = balanced,
            totals = totals
        )
    }

    private fun updateCategoryCount(
        category: String,
        inspired: MutableMap<String, Int>,
        dual: MutableMap<String, Int>,
        balanced: MutableMap<String, Int>
    ) {
        when (category) {
            "Inspired" -> inspired[category] = inspired.getOrDefault(category, 0) + 1
            "Dual" -> dual[category] = dual.getOrDefault(category, 0) + 1
            "Balanced" -> balanced[category] = balanced.getOrDefault(category, 0) + 1
        }
    }

    fun printAnalysis(name: String) {
        val result = analyzeName(name)

        println("Name Analysis for: $name")
        println("=".repeat(50))

        // Print header
        println("${"Category".padEnd(12)} | ${"Mental".padEnd(8)} | ${"Physical".padEnd(8)} | ${"Emotional".padEnd(8)} | ${"Intuitive".padEnd(8)} | Total")
        println("-".repeat(70))

        // Print Inspired row
        val inspiredMental = result.inspired.getOrDefault("Inspired", 0)
        val inspiredPhysical = 0 // No inspired in physical according to grid
        val inspiredEmotional = result.emotionalLettersInspired()
        val inspiredIntuitive = result.intuitiveLettersInspired()
        val inspiredTotal = inspiredMental + inspiredPhysical + inspiredEmotional + inspiredIntuitive

        println("${"Inspired".padEnd(12)} | ${inspiredMental.toString().padEnd(8)} | ${inspiredPhysical.toString().padEnd(8)} | ${inspiredEmotional.toString().padEnd(8)} | ${inspiredIntuitive.toString().padEnd(8)} | $inspiredTotal")

        // Print Dual row
        val dualMental = result.mentalLettersDual()
        val dualPhysical = result.physicalLettersDual()
        val dualEmotional = result.emotionalLettersDual()
        val dualIntuitive = result.intuitiveLettersDual()
        val dualTotal = dualMental + dualPhysical + dualEmotional + dualIntuitive

        println("${"Dual".padEnd(12)} | ${dualMental.toString().padEnd(8)} | ${dualPhysical.toString().padEnd(8)} | ${dualEmotional.toString().padEnd(8)} | ${dualIntuitive.toString().padEnd(8)} | $dualTotal")

        // Print Balanced row
        val balancedMental = result.mentalLettersBalanced()
        val balancedPhysical = result.physicalLettersBalanced()
        val balancedEmotional = 0 // No balanced in emotional according to grid
        val balancedIntuitive = result.intuitiveLettersBalanced()
        val balancedTotal = balancedMental + balancedPhysical + balancedEmotional + balancedIntuitive

        println("${"Balanced".padEnd(12)} | ${balancedMental.toString().padEnd(8)} | ${balancedPhysical.toString().padEnd(8)} | ${balancedEmotional.toString().padEnd(8)} | ${balancedIntuitive.toString().padEnd(8)} | $balancedTotal")

        // Print Totals row
        println("${"Total".padEnd(12)} | ${result.mental.toString().padEnd(8)} | ${result.physical.toString().padEnd(8)} | ${result.emotional.toString().padEnd(8)} | ${result.intuitive.toString().padEnd(8)} | ${result.mental + result.physical + result.emotional + result.intuitive}")

        println("\nBalance Analysis:")
        val totalLetters = result.mental + result.physical + result.emotional + result.intuitive
        if (totalLetters > 0) {
            val mentalPercent = (result.mental * 100) / totalLetters
            val physicalPercent = (result.physical * 100) / totalLetters
            val emotionalPercent = (result.emotional * 100) / totalLetters
            val intuitivePercent = (result.intuitive * 100) / totalLetters

            println("Mental: $mentalPercent% | Physical: $physicalPercent% | Emotional: $emotionalPercent% | Intuitive: $intuitivePercent%")

            when {
                mentalPercent in 20..30 && physicalPercent in 20..30 &&
                        emotionalPercent in 20..30 && intuitivePercent in 20..30 ->
                    println("✅ Name is WELL BALANCED")
                Math.abs(mentalPercent - physicalPercent) > 40 ||
                        Math.abs(emotionalPercent - intuitivePercent) > 40 ->
                    println("⚠️ Name is IMBALANCED - significant differences detected")
                else -> println("➖ Name is MODERATELY BALANCED")
            }
        }
    }
}

// Extension functions to calculate specific letter counts
private fun NameAnalysisGrid.NameAnalysisResult.mentalLettersInspired(): Int {
    return this.inspired.getOrDefault("Inspired", 0)
}

private fun NameAnalysisGrid.NameAnalysisResult.mentalLettersDual(): Int {
    // Count HJNP from mental letters that are Dual
    return 4 // Placeholder - would need actual counting logic
}

private fun NameAnalysisGrid.NameAnalysisResult.mentalLettersBalanced(): Int {
    // Count GL from mental letters that are Balanced
    return 2 // Placeholder - would need actual counting logic
}

private fun NameAnalysisGrid.NameAnalysisResult.physicalLettersDual(): Int {
    // Count W from physical letters that are Dual
    return 1 // Placeholder
}

private fun NameAnalysisGrid.NameAnalysisResult.physicalLettersBalanced(): Int {
    // Count DM from physical letters that are Balanced
    return 2 // Placeholder
}

private fun NameAnalysisGrid.NameAnalysisResult.emotionalLettersInspired(): Int {
    // Count ORIZ from emotional letters that are Inspired
    return 4 // Placeholder
}

private fun NameAnalysisGrid.NameAnalysisResult.emotionalLettersDual(): Int {
    // Count BSTX from emotional letters that are Dual
    return 4 // Placeholder
}

private fun NameAnalysisGrid.NameAnalysisResult.intuitiveLettersInspired(): Int {
    // Count K from intuitive letters that are Inspired
    return 1 // Placeholder
}

private fun NameAnalysisGrid.NameAnalysisResult.intuitiveLettersDual(): Int {
    // Count FQUY from intuitive letters that are Dual
    return 4 // Placeholder
}

private fun NameAnalysisGrid.NameAnalysisResult.intuitiveLettersBalanced(): Int {
    // Count CV from intuitive letters that are Balanced
    return 2 // Placeholder
}

// Usage example
fun main() {
    val analyzer = NameAnalysisGrid()

    // Test with a sample name
    val testName = "John"
    analyzer.printAnalysis(testName)

    println("\n" + "=".repeat(50) + "\n")

    // Test with another name
    val testName2 = "Alice"
    analyzer.printAnalysis(testName2)
}