package com.shambhu.myapplication.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

sealed class OccurrenceCount {
    data class Single(val value: Int) : OccurrenceCount()
    data class Range(val from: Int, val to: Int) : OccurrenceCount()
    data class OrMore(val value: Int) : OccurrenceCount()
    data class Either(val values: List<Int>) : OccurrenceCount()

    fun matches(count: Int): Boolean {
        return when (this) {
            is Single -> count == value
            is Range -> count in from..to
            is OrMore -> count >= value
            is Either -> values.contains(count)
        }
    }
}

class OccurrenceCountAdapter : JsonDeserializer<OccurrenceCount> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): OccurrenceCount {
        return if (json.isJsonPrimitive && json.asJsonPrimitive.isNumber) {
            OccurrenceCount.Single(json.asInt)
        } else {
            val countString = json.asString
            when {
                countString.contains("to") -> {
                    val parts = countString.split(" to ").map { it.trim().toInt() }
                    OccurrenceCount.Range(parts[0], parts[1])
                }
                countString.contains("or more") -> {
                    val value = countString.split(" ")[0].toInt()
                    OccurrenceCount.OrMore(value)
                }
                countString.contains("or") -> {
                    val values = countString.split(" or ").map { it.trim().toInt() }
                    OccurrenceCount.Either(values)
                }
                else -> OccurrenceCount.Single(countString.toInt())
            }
        }
    }
}
