package alektas.gamesheap.common.data.entities

import alektas.gamesheap.utils.StringUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GameInfo {
    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("genres")
    @Expose
    var genres: List<Genre>? = null
    @SerializedName("platforms")
    @Expose
    var platforms: List<Platform>? = null
    @SerializedName("deck")
    @Expose
    var description: String? = null
    @SerializedName("developers")
    @Expose
    var developers: List<Developer>? = null
    @SerializedName("image")
    @Expose
    var image: Image? = null
    @SerializedName("expected_release_day")
    @Expose
    val releaseDay: Int? = null
    @SerializedName("expected_release_month")
    @Expose
    val releaseMonth: Int? = null
    @SerializedName("expected_release_year")
    @Expose
    val releaseYear: Int? = null
        get() {
            if (field == null && releaseDate != null) return StringUtils.parseYear(releaseDate)
            return field
        }
    @SerializedName("original_release_date")
    @Expose
    val releaseDate: String? = null

    override fun toString(): String {
        return "GameInfo(id=$id, name=$name, genres=$genres, platforms=$platforms, description=$description, developers=$developers, image=$image, releaseDay=$releaseDay, releaseMonth=$releaseMonth, releaseYear=$releaseYear, releaseDate=$releaseDate)"
    }
}