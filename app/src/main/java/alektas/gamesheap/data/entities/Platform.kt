package alektas.gamesheap.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Platform {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("abbreviation")
    @Expose
    var abbreviation: String? = null

    override fun toString(): String {
        return "$abbreviation"
    }

}

fun List<Platform>.getAbbreviations(): String =  this.joinToString(separator = " ") { it.abbreviation as CharSequence }

fun List<Platform>.getFullNames(): String =  this.joinToString(separator = ", ") { it.name as CharSequence }