package com.shambhu.myapplication

import org.json.JSONException
import org.json.JSONObject

// Extension function for easy conversion
fun String.toJSONObject(): JSONObject? = try {JSONObject(this)
} catch (e: JSONException) {
    // Specifically catch JSONException for clarity and type safety
    e.printStackTrace() // Optional: log the error during development
    null
}

// Usage
fun main() {
    val jsonString = """{"key": "value", "number": 123}"""

    val jsonObject = jsonString.toJSONObject()
    println(jsonObject?.optString("key")) // "value"

    // Safe access with elvis operator
    val value = jsonString.toJSONObject()?.optString("key", "default")
    println(value) // "value"

    // Example with invalid JSON
    val invalidJsonString = """{"key": "value",}""" // Invalid trailing comma
    val invalidJsonObject = invalidJsonString.toJSONObject()
    println(invalidJsonObject) // null
}
