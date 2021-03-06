package alektas.gamesheap.ui

import alektas.gamesheap.*
import alektas.gamesheap.ui.gamelist.GAMELIST_FRAGMENT_TAG
import alektas.gamesheap.ui.gamelist.GamelistFragment
import alektas.gamesheap.ui.gamelist.GamelistViewModel
import alektas.gamesheap.ui.search.SEARCH_FRAGMENT_TAG
import alektas.gamesheap.ui.search.SearchFragment
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Position of list item from the end from which will started a new portion of items from server
 * on user scrolling.
 */
const val START_FETCHING_OFFSET = 7

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: GamelistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, GamelistFragment.newInstance(),
                GAMELIST_FRAGMENT_TAG
            )
            .commitNow()

        viewModel = ViewModelProviders.of(this).get(GamelistViewModel::class.java)

        handleSearch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleSearch(intent)
    }

    private fun handleSearch(intent: Intent?) {
        if (intent == null) return
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                if (query.isNotEmpty()) startSearching(query)
                Toast.makeText(this, "Search: $query", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startSearching(query: String) {
        var searchFragment = supportFragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG)
        if (searchFragment?.isVisible == true) {
            (searchFragment as SearchFragment).search(query)
        } else {
            searchFragment = SearchFragment.newInstance(query)
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, searchFragment,
                    SEARCH_FRAGMENT_TAG
                )
                .addToBackStack(null)
                .commit()
        }
    }

}
