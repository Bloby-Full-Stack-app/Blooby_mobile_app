package tn.esprit.looby.models

import java.io.Serializable

data class Music(

    val _id: String,
    val title: String,
    val artist: String,
    val filename: String,
    val imageFilename: String
    //val likes: String,
) : Serializable
