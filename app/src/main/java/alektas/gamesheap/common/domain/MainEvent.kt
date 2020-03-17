package alektas.gamesheap.common.domain

sealed class MainEvent {
    data class Search(val query: String) : MainEvent()
}