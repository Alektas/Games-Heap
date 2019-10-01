package alektas.gamesheap.domain

import org.junit.Test

import org.junit.Assert.*

class FilterTest {

    @Test
    fun toString_withoutPlatforms() {
        val filter = Filter()
        val expected = "original_release_date:2019-1-1 00:00:00|2022-1-1 00:00:00"
        assertEquals(expected, filter.toString())
    }

    @Test
    fun toString_addDifferentPlatforms() {
        val filter = Filter()
        filter.addPlatforms(PLATFORM_ANDROID, PLATFORM_IOS)
        val expected =
            "original_release_date:2019-1-1 00:00:00|2022-1-1 00:00:00,platforms:$PLATFORM_ANDROID|$PLATFORM_IOS"
        assertEquals(expected, filter.toString())
    }

    @Test
    fun toString_addDuplicatePlatforms() {
        val filter = Filter()
        filter.addPlatforms(PLATFORM_ANDROID, PLATFORM_ANDROID)
        val expected =
            "original_release_date:2019-1-1 00:00:00|2022-1-1 00:00:00,platforms:$PLATFORM_ANDROID|$PLATFORM_ANDROID"
        assertNotEquals(expected, filter.toString())
    }

    @Test
    fun fromYearBiggerThanToYear() {
        val filter = Filter().apply {
            fromYear = 2022
            toYear = 2019
        }
        assertFalse(filter.fromYear > filter.toYear)
    }

    @Test
    fun negativeYear() {
        val filter = Filter().apply {
            fromYear = -101
            toYear = -1
        }
        assertTrue(filter.fromYear > 0 && filter.toYear > 0)
    }

}