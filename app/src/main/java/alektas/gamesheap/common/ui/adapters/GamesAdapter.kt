package alektas.gamesheap.common.ui.adapters

import alektas.gamesheap.R
import alektas.gamesheap.data.entities.GameInfo
import alektas.gamesheap.data.entities.getAbbreviations
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_game.view.*
import kotlin.collections.ArrayList

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private val clickProducer = PublishSubject.create<Long>()
    val gameClicks: Observable<Long> = clickProducer
    var games: List<GameInfo> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface ItemListener {
        fun onItemSelected(id: Long)
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
        holder.itemView.clicks()
            .map { games[position].id }
            .subscribe(clickProducer)
    }

    override fun getItemCount(): Int = games.size

    override fun getItemId(position: Int): Long {
        return games[position].id ?: 0
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        clickProducer.onComplete()
        super.onDetachedFromRecyclerView(recyclerView)
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