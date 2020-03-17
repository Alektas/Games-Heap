package alektas.gamesheap.gamedetails.domain

sealed class GameDetailsEvent {
    data class Launch(val gameId: Long) : GameDetailsEvent()
}
