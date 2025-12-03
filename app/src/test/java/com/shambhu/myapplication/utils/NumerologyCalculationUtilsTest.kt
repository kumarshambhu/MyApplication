package com.shambhu.myapplication.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class NumerologyCalculationUtilsTest {

    @Test
    fun testCalculateNameAnalysisGrid() {
        val result = NumerologyCalculationUtils.calculateNameAnalysisGrid("Jules")
        assertEquals(0, result.mentalInspired)
        assertEquals(1, result.physicalInspired)
        assertEquals(0, result.emotionalInspired)
        assertEquals(0, result.intuitiveInspired)
        assertEquals(1, result.mentalDual)
        assertEquals(0, result.physicalDual)
        assertEquals(1, result.emotionalDual)
        assertEquals(1, result.intuitiveDual)
        assertEquals(1, result.mentalBalanced)
        assertEquals(0, result.physicalBalanced)
        assertEquals(0, result.emotionalBalanced)
        assertEquals(0, result.intuitiveBalanced)
    }
}
