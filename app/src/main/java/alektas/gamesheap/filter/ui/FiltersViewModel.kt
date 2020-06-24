package alektas.gamesheap.filter.ui

import alektas.gamesheap.App
import alektas.gamesheap.BuildConfig
import alektas.gamesheap.common.domain.Repository
import alektas.gamesheap.common.ui.Processor
import alektas.gamesheap.filter.domain.FilterAction
import alektas.gamesheap.filter.domain.FilterEvent
import alektas.gamesheap.filter.domain.FilterPartitialState
import alektas.gamesheap.filter.domain.FilterState
import alektas.gamesheap.filter.domain.entities.*
import alektas.gamesheap.common.utils.Parser
import alektas.gamesheap.common.utils.Validator
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class FiltersViewModel : ViewModel(), Processor<FilterEvent> {
    @Inject
    lateinit var repository: Repository
    private val _state = MutableLiveData(FilterState(Filter()))
    val state: LiveData<FilterState>
        get() = _state
    private val _actions = MutableLiveData<FilterAction>()
    val actions: LiveData<FilterAction>
        get() = _actions
    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        App.appComponent.injects(this)

        disposable += repository.getFilter()
            .first(Filter())
            .map { FilterPartitialState.InitData(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                applyState(it)
            }, {
                if (BuildConfig.DEBUG) Log.e("$this", "Failed to apply filter. Set default.", it)
                // Don't need to show error, instead use previous state (so do nothing)
            })
    }

    override fun process(event: FilterEvent) {
        val oldState = state.value ?: FilterState(Filter())
        when (event) {
            is FilterEvent.Close -> applyFilter(event)
            is FilterEvent.FromYear -> {
                val newState = FilterPartitialState.FromYearValidation(validate(oldState, event))
                applyState(newState)
            }
            is FilterEvent.ToYear -> {
                val newState = FilterPartitialState.ToYearValidation(validate(oldState, event))
                applyState(newState)
            }
        }
    }

    private fun applyState(partitialState: FilterPartitialState) {
        val oldState = state.value ?: FilterState(Filter())
        _state.value = reduce(oldState, partitialState)
    }

    private fun applyFilter(event: FilterEvent.Close) {
        val fromYear = Parser.parseInt(event.fromYear)
        val toYear = Parser.parseInt(event.toYear)
        if (!Validator.isValidPeriod(fromYear, toYear)) return

        val platforms = event.platforms.filterValues { it }.keys.toList()
        val filter = Filter(platforms, fromYear, toYear)

        repository.applyFilter(filter)
    }

    private fun reduce(oldState: FilterState, newState: FilterPartitialState): FilterState {
        return when (newState) {
            is FilterPartitialState.InitData -> oldState.copy(
                filter = newState.filter,
                isInitSettings = true
            )
            is FilterPartitialState.FromYearValidation -> oldState.copy(
                isInitSettings = false,
                isValidFromYear = newState.isValid,
                isValidToYear = oldState.isValidFromYear
            )
            is FilterPartitialState.ToYearValidation -> oldState.copy(
                isInitSettings = false,
                isValidToYear = newState.isValid,
                isValidFromYear = oldState.isValidFromYear
            )
        }
    }

    private fun validate(oldState: FilterState, event: FilterEvent.FromYear): Boolean {
        val fromYear = Parser.parseInt(event.year)
        val toYear = oldState.filter?.toYear ?: return Validator.isValidYear(fromYear)
        return Validator.isValidPeriod(fromYear, toYear)
    }

    private fun validate(oldState: FilterState, event: FilterEvent.ToYear): Boolean {
        val toYear = Parser.parseInt(event.year)
        val fromYear = oldState.filter?.fromYear ?: return Validator.isValidYear(toYear)
        return Validator.isValidPeriod(fromYear, toYear)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

}