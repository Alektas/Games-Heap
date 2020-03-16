package alektas.gamesheap.domain

import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.data.remote.Response
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