package alektas.gamesheap.common.data.remote

import alektas.gamesheap.common.data.entities.GameInfo

sealed class Response {

    data class DataList(val games: List<GameInfo>): Response()

    data class Error(val message: String) : Response()

}