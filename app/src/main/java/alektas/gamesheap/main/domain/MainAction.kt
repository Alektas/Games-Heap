package alektas.gamesheap.main.domain

import alektas.gamesheap.common.DisposableContainer

sealed class MainAction {
    data class Navigate(val destinationContainer: DisposableContainer<Int>) : MainAction() {
        companion object {
            const val SEARCH = 101
        }
    }
}