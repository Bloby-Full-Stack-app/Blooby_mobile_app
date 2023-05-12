package tn.esprit.looby.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.UserService


class RegisterActivity : AppCompatActivity() {

    var usernameid: TextInputEditText? = null
    var emailid: TextInputEditText? = null
    var mdpid: TextInputEditText? = null

    private var usernamelayout: TextInputLayout? = null
    private var emaillayout: TextInputLayout? = null
    private var mdplayout: TextInputLayout? = null


    //var mdpid: TextInputEditText? = null
    var btncnx: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //  usernamelayout = findViewById(R.id.username)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameid = findViewById(R.id.usernameid)
        emailid = findViewById(R.id.emailid)
        mdpid = findViewById(R.id.mdpid)
        btncnx = findViewById(R.id.btncnx)





        btncnx!!.setOnClickListener {
            ApiService.userService.register(
                UserService.UserBody(
                    emailid!!.text.toString(),
                    mdpid!!.text.toString(),
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
                                Intent(this@RegisterActivity, LoginActivity::class.java)
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
                    }
                }
            )
        }

    }
}