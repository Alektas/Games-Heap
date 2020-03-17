package alektas.gamesheap.data

import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.data.remote.Response
import alektas.gamesheap.common.domain.Filter
import io.reactivex.Observable
import io.reactivex.Single

interface DataSource {
    fun fetchGames(fromStart: Boolean)
    fun searchGames(query: String)
    fun getGames(): Observable<Response>
    fun applyFilter(filter: Filter)
    fun getSearchGames(): Observable<Response>
    fun fetchGame(id: Long): Single<GameInfo>
}