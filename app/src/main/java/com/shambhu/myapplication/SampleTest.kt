fun createPairsFromNumber(number: String): List<String> {
    val pairs = mutableListOf<String>()

    if (number.length < 2) {
        return pairs
    }

    // Filter out 0 from the number
    val filteredNumber = number.filter { it != '0' }

    if (filteredNumber.length < 2) {
        return pairs
    }

    for (i in 0..filteredNumber.length - 2) {
        val pair = filteredNumber.substring(i, i + 2)
        if (pair.length == 2 && pair.all { it.isDigit() && it != '0' }) {
            pairs.add(pair)
        }
    }

    return pairs
}
// Usage example
fun main() {
    print(createPairsFromNumber("9686391100"))

}