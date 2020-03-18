package alektas.gamesheap.common.data.remote.api

import alektas.gamesheap.common.data.entities.GameInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GamesResponse {
    @SerializedName("error")
    @Expose
    var error: String? = null
    @SerializedName("limit")
    @Expose
    var limit: Int? = null
    @SerializedName("offset")
    @Expose
    var offset: Int? = null
    @SerializedName("number_of_page_results")
    @Expose
    var numberOfPageResults: Int? = null
    @SerializedName("number_of_total_results")
    @Expose
    var numberOfTotalResults: Int? = null
    @SerializedName("status_code")
    @Expose
    var statusCode: Int? = null
    @SerializedName("results")
    @Expose
    var results: List<GameInfo>? = null

    override fun toString(): String {
        return "GameResponse(error=$error, limit=$limit, offset=$offset, numberOfPageResults=$numberOfPageResults, numberOfTotalResults=$numberOfTotalResults, statusCode=$statusCode, results=$results)"
    }

}