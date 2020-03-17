package alektas.gamesheap.common.domain.usecases

import alektas.gamesheap.common.domain.Repository

class SearchUseCase(val repository: Repository) : UseCase<String> {
    override fun execute(request: String) {
        repository.searchGames(request)
    }
}