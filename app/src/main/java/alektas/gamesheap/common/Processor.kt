package alektas.gamesheap.common

interface Processor<E> {
    fun process(event: E)
}