package alektas.gamesheap.filter.domain

import alektas.gamesheap.filter.domain.entities.Filter

data class FilterState(
    val filter: Filter,
    val isInitSettings: Boolean = false,
    val isValidFromYear: Boolean = true,
    val isValidToYear: Boolean = true
)

sealed class FilterPartitialState {
    data class InitData(val filter: Filter) : FilterPartitialState()
    data class FromYearValidation(val isValid: Boolean) : FilterPartitialState()
    data class ToYearValidation(val isValid: Boolean) : FilterPartitialState()

}