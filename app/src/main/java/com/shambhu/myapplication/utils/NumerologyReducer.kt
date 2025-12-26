package com.shambhu.myapplication.utils


object NumerologyReducer {
    private val MASTER_NUMBERS = setOf(11, 22, 33)
    private val KARMIC_NUMBERS = setOf(13, 14, 16, 19)

    enum class ResultType {
        MASTER,
        KARMIC,
        OTHER
    }

    data class ReductionResult(
        val value: Int,
        val type: ResultType
    )

    data class NumerologyValue(
        val preReduced: String,
        val reduced: Int,
        val masterNumbers: String
    )
    fun reduce(number: Int): ReductionResult {
        var n = number

        while (true) {
            when {
                n in MASTER_NUMBERS ->
                    return ReductionResult(n, ResultType.MASTER)

                n in KARMIC_NUMBERS ->
                    return ReductionResult(n, ResultType.KARMIC)

                n < 10 ->
                    return ReductionResult(n, ResultType.OTHER)

                else ->
                    n = digitSum(n)
            }
        }
    }

    private fun digitSum(number: Int): Int {
        return number.toString().sumOf { it - '0' }
    }
    public fun reduceToSingleDigit(number: Int):Int{
        var sum = number;
        while (sum > 9) {
            sum = sum.toString().map { it.toString().toInt() }.sum()
        }
        return sum
    }

    fun getNumerologyValue(sum: Int):NumerologyValue{
        val result =  reduce(sum)

        return NumerologyValue(
            preReduced = result.value.toString(),
            reduced =  reduceToSingleDigit(sum),
            masterNumbers =  result.type.toString(),
        )
    }
}
