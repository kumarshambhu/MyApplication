import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.CommonUtils.nameToIntArray
import com.shambhu.myapplication.utils.NumerologyCalculationUtils


fun missingNumbers(numList: MutableList<Int>): List<Int> {
    val fullRange = (1..9).toSet()
    val present = numList.toSet()
    return (fullRange - present).toList().sorted()
}

fun main() {
    val fullName = "Shambhu Kumar"

    val nameNumbers = nameToIntArray(fullName)
    val numList: MutableList<Int> = nameNumbers.toMutableList()
    println(numList)

    val dateOfBirth = "17/03/1979"
    val date = CommonUtils.parseDate(dateOfBirth)
    val day = date.dayOfMonth
    val month = date.monthValue
    val year = date.year

    val lifePathNumber = NumerologyCalculationUtils.calculateLifePath(day, month, year)
    numList.add(lifePathNumber)

    val birthPathNumber = NumerologyCalculationUtils.calculateBirthdayNumber(day)
    numList.add(birthPathNumber)

    val uniqueList = numList.distinct().toMutableList()

    println(uniqueList) // [1, 2, 3, 4, 5]

    println(numList)

    val missing = missingNumbers(uniqueList)
    println("Missing numbers: $missing")

}
