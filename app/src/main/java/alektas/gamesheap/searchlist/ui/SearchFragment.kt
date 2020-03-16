package alektas.gamesheap.searchlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import alektas.gamesheap.R
import alektas.gamesheap.common.ui.adapters.GamesAdapter
import alektas.gamesheap.gamedetails.ui.GameFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_searchlist.*

const val SEARCH_FRAGMENT_TAG = "SearchFragment"
private const val ARG_QUERY = "query"

class SearchFragment : Fragment(), GamesAdapter.ItemListener {
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
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        gamesAdapter = GamesAdapter()

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
        if (!isAdded) return
        val f = GameFragment.newInstance(id)
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.content_container, f, GameFragment.TAG)
            .commit()
    }

}
