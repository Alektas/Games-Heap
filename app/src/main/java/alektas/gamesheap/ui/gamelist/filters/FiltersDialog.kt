package alektas.gamesheap.ui.gamelist.filters

import alektas.gamesheap.BuildConfig
import alektas.gamesheap.R
import alektas.gamesheap.domain.*
import alektas.gamesheap.utils.StringUtils
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.filters_gamelist.*

class FiltersDialog : BottomSheetDialogFragment() {
    private lateinit var viewModel: FiltersViewModel

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FiltersViewModel::class.java)
        loadFilterValues(viewModel)
    }

    override fun onCancel(dialog: DialogInterface?) {
        applyFilter(viewModel)
        super.onCancel(dialog)
    }

    private fun setupFilters(viewModel: FiltersViewModel) {
        val filterListener = createFilterListener(viewModel)

        games_filter_pc.setOnCheckedChangeListener(filterListener)
        games_filter_ps4.setOnCheckedChangeListener(filterListener)
        games_filter_xone.setOnCheckedChangeListener(filterListener)
        games_filter_android.setOnCheckedChangeListener(filterListener)
        games_filter_ios.setOnCheckedChangeListener(filterListener)

        games_filter_from_year.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onChooseFromYear(StringUtils.parseInt(s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        games_filter_to_year.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onChooseToYear(StringUtils.parseInt(s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun createFilterListener(
        viewModel: FiltersViewModel
    ): CompoundButton.OnCheckedChangeListener {

        return CompoundButton.OnCheckedChangeListener { btn, isChecked ->
            run {
                when (btn.id) {
                    R.id.games_filter_pc -> viewModel.onChoosePlatform(PLATFORM_PC, isChecked)
                    R.id.games_filter_ps4 -> viewModel.onChoosePlatform(PLATFORM_PS4, isChecked)
                    R.id.games_filter_xone -> viewModel.onChoosePlatform(PLATFORM_XONE, isChecked)
                    R.id.games_filter_android -> viewModel.onChoosePlatform(
                        PLATFORM_ANDROID,
                        isChecked
                    )
                    R.id.games_filter_ios -> viewModel.onChoosePlatform(PLATFORM_IOS, isChecked)
                    else -> Toast.makeText(
                        requireContext(),
                        "Invalid platform",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadFilterValues(viewModel: FiltersViewModel) {
        if (BuildConfig.DEBUG) println("FiltersDialog.loadFilterValues: VM = $viewModel")

        viewModel.filter.observe(viewLifecycleOwner, Observer { filter ->
            if (BuildConfig.DEBUG) println("Load filter to dialog: $filter")
            filter.getPlatforms().forEach {
                games_filter_platforms.check(
                    when (it) {
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
        })
    }

    private fun applyFilter(viewModel: FiltersViewModel) {
        val filter = Filter()

        games_filter_platforms.children.forEach {
            if ((it as Chip).isChecked) filter.addPlatform(when (it.id) {
                R.id.games_filter_pc -> PLATFORM_PC
                R.id.games_filter_ps4 -> PLATFORM_PS4
                R.id.games_filter_xone -> PLATFORM_XONE
                R.id.games_filter_android -> PLATFORM_ANDROID
                R.id.games_filter_ios -> PLATFORM_IOS
                else -> 0
            })
        }

        filter.fromYear = StringUtils.parseInt(games_filter_from_year.text.toString())
        filter.toYear = StringUtils.parseInt(games_filter_to_year.text.toString())

        viewModel.applyFilter(filter)
    }
}