package tn.esprit.looby.models

import java.io.Serializable
import java.util.*

data class User(
    val _id: String,
    val username: String,
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val birthdate: Date,
    val gender: String,
    val bio: String,
    val imageFilename: String,
    val role: String,
    val isVerified: Boolean
) : Serializable
