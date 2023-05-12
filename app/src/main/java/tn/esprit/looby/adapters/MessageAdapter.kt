package tn.esprit.looby.adapters;

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import tn.esprit.looby.R
import tn.esprit.looby.models.Conversation
import tn.esprit.looby.models.MessageWithoutPopulate
import tn.esprit.looby.models.User
import tn.esprit.looby.utils.Constants


class MessageAdapter(var items: MutableList<MessageWithoutPopulate>, var conversation: Conversation) :
    RecyclerView.Adapter<MessageAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_message, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bindView(items[position], conversation)
    }

    override fun getItemCount(): Int = items.size

    class ConversationViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val lastMessageTV: TextView = itemView.findViewById(R.id.descriptionTV)
        private val profilePictureIV: ImageView = itemView.findViewById(R.id.profilePictureIV)
        private val userNameTV: TextView = itemView.findViewById(R.id.userNameTV)

        fun bindView(message: MessageWithoutPopulate, conversation: Conversation) {

            val sharedPreferences =
                itemView.context.getSharedPreferences(Constants.SHARED_PREF_SESSION, AppCompatActivity.MODE_PRIVATE)
            val userData = sharedPreferences.getString("USER_DATA", null)

            val sessionUser: User? = Gson().fromJson(userData, User::class.java)

            if (message.senderConversation!!.sender == sessionUser!!._id) {
                (itemView as LinearLayout).gravity = Gravity.END

                userNameTV.text = conversation.sender!!.username
                /*ImageLoader.setImageFromUrlWithoutProgress(
                    profilePictureIV, Constants.BASE_URL_IMAGES + conversation.sender.imageFilename
                )*/
            } else {
                userNameTV.text = conversation.receiver!!.username
                /*ImageLoader.setImageFromUrlWithoutProgress(
                    profilePictureIV, Constants.BASE_URL_IMAGES + conversation.receiver.imageFilename
                )*/

            }

            itemView.setOnClickListener {

            }

            lastMessageTV.text = message.description

        }
    }
}