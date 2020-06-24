package alektas.gamesheap.common.data

import alektas.gamesheap.common.data.entities.GameInfo
import alektas.gamesheap.common.data.remote.Response
import alektas.gamesheap.filter.domain.entities.Filter
import io.reactivex.Observable
import io.reactivex.Single

open class DataSourceAdapter: DataSource {
    override fun getSearchGames(): Observable<Response> {
        return Observable.create {  }
    }

    override fun fetchGame(id: Long): Single<GameInfo> {
        return Single.create {  }
    }

    override fun applyFilter(filter: Filter) {
    }

    override fun fetchGames(fromStart: Boolean) {
    }

    override fun searchGames(query: String) {

    }

    override fun getGames(): Observable<Response> {
        return Observable.create {  }
    }
}