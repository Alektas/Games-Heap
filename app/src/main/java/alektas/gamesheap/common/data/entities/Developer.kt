package alektas.gamesheap.common.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Developer {
    @SerializedName("name")
    @Expose
    var name: String? = null

    override fun toString(): String {
        return "Developer(name=$name)"
    }
}