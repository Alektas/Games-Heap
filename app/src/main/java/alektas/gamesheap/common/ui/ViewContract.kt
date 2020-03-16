package alektas.gamesheap.common.ui

import io.reactivex.Observable

interface ViewContract<E> {
    fun events(): Observable<E>
}