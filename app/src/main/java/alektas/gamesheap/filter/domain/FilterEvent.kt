package alektas.gamesheap.filter.domain

sealed class FilterEvent {
    data class Close(val platforms: Map<Int, Boolean>, val fromYear: String, val toYear: String) :
        FilterEvent()
    data class FromYear(val year: String) : FilterEvent()
    data class ToYear(val year: String) : FilterEvent()
}
