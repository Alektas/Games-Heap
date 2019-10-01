package alektas.gamesheap.data

import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.domain.Filter
import io.reactivex.Observable
import io.reactivex.Single

interface DataSource {
    fun fetchGames(fromStart: Boolean)
    fun searchGames(query: String)
    fun getGames(): Observable<List<GameInfo>>
    fun applyFilter(filter: Filter)
    fun getSearchGames(): Observable<List<GameInfo>>
    fun fetchGame(id: Long): Single<GameInfo>
}