package alektas.gamesheap.gamelist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import alektas.gamesheap.R
import alektas.gamesheap.common.ErrorCode
import alektas.gamesheap.common.ui.ViewContract
import alektas.gamesheap.filter.ui.FiltersDialog
import alektas.gamesheap.gamedetails.ui.GameFragment
import alektas.gamesheap.common.ui.adapters.GamesAdapter
import alektas.gamesheap.gamelist.domain.GamelistAction
import alektas.gamesheap.gamelist.domain.GamelistEvent
import android.content.Context
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.content_gamelist.*

const val GAMELIST_FRAGMENT_TAG = "GamelistFragment"

class GamelistFragment : Fragment(), ViewContract<GamelistEvent> {
    private val viewModel by viewModels<GamelistViewModel>()
    private lateinit var gamesAdapter: GamesAdapter
    private val disposables = CompositeDisposable()

    companion object {
        @JvmStatic
        fun newInstance() = GamelistFragment()
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.gamelist, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_filters -> {
                if (isAdded) FiltersDialog().show(parentFragmentManager, FiltersDialog.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.content_gamelist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamesAdapter = GamesAdapter()
        setupGamelist(requireContext(), gamesAdapter)
        disposables += events().subscribe { viewModel.process(it) }
        subscribeOn(viewModel)
    }

    override fun onResume() {
        super.onResume()
        if (gamesAdapter.games.isEmpty()) viewModel.process(GamelistEvent.FirstLaunch)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    override fun events(): Observable<GamelistEvent> {
        return Observable.merge(
            gamesAdapter.gameClicks.map { GamelistEvent.SelectGame(it) },
            game_list.scrollEvents().map {
                GamelistEvent.Scroll(
                    it.view.layoutManager!!.itemCount,
                    (it.view.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                )
            }
        )
    }

    private fun setupGamelist(context: Context, adapter: GamesAdapter) {
        val linearLayoutManager = LinearLayoutManager(context)
        game_list.layoutManager = linearLayoutManager
        game_list.adapter = adapter
    }

    private fun subscribeOn(viewModel: GamelistViewModel) {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            gamesAdapter.games = it.games
            games_placeholder.visibility = if (it.showPlaceholder) View.VISIBLE else View.INVISIBLE
            games_loading_bar.visibility = if (it.isLoading) View.VISIBLE else View.INVISIBLE
            games_error_text.visibility = if (it.errorCode != null) View.VISIBLE else View.INVISIBLE
            it.errorCode?.let { code -> showError(code) }
        })

        viewModel.actions.observe(viewLifecycleOwner, Observer {
            when (it) {
                is GamelistAction.Navigate -> it.gameIdContainer.value?.let { id -> showGameDetails(id) }
            }
        })
    }

    private fun showError(code: ErrorCode) {
        val msg = when(code) {
            ErrorCode.ERROR_LOADING -> getString(R.string.error_failed_to_load_gamelist)
        }
        games_error_text.text = msg
    }

    private fun showGameDetails(id: Long) {
        if (!isAdded) return
        val f = GameFragment.newInstance(id)
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.content_container, f, GameFragment.TAG)
            .commit()
    }

}
