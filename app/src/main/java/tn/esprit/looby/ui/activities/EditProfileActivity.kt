package tn.esprit.looby.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.models.User
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.UserService
import tn.esprit.looby.utils.Constants
import tn.esprit.looby.utils.ImageLoader


class EditProfileActivity : AppCompatActivity() {


    var usernameid: TextInputEditText? = null
    var emailid: TextInputEditText? = null


    private var usernamelayout: TextInputLayout? = null
    private var emaillayout: TextInputLayout? = null

    var roundedimage: ImageView? = null

    //var mdpid: TextInputEditText? = null
    var btncnx: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //  usernamelayout = findViewById(R.id.username)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        usernameid = findViewById(R.id.usernameid)
        emailid = findViewById(R.id.emailid)

        btncnx = findViewById(R.id.btncnx)

        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SESSION, MODE_PRIVATE)
        val userData = sharedPreferences.getString("USER_DATA", null)




        roundedimage = findViewById(R.id.roundedimage)
        if (userData != null) {
            val user: User = Gson().fromJson(userData, User::class.java)
            //   fullName!!.text = user.firstname + " " + user.lastname
            //  email!!.text = user.email
            //image
            ImageLoader.setImageFromUrlWithoutProgress(
                roundedimage!!,
                Constants.BASE_URL_IMAGES + user.imageFilename
            )
        }



        btncnx!!.setOnClickListener {
            ApiService.userService.updateProfile(
                UserService.UserupdateBody(
                    emailid!!.text.toString(),
                    usernameid!!.text.toString()
                )
            ).enqueue(
                object : Callback<UserService.UserResponse> {
                    override fun onResponse(
                        call: Call<UserService.UserResponse>,
                        response: Response<UserService.UserResponse>
                    ) {
                        if (response.code() == 200) {
                            val intent =
                                Intent(this@EditProfileActivity, ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            println("status code is " + response.code())
                        }
                    }

                    override fun onFailure(
                        call: Call<UserService.UserResponse>,
                        t: Throwable
                    ) {
                        println("HTTP ERROR")
                        t.printStackTrace()
                        t.printStackTrace()
                    }
                }
            )
        }

    }
}