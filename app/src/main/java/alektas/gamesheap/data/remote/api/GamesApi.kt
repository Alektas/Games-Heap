package alektas.gamesheap.data.remote.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GamesApi {

    /**
     * Filter should be like this: field:value,field:value
     * Platforms filter format should be like this: 94|145|146 (i.e PC and XONE and PS4)
     * Date filter format should be like this: 2019-01-01|2021-01-01 (i.e from the 2019 to the 2021 years)
     * If a filter is omitted then fetch games with default filters:
     * - Platforms: PC, PS4, XONE
     * - Release dates: 2019-2021
     */
    @GET("games/?format=json")
    fun fetchGames(
        @Query("api_key") key: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("sort") sort: String = "original_release_date:asc,name:asc",
        @Query("filter") filter: String =
            "original_release_date:2019-1-1 00:00:00|2022-1-1 00:00:00,platforms:94|145|146"
    ) : Single<GamesResponse>

    @GET("search/?format=json&resources=game")
    fun searchGames(
        @Query("api_key") key: String,
        @Query("limit") limit: Int = 100,
        @Query("query") query: String
    ) : Single<GamesResponse>

    @GET("game/{id}/?format=json")
    fun fetchGame(
        @Path("id") id: Long,
        @Query("api_key") key: String
    ): Single<GameResponse>
}