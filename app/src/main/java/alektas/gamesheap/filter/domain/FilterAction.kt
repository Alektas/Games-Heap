package alektas.gamesheap.filter.domain

import alektas.gamesheap.common.domain.entities.DisposableContainer

sealed class FilterAction {
    data class Navigate(val gameIdContainer: DisposableContainer<Long>) : FilterAction()
}