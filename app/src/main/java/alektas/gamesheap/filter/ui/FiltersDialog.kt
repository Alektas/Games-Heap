package alektas.gamesheap.filter.ui

import alektas.gamesheap.R
import alektas.gamesheap.common.ui.ViewContract
import alektas.gamesheap.filter.domain.FilterEvent
import alektas.gamesheap.filter.domain.FilterState
import alektas.gamesheap.filter.domain.entities.*
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.filters_gamelist.*

class FiltersDialog : BottomSheetDialogFragment(), ViewContract<FilterEvent> {
    private val viewModel: FiltersViewModel by viewModels()
    private val disposable = CompositeDisposable()

    companion object {
        const val TAG = "FiltersDialog"
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filters_gamelist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOn(viewModel)
        disposable += events().subscribe { viewModel.process(it) }
    }

    override fun onCancel(dialog: DialogInterface) {
        notifyClose()
        disposable.clear()
        super.onCancel(dialog)
    }

    override fun events(): Observable<FilterEvent> {
        return Observable.merge(
            games_filter_from_year.textChanges()
                .map { FilterEvent.FromYear(it.toString()) },
            games_filter_to_year.textChanges()
                .map { FilterEvent.ToYear(it.toString()) }
        )
    }

    private fun subscribeOn(viewModel: FiltersViewModel) {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
                if (state.isInitSettings) renderFilter(state.filter)
                renderFromYearValidation(state.isValidFromYear)
                renderToYearValidation(state.isValidToYear)
        })
    }

    private fun renderFilter(filter: Filter?) {
        if (filter == null) return

        filter.platforms.forEach { platform ->
            games_filter_platforms.check(
                when (platform) {
                    PLATFORM_PC -> R.id.games_filter_pc
                    PLATFORM_PS4 -> R.id.games_filter_ps4
                    PLATFORM_XONE -> R.id.games_filter_xone
                    PLATFORM_ANDROID -> R.id.games_filter_android
                    PLATFORM_IOS -> R.id.games_filter_ios
                    else -> 0
                }
            )
        }

        games_filter_from_year.setText(filter.fromYear.toString())
        games_filter_to_year.setText(filter.toYear.toString())
    }

    private fun renderFromYearValidation(isValid: Boolean) {
        games_filter_from_year_layout.error =
            if (isValid) null else getString(R.string.error_invalid_from_year)
    }

    private fun renderToYearValidation(isValid: Boolean) {
        games_filter_to_year_layout.error =
            if (isValid) null else getString(R.string.error_invalid_to_year)
    }

    private fun notifyClose() {
        val fromYear = games_filter_from_year.text.toString()
        val toYear = games_filter_to_year.text.toString()

        val platforms = HashMap<Int, Boolean>()
        platforms[PLATFORM_PC] = games_filter_pc.isChecked
        platforms[PLATFORM_PS4] = games_filter_ps4.isChecked
        platforms[PLATFORM_XONE] = games_filter_xone.isChecked
        platforms[PLATFORM_ANDROID] = games_filter_android.isChecked
        platforms[PLATFORM_IOS] = games_filter_ios.isChecked

        viewModel.process(FilterEvent.Close(platforms, fromYear, toYear))
    }

}