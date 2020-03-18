package alektas.gamesheap.gamelist.domain

import alektas.gamesheap.common.domain.entities.ErrorCode
import alektas.gamesheap.common.data.entities.GameInfo

data class GamelistState(
    val games: List<GameInfo> = listOf(),
    val isLoading: Boolean = false,
    val showPlaceholder: Boolean = false,
    val errorCode: ErrorCode? = null
)

sealed class PartialGamelistState {
    object Loading : PartialGamelistState()
    data class Data(val games: List<GameInfo>) : PartialGamelistState()
    object Empty : PartialGamelistState()
    data class Error(val code: ErrorCode) : PartialGamelistState()
}
