package tn.esprit.looby.service

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*
import tn.esprit.looby.models.Conversation
import tn.esprit.looby.models.Message
import tn.esprit.looby.models.MessageWithoutPopulate


interface ChatService {

    data class ConversationsResponse(
        @SerializedName("conversations")
        val conversations: List<Conversation>
    )

    data class ConversationResponse(
        @SerializedName("conversation")
        val conversation: Conversation
    )

    data class MessageResponse(
        @SerializedName("message")
        val message: String
    )

    data class MessagesResponse(
        @SerializedName("messages")
        val messages: List<MessageWithoutPopulate>
    )

    data class ConversationBody(val sender: String, val receiver: String)

    data class OneConversationBody(val conversation: String)

    data class MyConversationsBody(val sender: String)

    data class MessageBody(
        val description: String,
        val sender: String,
        val receiver: String
    )

    @POST("/chat/my-conversations")
    fun getMyConversations(@Body myConversationsBody: MyConversationsBody): Call<ConversationsResponse>

    @POST("/chat/my-messages")
    fun getMyMessages(@Body oneConversationBody: OneConversationBody): Call<MessagesResponse>

    @POST("/chat/creer-conversation")
    fun creerNouvelleConversation(@Body conversationBody: ConversationBody): Call<MessageResponse>

    @POST("/chat/envoyer-message")
    fun envoyerMessage(@Body messageBody: MessageBody): Call<MessageResponse>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/", hasBody = true)
    fun deleteConversation(@Field("_id") _id: String?): Call<MessageResponse?>?
}