package alektas.gamesheap.searchlist.ui

import alektas.gamesheap.App
import alektas.gamesheap.BuildConfig
import alektas.gamesheap.common.DisposableContainer
import alektas.gamesheap.common.ErrorCode
import alektas.gamesheap.common.Processor
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.data.remote.Response
import alektas.gamesheap.common.domain.Repository
import alektas.gamesheap.gamelist.domain.GamelistAction
import alektas.gamesheap.gamelist.domain.GamelistState
import alektas.gamesheap.gamelist.domain.PartialGamelistState
import alektas.gamesheap.searchlist.domain.SearchlistEvent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel : ViewModel(), Processor<SearchlistEvent> {
    @Inject lateinit var repository: Repository
    private val _state = MutableLiveData(GamelistState())
    val state: LiveData<GamelistState>
        get() = _state
    private val _actions = MutableLiveData<GamelistAction>()
    val actions: LiveData<GamelistAction>
        get() = _actions
    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        App.appComponent.injects(this)

        applyState(PartialGamelistState.Loading)

        disposable += repository.getSearchGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                applyState(
                    when (response) {
                        is Response.DataList -> resolveState(response.games)
                        is Response.Error -> PartialGamelistState.Error(ErrorCode.ERROR_LOADING)
                    }
                )
            }, {
                if (BuildConfig.DEBUG) Log.e("$this", "Failed to load gamelist.", it)
                applyState(PartialGamelistState.Error(ErrorCode.ERROR_LOADING))
            })
    }

    override fun process(event: SearchlistEvent) {
        when (event) {
            is SearchlistEvent.SelectGame -> {
                applyAction(GamelistAction.Navigate(DisposableContainer(event.gameId)))
            }
        }
    }

    private fun applyState(newState: PartialGamelistState) {
        val oldState = _state.value ?: GamelistState()
        _state.value = reduce(oldState, newState)
    }

    private fun applyAction(action: GamelistAction) {
        _actions.value = action
    }

    private fun reduce(oldState: GamelistState, newState: PartialGamelistState): GamelistState {
        return when (newState) {
            is PartialGamelistState.Loading -> oldState.copy(
                isLoading = true,
                showPlaceholder = false,
                errorCode = null
            )
            is PartialGamelistState.Data -> GamelistState(
                games = newState.games
            )
            is PartialGamelistState.Empty -> GamelistState(
                showPlaceholder = true
            )
            is PartialGamelistState.Error -> oldState.copy(
                errorCode = newState.code,
                showPlaceholder = false,
                isLoading = false
            )
        }
    }

    private fun resolveState(games: List<GameInfo>): PartialGamelistState {
        return if (games.isEmpty()) {
            PartialGamelistState.Empty
        } else {
            PartialGamelistState.Data(games)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}
