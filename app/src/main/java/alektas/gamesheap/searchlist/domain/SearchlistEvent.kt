package alektas.gamesheap.searchlist.domain

sealed class SearchlistEvent {
    data class SelectGame(val gameId: Long) : SearchlistEvent()
    data class Search(val query: String) : SearchlistEvent()
}