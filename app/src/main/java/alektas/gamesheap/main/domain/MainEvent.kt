package alektas.gamesheap.main.domain

sealed class MainEvent {
    data class Search(val query: String) : MainEvent()
}