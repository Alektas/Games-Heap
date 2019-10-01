package alektas.gamesheap.ui.gamelist

import alektas.gamesheap.R
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.data.entities.getAbbreviations
import alektas.gamesheap.ui.ItemListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_game.view.*

class GamesAdapter(private val itemListener: ItemListener) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    var games: List<GameInfo> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bindItem(games[position])
        holder.itemView.setOnClickListener { games[position].id?.let { itemListener.onItemSelected(it) } }
    }

    override fun getItemCount(): Int = games.size

    override fun getItemId(position: Int): Long {
        return games[position].id ?: 0
    }

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.item_img
        val name: TextView = view.item_name
        val platforms: TextView = view.game_platforms
        val releaseDate: TextView = view.game_release_date

        fun bindItem(item: GameInfo?) {
            name.text = item?.name
            releaseDate.text = item?.releaseYear?.toString()
            platforms.text = item?.platforms?.getAbbreviations() ?: ""
            img.setImageResource(R.drawable.ic_image_black_24dp)
            Glide.with(itemView)
                .load(item?.image?.url)
                .optionalCenterCrop()
                .optionalFitCenter()
                .placeholder(R.drawable.ic_image_black_24dp)
                .thumbnail(0.1f)
                .into(img)
        }
    }
}