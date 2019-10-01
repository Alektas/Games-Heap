package alektas.gamesheap.data

import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.domain.Filter
import io.reactivex.Observable
import io.reactivex.Single

open class DataSourceAdapter: DataSource {
    override fun getSearchGames(): Observable<List<GameInfo>> {
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

    override fun getGames(): Observable<List<GameInfo>> {
        return Observable.create {  }
    }
}