package tn.esprit.looby.models

import java.io.Serializable
import java.util.*

data class Record(
    var locationName: String,
    var longitude: Double,
    var lattitude: Double,
    var date: Date,
    var user: User?
) : Serializable
