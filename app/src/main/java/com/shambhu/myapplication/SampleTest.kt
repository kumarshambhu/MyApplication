import com.shambhu.myapplication.utils.Constants

fun retainOnlyVowels(input: String): String {
    return input.replace(Regex("[^aeiouAEIOU]"), "")
}

fun removeVowels(input: String): String {
    return input.replace(Regex("[aeiouAEIOU]"), "")
}

val LETTER_VALUES = mapOf(
    'A' to 1, 'B' to 2, 'C' to 3, 'D' to 4, 'E' to 5, 'F' to 6, 'G' to 7, 'H' to 8, 'I' to 9,
    'J' to 1, 'K' to 2, 'L' to 3, 'M' to 4, 'N' to 5, 'O' to 6, 'P' to 7, 'Q' to 8, 'R' to 9,
    'S' to 1, 'T' to 2, 'U' to 3, 'V' to 4, 'W' to 5, 'X' to 6, 'Y' to 7, 'Z' to 8
)

fun main() {
    val fullName = "Shambhu Kumar"
    println(retainOnlyVowels(fullName))  // Output: oiaoi

    println(removeVowels(fullName))

    val personalityName = removeVowels(fullName)
    val soulName = retainOnlyVowels(fullName)

    val personalityCleanedName = removeVowels(fullName).uppercase().filter { it in Constants.Companion.LETTER_VALUES }
    val soulCleanedName = retainOnlyVowels(fullName).uppercase().filter { it in Constants.Companion.LETTER_VALUES }
    val destinyCleanedName = fullName.uppercase().filter { it in Constants.Companion.LETTER_VALUES }

    val personalitySum = personalityCleanedName.map { Constants.Companion.LETTER_VALUES[it] ?: 0 }.sum()
    val soulSum = soulCleanedName.map { Constants.Companion.LETTER_VALUES[it] ?: 0 }.sum()
    val destinySum = destinyCleanedName.map { Constants.Companion.LETTER_VALUES[it] ?: 0 }.sum()

    print("${personalitySum} ${soulSum} ${destinySum}")
    print("${personalityCleanedName} ${soulCleanedName} ${destinyCleanedName}")
}