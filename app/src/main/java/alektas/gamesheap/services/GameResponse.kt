package alektas.gamesheap.services

import alektas.gamesheap.data.entities.GameInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GameResponse {
    @SerializedName("error")
    @Expose
    var error: String? = null
    @SerializedName("status_code")
    @Expose
    var statusCode: Int? = null
    @SerializedName("results")
    @Expose
    var results: GameInfo? = null

    override fun toString(): String {
        return "GameResponse(error=$error, statusCode=$statusCode, results=$results)"
    }

}