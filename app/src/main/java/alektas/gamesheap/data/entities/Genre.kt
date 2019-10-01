package alektas.gamesheap.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Genre {
    @SerializedName("name")
    @Expose
    var name: String? = null

    override fun toString(): String {
        return "Genre(name=$name)"
    }
}