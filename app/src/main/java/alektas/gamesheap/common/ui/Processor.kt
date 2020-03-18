package alektas.gamesheap.common.ui

interface Processor<E> {
    fun process(event: E)
}