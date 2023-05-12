package tn.esprit.looby.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    val _id: String,
    val title: String,
    val description: String,
    val videoFilename: String,

    @SerializedName("user")
    val user: User?,
    @SerializedName("likes")
    val likes: List<Like>?,
    @SerializedName("comments")
    val comments: List<Comment>?
) : Serializable
