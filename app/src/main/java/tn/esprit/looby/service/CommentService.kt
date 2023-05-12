package tn.esprit.looby.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CommentService {

    data class CommentResponse(
        val message: String
    )

    data class CommentBody(val idPost: String, val idUser: String, val description: String)

    @POST("/comment")
    fun addComment(@Body commentBody: CommentBody): Call<CommentResponse>
}