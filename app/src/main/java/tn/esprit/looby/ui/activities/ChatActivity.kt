package tn.esprit.looby.ui.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.adapters.MessageAdapter
import tn.esprit.looby.models.Conversation
import tn.esprit.looby.models.MessageWithoutPopulate
import tn.esprit.looby.models.User
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.ChatService
import tn.esprit.looby.utils.Constants

class ChatActivity : AppCompatActivity() {

    var messagesList: MutableList<MessageWithoutPopulate> = mutableListOf()
    private var chatRV: RecyclerView? = null
    var addButton: Button? = null
    var messageTIET: TextInputEditText? = null

    private var currentConversation: Conversation? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRV = findViewById(R.id.chatRV)
        addButton = findViewById(R.id.addButton)
        messageTIET = findViewById(R.id.messageTIET)

        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SESSION, MODE_PRIVATE)
        val userData = sharedPreferences.getString("USER_DATA", null)

        val sessionUser: User? = Gson().fromJson(userData, User::class.java)
        currentConversation = intent.getSerializableExtra("conversation") as Conversation?
        val currentUser = currentConversation!!.receiver

        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        chatRV!!.layoutManager = linearLayoutManager
        chatRV!!.adapter = MessageAdapter(
            messagesList,
            currentConversation!!
        )

        getData()
        startUpdates()

        addButton!!.setOnClickListener {
            ApiService.chatService.envoyerMessage(
                ChatService.MessageBody(
                    messageTIET!!.text.toString(),
                    sessionUser!!._id,
                    currentUser!!._id
                )
            ).enqueue(object : Callback<ChatService.MessageResponse> {
                override fun onResponse(
                    call: Call<ChatService.MessageResponse>,
                    response: Response<ChatService.MessageResponse>
                ) {
                    if (response.code() == 200) {
                        messageTIET!!.setText("")
                    }
                }

                override fun onFailure(
                    call: Call<ChatService.MessageResponse>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                }
            })
        }
    }



    private fun getData() {
        ApiService.chatService.getMyMessages(
            ChatService.OneConversationBody(
                currentConversation!!._id,
            )
        ).enqueue(
            object : Callback<ChatService.MessagesResponse> {
                override fun onResponse(
                    call: Call<ChatService.MessagesResponse>,
                    response: Response<ChatService.MessagesResponse>
                ) {
                    if (response.code() == 200) {
                        messagesList = response.body()?.messages!! as MutableList<MessageWithoutPopulate>

                        chatRV!!.adapter = MessageAdapter(messagesList, currentConversation!!)
                        chatRV!!.scrollToPosition(messagesList.size - 1);

                    } else {
                        println("status code is " + response.code())
                    }
                }

                override fun onFailure(
                    call: Call<ChatService.MessagesResponse>,
                    t: Throwable
                ) {
                    println("HTTP ERROR")
                    t.printStackTrace()
                }
            }
        )
    }

    private val scope = MainScope() // could also use an other scope such as viewModelScope if available
    var job: Job? = null

    private fun startUpdates() {
        job = scope.launch {
            while (true) {
                getData() // the function that should be ran every second
                delay(2500)
            }
        }
    }

    private fun stopUpdates() {
        job?.cancel()
        job = null
    }

    override fun finish() {
        super.finish()
        stopUpdates()
    }
}