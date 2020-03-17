package alektas.gamesheap.filter.ui

import alektas.gamesheap.App
import alektas.gamesheap.common.domain.Filter
import alektas.gamesheap.common.domain.Repository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class FiltersViewModel : ViewModel() {
    @Inject lateinit var repository: Repository
    var filter = MutableLiveData<Filter>()
    private var disposable: Disposable

    init {
        App.appComponent.injects(this)
        filter.value = Filter()
        disposable = repository.getFilter()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<Filter>() {
                override fun onNext(t: Filter) {
                    filter.value = t
                }

                override fun onError(e: Throwable) { }

                override fun onComplete() { }
            })
    }

    fun onChoosePlatform(platformId: Int, isChoosed: Boolean) {
        val f = filter.value ?: return
        if (isChoosed) f.addPlatform(platformId)
        else f.removePlatform(platformId)
        repository.applyFilter(f)
    }

    fun onChooseFromYear(fromYear: Int) {
        val f = filter.value ?: return
        f.fromYear = fromYear
        repository.applyFilter(f)
    }

    fun onChooseToYear(toYear: Int) {
        val f = filter.value ?: return
        f.toYear = toYear
        repository.applyFilter(f)
    }

    fun applyFilter(filter: Filter) {
        if (this.filter.value?.equals(filter) == true) return
        repository.applyFilter(filter)
    }

}