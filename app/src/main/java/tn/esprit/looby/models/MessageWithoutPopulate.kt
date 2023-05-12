package tn.esprit.looby.models

import java.io.Serializable
import java.util.*

data class MessageWithoutPopulate(
    val _id: String,
    val description: String,
    val date: Date,
    val senderConversation: ConversationWithoutPopulate?,
    val receiverConversation: ConversationWithoutPopulate?
) : Serializable
