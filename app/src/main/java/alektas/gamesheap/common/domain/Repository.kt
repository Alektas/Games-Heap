package alektas.gamesheap.common.domain

import alektas.gamesheap.common.data.entities.GameInfo
import alektas.gamesheap.common.data.remote.Response
import alektas.gamesheap.common.domain.entities.Filter
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
    fun fetchGames(fromStart: Boolean)
    fun searchGames(query: String)
    fun getGames(): Observable<Response>
    fun getSearchGames(): Observable<Response>
    fun applyFilter(filter: Filter)
    fun getFilter(): Observable<Filter>
    fun fetchGame(id: Long): Single<GameInfo>
}