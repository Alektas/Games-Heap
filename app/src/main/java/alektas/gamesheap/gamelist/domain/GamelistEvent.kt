package alektas.gamesheap.gamelist.domain

sealed class GamelistEvent {
    object FirstLaunch : GamelistEvent()
    data class SelectGame(val gameId: Long) : GamelistEvent()
    data class Scroll(val loadedGamesCount: Int, val lastVisiblePosition: Int) : GamelistEvent()
}
