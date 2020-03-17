package alektas.gamesheap.gamelist.domain

import alektas.gamesheap.common.DisposableContainer

sealed class GamelistAction {
    data class Navigate(val gameIdContainer: DisposableContainer<Long>) : GamelistAction()
}