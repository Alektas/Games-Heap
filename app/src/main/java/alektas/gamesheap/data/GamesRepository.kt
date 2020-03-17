package alektas.gamesheap.data

import alektas.gamesheap.BuildConfig
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.data.remote.Response
import alektas.gamesheap.common.domain.Filter
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
        if (BuildConfig.DEBUG) println("Apply filter: $filter")
        this.filter.onNext(filter)
        remoteSource.applyFilter(filter)
    }

    override fun getFilter(): Observable<Filter> {
        return filter
    }

}