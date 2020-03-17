package alektas.gamesheap.common.ui

import alektas.gamesheap.*
import alektas.gamesheap.common.domain.MainAction
import alektas.gamesheap.common.domain.MainEvent
import alektas.gamesheap.gamelist.ui.GAMELIST_FRAGMENT_TAG
import alektas.gamesheap.gamelist.ui.GamelistFragment
import alektas.gamesheap.searchlist.ui.SEARCH_FRAGMENT_TAG
import alektas.gamesheap.searchlist.ui.SearchFragment
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bar)

        if (supportFragmentManager.findFragmentByTag(GAMELIST_FRAGMENT_TAG) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, GamelistFragment.newInstance(),
                    GAMELIST_FRAGMENT_TAG
                )
                .commitNow()
        }

        viewModel.actions.observe(this, Observer {
            when (it) {
                is MainAction.Navigate -> it.destinationContainer.value?.let { navigateToShowlist() }
            }
        })
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
                viewModel.process(MainEvent.Search(query))
            }
        }
    }

    private fun navigateToShowlist() {
        var searchFragment = supportFragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG)
        if (searchFragment == null) {
            searchFragment = SearchFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, searchFragment, SEARCH_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
        }
    }

}
