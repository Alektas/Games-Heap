package alektas.gamesheap.ui.gamelist.game

import alektas.gamesheap.App
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.domain.Repository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameViewModel: ViewModel() {
    @Inject lateinit var repository: Repository
    var game = MutableLiveData<GameInfo>()
    private var disposable: Disposable? = null

    init {
        App.appComponent.injects(this)
    }

    fun fetchGame(id: Long)  {
        disposable = repository.fetchGame(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { game.value = it },
                { println("GameViewModel. Error on loading game: $it") })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

}