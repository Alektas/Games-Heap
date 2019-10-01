package alektas.gamesheap.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Image {
    @SerializedName("small_url")
    @Expose
    var url: String? = null

    override fun toString(): String {
        return "Image(url=$url)"
    }
}