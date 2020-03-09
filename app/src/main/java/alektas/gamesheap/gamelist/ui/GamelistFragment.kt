package alektas.gamesheap.gamelist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import alektas.gamesheap.R
import alektas.gamesheap.common.ui.START_FETCHING_OFFSET
import alektas.gamesheap.filter.ui.FiltersDialog
import alektas.gamesheap.gamedetails.ui.GameFragment
import alektas.gamesheap.common.ui.adapters.GamesAdapter
import android.content.Context
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_gamelist.*

const val GAMELIST_FRAGMENT_TAG = "GamelistFragment"

class GamelistFragment : Fragment(), GamesAdapter.ItemListener {
    private lateinit var viewModel: GamelistViewModel
    private lateinit var gamesAdapter: GamesAdapter

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

    override fun onResume() {
        if (gamesAdapter.games.isEmpty()) viewModel.fetchGames(true)
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.content_gamelist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GamelistViewModel::class.java)
        gamesAdapter = GamesAdapter(this)
        subscribeOn(viewModel)
        setupGamelist(requireContext(), viewModel, gamesAdapter)
    }

    private fun setupGamelist(context: Context, viewModel: GamelistViewModel, adapter: GamesAdapter) {
        val linearLayoutManager = LinearLayoutManager(context)
        game_list.layoutManager = linearLayoutManager
        game_list.adapter = adapter
        game_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val itemCount = linearLayoutManager.itemCount
                val lastPos = linearLayoutManager.findLastVisibleItemPosition()

                if ((lastPos + START_FETCHING_OFFSET >= itemCount)) {
                    viewModel.fetchGames()
                }
            }
        })
    }

    private fun subscribeOn(viewModel: GamelistViewModel) {
        viewModel.games.observe(viewLifecycleOwner, Observer {
            gamesAdapter.games = it
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            games_loading_bar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
    }

    override fun onItemSelected(id: Long) {
        if (!isAdded) return
        val f = GameFragment.newInstance(id)
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.content_container, f, GameFragment.TAG)
            .commit()
    }
}