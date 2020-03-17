package alektas.gamesheap.searchlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import alektas.gamesheap.R
import alektas.gamesheap.common.ErrorCode
import alektas.gamesheap.common.ui.ViewContract
import alektas.gamesheap.common.ui.adapters.GamesAdapter
import alektas.gamesheap.gamedetails.ui.GameFragment
import alektas.gamesheap.gamelist.domain.GamelistAction
import alektas.gamesheap.searchlist.domain.SearchlistEvent
import android.content.Context
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.content_searchlist.*

const val SEARCH_FRAGMENT_TAG = "SearchFragment"

class SearchFragment : Fragment(), ViewContract<SearchlistEvent> {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var gamesAdapter: GamesAdapter
    private val disposables = CompositeDisposable()

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.content_searchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamesAdapter = GamesAdapter()
        setupGamelist(requireContext(), gamesAdapter)
        disposables += events().subscribe { viewModel.process(it) }
        subscribeOn(viewModel)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    override fun events(): Observable<SearchlistEvent> {
        return gamesAdapter.gameClicks.map { SearchlistEvent.SelectGame(it) }
    }

    private fun setupGamelist(context: Context, adapter: GamesAdapter) {
        val linearLayoutManager = LinearLayoutManager(context)
        game_list.layoutManager = linearLayoutManager
        game_list.adapter = adapter
    }

    private fun subscribeOn(viewModel: SearchViewModel) {
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
