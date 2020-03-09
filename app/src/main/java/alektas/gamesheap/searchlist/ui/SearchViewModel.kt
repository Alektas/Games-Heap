package alektas.gamesheap.searchlist.ui

import alektas.gamesheap.App
import alektas.gamesheap.BuildConfig
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.domain.Repository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel : ViewModel() {
    @Inject lateinit var repository: Repository
    var games: MutableLiveData<List<GameInfo>> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isError: MutableLiveData<Boolean> = MutableLiveData()
    private val disposable: Disposable
    init {
        App.appComponent.injects(this)
        disposable = repository.getSearchGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<GameInfo>>() {
                override fun onComplete() {
                    if (BuildConfig.DEBUG) println("SearchViewModel. Complete loading")
                    isLoading.value = false
                }

                override fun onNext(t: List<GameInfo>) {
                    if (BuildConfig.DEBUG) println("SearchViewModel. On next")
                    games.value = t
                    isLoading.value = false
                }

                override fun onError(e: Throwable) {
                    if (BuildConfig.DEBUG) println("SearchViewModel. Error loading")
                    isLoading.value = false
                    isError.value = true
                }
            })
    }

    fun searchGames(query: String) {
        isLoading.value = true
        repository.searchGames(query)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}
