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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RemoteGamesSource(private val apiKey: String) : DataSourceAdapter() {
    @Inject
    lateinit var gamesApi: GamesApi
    private val disposable = CompositeDisposable()
    private val gamesSource: PublishSubject<Response> = PublishSubject.create()
    private val searchSource: PublishSubject<Response> = PublishSubject.create()
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

        disposable += gamesApi.fetchGames(
                apiKey,
                offset = fetchOffset,
                limit = fetchLimit,
                filter = filter.toString()
            )
            .map { response: GamesResponse ->
                if (BuildConfig.DEBUG) println("Fetched games [response = $response]")
                if (isError(response.error)) Response.Error(response.error!!)
                else Response.DataList(response.results ?: listOf())
            }
            .subscribeOn(Schedulers.io())
            .onErrorReturnItem(Response.Error("Failed to fetch games page. Probably problems with Internet connection."))
            .subscribe({
                if (BuildConfig.DEBUG) println("Complete fetching games page.")
                gamesSource.onNext(it)
                fetchOffset += fetchLimit
            }, {
                if (BuildConfig.DEBUG) println("Failed to fetch games page.")
            })
    }

    override fun searchGames(query: String) {
        if (BuildConfig.DEBUG) println("Searching games [query = $query]")

        disposable += gamesApi.searchGames(apiKey, query = query)
            .map { response: GamesResponse ->
                if (BuildConfig.DEBUG) println("Searched games [response = $response]")
                if (isError(response.error)) Response.Error(response.error!!)
                else Response.DataList(response.results ?: listOf())
            }
            .onErrorReturnItem(Response.Error("Failed to search games. Probably problems with Internet connection."))
            .subscribe({
                if (BuildConfig.DEBUG) println("Complete searching games.")
                searchSource.onNext(it)
            }, {
                if (BuildConfig.DEBUG) println("Failed to search games.")
            })
    }

    override fun fetchGame(id: Long): Single<GameInfo> =
        gamesApi.fetchGame(id, apiKey).map { it.results }

    override fun applyFilter(filter: Filter) {
        this.filter = filter
        fetchGames(true)
    }

    override fun getGames(): Observable<Response> {
        return gamesSource
    }

    override fun getSearchGames(): Observable<Response> {
        return searchSource
    }

    private fun isError(message: String?): Boolean {
        return message != null && message != "OK"
    }
}