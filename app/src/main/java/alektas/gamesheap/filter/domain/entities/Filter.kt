package alektas.gamesheap.filter.domain.entities

const val PLATFORM_PC = 94
const val PLATFORM_XONE = 145
const val PLATFORM_PS4 = 146
const val PLATFORM_ANDROID = 123
const val PLATFORM_IOS = 96

data class Filter(
    val platforms: List<Int> = listOf(),
    val fromYear: Int = 2019,
    val toYear: Int = 2021
)