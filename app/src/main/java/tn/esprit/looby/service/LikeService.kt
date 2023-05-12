package tn.esprit.looby.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LikeService {

    data class LikeResponse(
        val message: String
    )

    data class LikeBody(val idPost: String, val idUser: String)

    @POST("/like")
    fun addOrRemoveLike(@Body likeBody: LikeBody): Call<LikeResponse>
}