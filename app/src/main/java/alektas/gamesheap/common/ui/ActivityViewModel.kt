package alektas.gamesheap.common.ui

import alektas.gamesheap.App
import alektas.gamesheap.common.DisposableContainer
import alektas.gamesheap.common.Processor
import alektas.gamesheap.common.domain.MainAction
import alektas.gamesheap.common.domain.MainEvent
import alektas.gamesheap.common.domain.Repository
import alektas.gamesheap.common.domain.usecases.SearchUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ActivityViewModel : ViewModel(), Processor<MainEvent> {
    @Inject lateinit var repository: Repository
    private val _actions = MutableLiveData<MainAction>()
    val actions: LiveData<MainAction>
        get() = _actions

    init {
        App.appComponent.injects(this)
    }

    override fun process(event: MainEvent) {
        when (event) {
            is MainEvent.Search -> searchGames(event.query)
        }
    }

    private fun searchGames(query: String) {
        if (query.isEmpty()) return
        applyAction(MainAction.Navigate(DisposableContainer(MainAction.Navigate.SEARCH)))
        SearchUseCase(repository).execute(query)
    }

    private fun applyAction(action: MainAction) {
        _actions.value = action
    }

}
