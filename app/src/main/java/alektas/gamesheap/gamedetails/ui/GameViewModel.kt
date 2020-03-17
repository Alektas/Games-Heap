package alektas.gamesheap.gamedetails.ui

import alektas.gamesheap.App
import alektas.gamesheap.BuildConfig
import alektas.gamesheap.common.ErrorCode
import alektas.gamesheap.common.Processor
import alektas.gamesheap.common.domain.Repository
import alektas.gamesheap.gamedetails.domain.GameDetailsEvent
import alektas.gamesheap.gamedetails.domain.GameDetailsState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameViewModel : ViewModel(), Processor<GameDetailsEvent> {
    @Inject
    lateinit var repository: Repository
    private val _state = MutableLiveData<GameDetailsState>(
        GameDetailsState.Empty)
    val state: LiveData<GameDetailsState>
        get() = _state
    private var disposable = CompositeDisposable()

    init {
        App.appComponent.injects(this)
    }

    override fun process(event: GameDetailsEvent) {
        when (event) {
            is GameDetailsEvent.Launch -> fetchGame(event.gameId)
        }
    }

    private fun fetchGame(id: Long) {
        applyState(GameDetailsState.Loading)

        disposable += repository.fetchGame(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                applyState(GameDetailsState.Data(it))
            }, {
                if (BuildConfig.DEBUG) Log.e("$this", "Failed to load game.", it)
                applyState(GameDetailsState.Error(ErrorCode.ERROR_LOADING))
            })
    }

    private fun applyState(newState: GameDetailsState) {
        _state.value = newState
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}