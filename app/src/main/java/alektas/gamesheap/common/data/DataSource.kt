package alektas.gamesheap.common.data

import alektas.gamesheap.common.data.entities.GameInfo
import alektas.gamesheap.common.data.remote.Response
import alektas.gamesheap.filter.domain.entities.Filter
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