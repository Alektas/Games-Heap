package alektas.gamesheap.common.domain.usecases

interface UseCase<REQUEST> {
    fun execute(request: REQUEST)
}