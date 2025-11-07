package com.shambhu.myapplication

import com.shambhu.myapplication.JsonConstants.USER_JSON_OBJECT
import org.json.JSONException
import org.json.JSONObject


// Usage
fun main() {
    print(USER_JSON_OBJECT.get("name").toString())

}


object JsonConstants {
    val USER_JSON_OBJECT: JSONObject = JSONObject().apply {
        put("name", "Alice")
        put("age", 25)
        put("email", "alice@example.com")
    }

    val USER_JSON_STRING: String = USER_JSON_OBJECT.toString()
}

