package alektas.gamesheap.common.domain.entities

import alektas.gamesheap.BuildConfig

const val PLATFORM_PC = 94
const val PLATFORM_XONE = 145
const val PLATFORM_PS4 = 146
const val PLATFORM_ANDROID = 123
const val PLATFORM_IOS = 96

class Filter {
    var fromYear: Int = 2019
        set(value) {
            field = if (value < 1970) 1970 else value
            field = if (fromYear > toYear) toYear else fromYear
        }
    var toYear: Int = 2021
        set(value) {
            field = if (value < 1970) 1970 else value
            field = if (toYear < fromYear) fromYear else toYear
        }
    private var platforms: MutableList<Int> = mutableListOf()

    fun setPlatforms(vararg platformId: Int) {
        platforms.clear()
        platforms.addAll(platformId.asList())
    }

    fun addPlatforms(vararg platformId: Int) {
        platformId.forEach { addPlatform(it) }
    }

    fun addPlatform(platformId: Int) {
        if (platformId == 0) return
        if (!platforms.contains(platformId)) platforms.add(platformId)
        if (BuildConfig.DEBUG) println("Filter. Add platform: $platformId; Result: ${toString()}")
    }

    fun removePlatform(platformId: Int) {
        platforms.remove(platformId)
        if (BuildConfig.DEBUG) println("Filter. Remove platform: $platformId; Result: ${toString()}")
    }

    fun getPlatforms(): List<Int> = platforms

    override fun toString(): String {
        val platformsString = if (platforms.isNotEmpty()) {
            ",platforms:${platforms.joinToString(separator = "|")}"
        } else ""
        return "original_release_date:$fromYear-1-1 00:00:00|${toYear + 1}-1-1 00:00:00$platformsString"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Filter) return false
        return this.toString() == other.toString()
    }

    override fun hashCode(): Int {
        var result = fromYear
        result = 31 * result + toYear
        result = 31 * result + platforms.hashCode()
        return result
    }
}