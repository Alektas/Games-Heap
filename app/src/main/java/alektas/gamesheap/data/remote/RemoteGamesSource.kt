package alektas.gamesheap.data.remote

import alektas.gamesheap.App
import alektas.gamesheap.BuildConfig
import alektas.gamesheap.data.DataSourceAdapter
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.domain.Filter
import alektas.gamesheap.data.remote.api.GamesResponse
import alektas.gamesheap.data.remote.api.GamesApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RemoteGamesSource(private val apiKey: String) : DataSourceAdapter() {
    @Inject
    lateinit var gamesApi: GamesApi
    private val gamesSource: PublishSubject<List<GameInfo>> = PublishSubject.create()
    private val searchSource: PublishSubject<List<GameInfo>> = PublishSubject.create()
    private var filter: Filter = Filter()
    private val fetchLimit = 15
    private var fetchOffset = 0

    init {
        App.appComponent.injects(this)
    }

    override fun fetchGames(fromStart: Boolean) {
        if (fromStart) fetchOffset = 0
        if (BuildConfig.DEBUG) println(
            "Fetching games " +
                    "[offset = $fetchOffset, " +
                    "limit = $fetchLimit, " +
                    "filter = $filter]"
        )
        gamesApi.fetchGames(
            apiKey,
            offset = fetchOffset,
            limit = fetchLimit,
            filter = filter.toString()
        )
            .map { response: GamesResponse ->
                if (BuildConfig.DEBUG) println("Fetched games [response = $response]")
                response.results
            }
            .subscribeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<GameInfo>>() {
                override fun onSuccess(t: List<GameInfo>) {
                    if (BuildConfig.DEBUG) println("Fetching games is successful. Prepare items.")
                    gamesSource.onNext(t)
                    fetchOffset += fetchLimit
                }

                override fun onError(e: Throwable) {
                    if (BuildConfig.DEBUG) println("Fetching games is failed. Throw error.")
                    gamesSource.onError(e)
                }
            })
    }

    override fun searchGames(query: String) {
        if (BuildConfig.DEBUG) println("Searching games [query = $query]")
        gamesApi.searchGames(apiKey, query = query)
            .map { response: GamesResponse ->
                if (BuildConfig.DEBUG) println("Searched games [response = $response]")
                response.results
            }
            .subscribe(object : DisposableSingleObserver<List<GameInfo>>() {
                override fun onSuccess(t: List<GameInfo>) {
                    searchSource.onNext(t)
                }

                override fun onError(e: Throwable) {
                    searchSource.onError(e)
                }
            })
    }

    override fun fetchGame(id: Long): Single<GameInfo> =
        gamesApi.fetchGame(id, apiKey).map { it.results }

    override fun applyFilter(filter: Filter) {
        this.filter = filter
        fetchGames(true)
    }

    override fun getGames(): Observable<List<GameInfo>> {
        return gamesSource.doOnSubscribe {
            if (BuildConfig.DEBUG) println("Subscribe to remote source.")
        }
    }

    override fun getSearchGames(): Observable<List<GameInfo>> {
        return searchSource
    }
}