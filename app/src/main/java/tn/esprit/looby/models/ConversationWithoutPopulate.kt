package tn.esprit.looby.models

import java.io.Serializable
import java.util.*

data class ConversationWithoutPopulate(
    val _id: String,
    val lastMessage: String,
    val lastMessageDate: Date,
    val sender: String?,
    val receiver: String?
) : Serializable
