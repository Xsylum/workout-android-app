package com.example.workoutapplication

import com.example.workoutapplication.dataClasses.ExerciseMetricValue
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import java.lang.IllegalArgumentException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun addition_notCorrect() {
        Assert.assertNotEquals(-1, (2 + 2).toLong())
    }

    @Test
    fun exerciseMetricValueFormat_tooLow() {
        Assert.assertThrows(IllegalArgumentException::class.java) { ExerciseMetricValue(-4) }
    }

    @Test
    fun exerciseMetricValueFormat_isCorrect() {
        assertAll( { ExerciseMetricValue(0) } )
    }
}