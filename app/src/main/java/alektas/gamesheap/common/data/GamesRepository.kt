package alektas.gamesheap.common.data

import alektas.gamesheap.BuildConfig
import alektas.gamesheap.common.data.entities.GameInfo
import alektas.gamesheap.common.data.remote.Response
import alektas.gamesheap.filter.domain.entities.Filter
import alektas.gamesheap.common.domain.Repository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class GamesRepository(var remoteSource: DataSource): Repository {
    private var filter: BehaviorSubject<Filter> = BehaviorSubject.create()

    override fun fetchGame(id: Long): Single<GameInfo> = remoteSource.fetchGame(id)

    override fun fetchGames(fromStart: Boolean) {
        remoteSource.fetchGames(fromStart)
    }

    override fun searchGames(query: String) {
        remoteSource.searchGames(query)
    }

    override fun getGames(): Observable<Response> {
        return remoteSource.getGames()
    }

    override fun getSearchGames(): Observable<Response> {
        return remoteSource.getSearchGames()
    }

    override fun applyFilter(filter: Filter) {
        if (this.filter.value == filter) return
        if (BuildConfig.DEBUG) println("Apply filter: $filter")
        remoteSource.applyFilter(filter)
        this.filter.onNext(filter)
    }

    override fun getFilter(): Observable<Filter> {
        return filter
    }

}