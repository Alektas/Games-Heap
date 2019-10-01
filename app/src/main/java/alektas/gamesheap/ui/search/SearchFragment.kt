package alektas.gamesheap.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import alektas.gamesheap.R
import alektas.gamesheap.ui.ItemListener
import alektas.gamesheap.ui.gamelist.GamesAdapter
import alektas.gamesheap.ui.gamelist.game.GameFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_searchlist.*

const val SEARCH_FRAGMENT_TAG = "SearchFragment"
private const val ARG_QUERY = "query"

class SearchFragment : Fragment(), ItemListener {
    private lateinit var viewModel: SearchViewModel
    private lateinit var gamesAdapter: GamesAdapter
    private var query: String? = null
    set(value) {
        arguments?.putString(ARG_QUERY, value)
        field = value
    }

    companion object {
        @JvmStatic
        fun newInstance(query: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(ARG_QUERY)
        }
    }

    override fun onResume() {
        query?.let { viewModel.searchGames(it) }
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.content_searchlist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        gamesAdapter = GamesAdapter(this)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        game_list.layoutManager = linearLayoutManager
        game_list.adapter = gamesAdapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            games_loading_bar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        viewModel.games.observe(viewLifecycleOwner, Observer {
            gamesAdapter.games = it
        })
    }

    fun search(query: String) {
        this.query = query
        viewModel.searchGames(query)
    }

    override fun onItemSelected(id: Long) {
        val f = GameFragment.newInstance(id)
        fragmentManager?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.content_container, f, GameFragment.TAG)
            ?.commit()
    }

}
