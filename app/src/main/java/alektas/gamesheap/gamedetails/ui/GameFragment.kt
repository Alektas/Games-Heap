package alektas.gamesheap.gamedetails.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import alektas.gamesheap.R
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.data.entities.getFullNames
import alektas.gamesheap.gamelist.ui.GamelistFragment
import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.item_game.item_name

private const val ARG_GAME_ID = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private var gameId: Long? = null
    private lateinit var viewModel: GameViewModel

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
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.game.observe(viewLifecycleOwner, Observer { bind(it) })

        gameId?.let { viewModel.fetchGame(it) }
    }

    private fun bind(game: GameInfo) {
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
