package alektas.gamesheap.gamedetails.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import alektas.gamesheap.R
import alektas.gamesheap.common.domain.entities.ErrorCode
import alektas.gamesheap.common.data.entities.GameInfo
import alektas.gamesheap.common.data.entities.getFullNames
import alektas.gamesheap.gamedetails.domain.GameDetailsEvent
import alektas.gamesheap.gamedetails.domain.GameDetailsState
import alektas.gamesheap.gamelist.ui.GamelistFragment
import android.content.Intent
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.item_game.item_name

private const val ARG_GAME_ID = "param1"

class GameFragment : Fragment() {
    private var gameId: Long? = null
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameId = it.getLong(ARG_GAME_ID)
        }
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.navigateUpTo(Intent(requireContext(), GamelistFragment::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        subscribeOn(viewModel)

        gameId?.let { viewModel.process(GameDetailsEvent.Launch(it)) }
    }

    private fun subscribeOn(viewModel: GameViewModel) {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is GameDetailsState.Loading -> renderLoading()
                is GameDetailsState.Data -> renderGame(it.game)
                is GameDetailsState.Empty -> renderPlaceholder()
                is GameDetailsState.Error -> renderError(it.code)
            }
        })
    }

    private fun renderLoading() {
        game_loading_bar.visibility = View.VISIBLE
        game_placeholder_text.visibility = View.INVISIBLE
        game_content.visibility = View.INVISIBLE
    }

    private fun renderGame(game: GameInfo) {
        item_name.text = game.name ?: getString(R.string.item_name)
        game_release.text = game.releaseYear?.toString() ?: getString(R.string.release_empty)
        item_platforms.text = game.platforms?.getFullNames() ?: getString(R.string.platforms_empty)
        item_description.text = game.description ?: getString(R.string.desciption_empty)

        item_img.setImageResource(R.drawable.ic_image_black_24dp)
        if (game.image?.url == null) return
        Glide.with(this)
            .load(game.image?.url)
            .optionalCenterCrop()
            .optionalFitCenter()
            .placeholder(R.drawable.ic_image_black_24dp)
            .thumbnail(0.1f)
            .into(item_img)

        game_loading_bar.visibility = View.INVISIBLE
        game_placeholder_text.visibility = View.INVISIBLE
        game_content.visibility = View.VISIBLE
    }

    private fun renderPlaceholder() {
        game_placeholder_text.visibility = View.VISIBLE
        game_loading_bar.visibility = View.INVISIBLE
        game_content.visibility = View.INVISIBLE
    }

    private fun renderError(code: ErrorCode) {
        val msg = when (code) {
            ErrorCode.ERROR_LOADING -> getString(R.string.error_failed_to_load_game)
        }
        error_text.apply {
            text = msg
            visibility = View.VISIBLE
        }
        game_content.visibility = View.INVISIBLE
        game_loading_bar.visibility = View.INVISIBLE
    }

    companion object {
        const val TAG: String = "GameFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param gameId id of the selected game.
         * @return A new instance of fragment GameFragment.
         */
        @JvmStatic
        fun newInstance(gameId: Long) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_GAME_ID, gameId)
                }
            }
    }
}
