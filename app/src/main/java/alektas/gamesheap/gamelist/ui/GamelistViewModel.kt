package alektas.gamesheap.gamelist.ui

import alektas.gamesheap.App
import alektas.gamesheap.BuildConfig
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.domain.Filter
import alektas.gamesheap.domain.Repository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GamelistViewModel : ViewModel() {
    @Inject lateinit var repository: Repository
    var games: MutableLiveData<List<GameInfo>> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isError: MutableLiveData<Boolean> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var isLoadingCompleted = false
    private val gamelist: MutableList<GameInfo> = mutableListOf()

    init {
        App.appComponent.injects(this)

        disposable.add(repository.getGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<GameInfo>>() {
            override fun onComplete() {
                if (BuildConfig.DEBUG) println("$this. Complete loading.")
                isLoading.value = false
                isLoadingCompleted = true
            }

            override fun onNext(t: List<GameInfo>) {
                isLoading.value = false

                if (t.isEmpty()) {
                    if (BuildConfig.DEBUG) println("$this. Complete loading.")
                    isLoadingCompleted = true
                    return
                }

                isLoadingCompleted = false
                if (BuildConfig.DEBUG) println("$this. Get items.")
                gamelist.addAll(t)
                games.value = gamelist
            }

            override fun onError(e: Throwable) {
                if (BuildConfig.DEBUG) println("$this. Error loading.")
                isLoading.value = false
                isError.value = true
            }
        }))

        disposable.add(repository.getFilter()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<Filter>() {
                override fun onNext(t: Filter) {
                    gamelist.clear()
                }

                override fun onComplete() { }

                override fun onError(e: Throwable) { }
            }))

    }

    fun fetchGames(fromStart: Boolean = false) {
        if (isLoadingCompleted || isLoading.value == true) return
        isLoading.value = true
        repository.fetchGames(fromStart)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
        if (BuildConfig.DEBUG) println("GamelistViewModel. Unsubscribe from DB games.")
    }

}