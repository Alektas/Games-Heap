package alektas.gamesheap.gamedetails.domain

import alektas.gamesheap.common.ErrorCode
import alektas.gamesheap.data.entities.GameInfo

sealed class GameDetailsState {
    object Loading : GameDetailsState()
    data class Data(val game: GameInfo) : GameDetailsState()
    object Empty : GameDetailsState()
    data class Error(val code: ErrorCode) : GameDetailsState()
}
