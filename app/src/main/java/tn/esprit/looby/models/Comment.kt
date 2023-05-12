package tn.esprit.looby.models

import java.io.Serializable
import java.util.*

data class Comment(
    var date: Date,
    var description: String?,
    var idPost: String?,
    var idUser: String?
    //var username: String?,
    //var userPhoto: String?,
) : Serializable