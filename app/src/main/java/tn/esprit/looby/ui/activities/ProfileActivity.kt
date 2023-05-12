package tn.esprit.looby.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.adapters.PostsGridAdapter
import tn.esprit.looby.models.Post
import tn.esprit.looby.models.User
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.ChatService
import tn.esprit.looby.service.PostService
import tn.esprit.looby.service.UserService
import tn.esprit.looby.utils.Constants
import tn.esprit.looby.utils.ImageLoader

class ProfileActivity : AppCompatActivity() {

    private var fullName: TextView? = null
    private var email: TextView? = null
    private var btnlogout: Button? = null
    private var btndelete: Button? = null
    private var postsGV: GridView? = null
    private var profileIV: ImageView? = null

    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        fullName = findViewById(R.id.fullName)
        email = findViewById(R.id.email)
        btnlogout = findViewById(R.id.btnlogout)
        btndelete = findViewById(R.id.delete)
        postsGV = findViewById(R.id.postsGV)
        profileIV = findViewById(R.id.profileIV)

        println(intent.dataString)
        var qrUserId  = intent.dataString

        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SESSION, MODE_PRIVATE)
        val userData = sharedPreferences.getString("USER_DATA", null)

        val sessionUser: User? = Gson().fromJson(userData, User::class.java)
        currentUser = intent.getSerializableExtra("user") as User?

        if (currentUser == null) {
            currentUser = sessionUser
        }


        if (currentUser != sessionUser) {
            btnlogout!!.text = "Block"
            btnlogout!!.setOnClickListener {
                Toast.makeText(applicationContext, "Feature coming soon", Toast.LENGTH_SHORT).show()
            }
            btndelete!!.text = "Contact"
            btndelete!!.setOnClickListener {
                ApiService.chatService.creerNouvelleConversation(
                    ChatService.ConversationBody(
                        sessionUser!!._id,
                        currentUser!!._id
                    )
                ).enqueue(
                    object : Callback<ChatService.MessageResponse> {
                        override fun onResponse(
                            call: Call<ChatService.MessageResponse>,
                            response: Response<ChatService.MessageResponse>
                        ) {
                            if (response.code() == 200) {
                                finish()
                            } else {
                                println("status code is " + response.code())
                            }
                        }

                        override fun onFailure(
                            call: Call<ChatService.MessageResponse>,
                            t: Throwable
                        ) {
                            println("HTTP ERROR : ")
                        }
                    }
                )
            }
        } else {
            btndelete!!.setOnClickListener {
                val intent =
                    Intent(this@ProfileActivity, EditProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        if (qrUserId != null) {
            qrUserId =qrUserId.replace("chicky://","")


            //411111111111111111111
            //ay esm
            //cvc 111
            ApiService.userService.getUser(
                UserService.OneUserBody(
                    qrUserId
                )
            ).enqueue(
                object : Callback<UserService.UserResponse> {
                    override fun onResponse(
                        call: Call<UserService.UserResponse>,
                        response: Response<UserService.UserResponse>
                    ) {
                        if (response.code() == 200) {
                            currentUser = response.body()?.user
                            initProfile()
                        } else {
                            Log.d("HTTP ERROR", "status code is " + response.code())
                        }
                    }

                    override fun onFailure(
                        call: Call<UserService.UserResponse>,
                        t: Throwable
                    ) {
                        Log.d("FAIL", "fail")
                    }
                }
            )
        }

        initProfile()
        getMyPosts()
    }

    fun initProfile(){
        fullName!!.text = currentUser!!.username
        email!!.text = currentUser!!.email

        ImageLoader.setImageFromUrlWithoutProgress(
            profileIV!!, Constants.BASE_URL_IMAGES +
                    currentUser!!.imageFilename
        )

        btnlogout!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.logout))
            builder.setMessage(R.string.logout_message)
            builder.setPositiveButton("Yes") { _, _ ->
                logout()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                dialogInterface.dismiss()
            }
            builder.create().show()
        }



        btndelete!!.setOnClickListener {
            val intent =
                Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun getQrCodeBitmap(): Bitmap {
        val size = 512 //pixels
        val i = packageManager.getLaunchIntentForPackage("tn.esprit.chicky")
        val qrCodeContent = "www.facebook.com"
        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 } // Make the QR code buffer border narrower
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }



    private fun getMyPosts() {
        ApiService.postService.getPosts()
            .enqueue(
                object : Callback<PostService.PostsResponse> {
                    override fun onResponse(
                        call: Call<PostService.PostsResponse>,
                        response: Response<PostService.PostsResponse>
                    ) {
                        if (response.code() == 200) {
                            val list: MutableList<Post> = emptyList<Post>().toMutableList()

                            for (post in response.body()?.posts as MutableList<Post>) {
                                if (post.user?._id == currentUser?._id) {
                                    list.add(post)
                                }
                            }

                            postsGV!!.adapter = PostsGridAdapter(
                                applicationContext,
                                list
                            )
                        } else {
                            println("status code is " + response.code())
                        }
                    }

                    override fun onFailure(
                        call: Call<PostService.PostsResponse>,
                        t: Throwable
                    ) {
                        println("HTTP ERROR : ")
                    }
                }
            )
    }



    private fun logout() {
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SESSION, MODE_PRIVATE)
        val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPreferencesEditor.clear().apply()

        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SESSION, MODE_PRIVATE)
            val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()
            val json = Gson().toJson(currentUser)
            sharedPreferencesEditor.putString("USER_DATA", json)
            sharedPreferencesEditor.apply()

            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return true
    }
}