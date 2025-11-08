package com.shambhu.myapplication.utils

import org.json.JSONObject

class Constants {
    companion object {
        const val PREFERENCE_FULL_NAME = "full_name"
        const val PREFERENCE_DATE_OF_BIRTH = "date_of_birth"
        const val PREFERENCE_TIME_OF_BIRTH = "time_of_birth"
        const val PREFERENCE_PLACE_OF_BIRTH = "place_of_birth"

        const val ARG_DOB = "dob"
        const val ARG_FULL_NAME = "fullName"
        const val TAB_KEY_PERSONALITY = "Personality"
        const val TAB_KEY_BIRTH_NUMBER = "Birth Number"
        const val TAB_KEY_SOUL_URGE = "Soul Urge"
        const val TAB_KEY_LIFE_PATH = "Life Path"
        const val TAB_KEY_EXPRESSION = "Expression"
        const val TAB_KEY_PERSONAL_YEAR = "Personal Year"
        const val TAB_KEY_PERSONAL_MONTH = "Personal Month"
        const val TAB_KEY_KARMIC_NUMBER = "Karmic Number"
        const val TAB_KEY_CHALLENGE_NUMBER = "Challenge Number"
        const val TAB_KEY_PINNACLE_NUMBER = "Pinnacle Number"
        const val TAB_KEY_NUMEROLOGY_PLAIN = "Numerology Plain"

        const val MY_DATA = "<ul><li><b>Responsibility & Self-Control:</b> You’re encouraged to face your responsibilities head-on and avoid escapism. You have the potential to solve problems creatively if you apply yourself.</li><li><b>Confidence & Independence:</b> A dynamic, risk-taking individual who prefers forging your own path. You value originality and admire resilience in others.</li></ul>"

        val LETTER_VALUES = mapOf(
            'A' to 1, 'B' to 2, 'C' to 3, 'D' to 4, 'E' to 5, 'F' to 6, 'G' to 7, 'H' to 8, 'I' to 9,
            'J' to 1, 'K' to 2, 'L' to 3, 'M' to 4, 'N' to 5, 'O' to 6, 'P' to 7, 'Q' to 8, 'R' to 9,
            'S' to 1, 'T' to 2, 'U' to 3, 'V' to 4, 'W' to 5, 'X' to 6, 'Y' to 7, 'Z' to 8
        )

    }
}
