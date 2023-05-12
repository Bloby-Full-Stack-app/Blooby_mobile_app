package tn.esprit.looby.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.UserService
import tn.esprit.looby.utils.Constants

class LoginActivity : AppCompatActivity() {
    var signInButton: SignInButton? = null
    var emailid: TextInputEditText? = null
    var mdpid: TextInputEditText? = null
    var btncnx: Button? = null
    var btnforgetpassword: Button? = null
    var btnsignup: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInButton = findViewById(R.id.sign_in_button);
        signInButton!!.setSize(SignInButton.SIZE_STANDARD);
        emailid = findViewById(R.id.emailid)
        mdpid = findViewById(R.id.mdpid)
        btncnx = findViewById(R.id.btncnx)
        btnforgetpassword = findViewById(R.id.btnforgetpassword)
        btnsignup = findViewById(R.id.btnsignup)

        signInButton!!.setOnClickListener {

        }

        btncnx!!.setOnClickListener {
            login()
        }
        btnsignup!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnforgetpassword!!.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        //startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun login() {
        ApiService.userService.login(
            UserService.LoginBody(
                emailid!!.text.toString(),
                mdpid!!.text.toString()
            )
        ).enqueue(
            object : Callback<UserService.UserResponse> {
                override fun onResponse(
                    call: Call<UserService.UserResponse>,
                    response: Response<UserService.UserResponse>
                ) {
                    if (response.code() == 200) {
                        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SESSION, MODE_PRIVATE)
                        val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()
                        val json = Gson().toJson(response.body()!!.user)
                        sharedPreferencesEditor.putString("USER_DATA", json)
                        sharedPreferencesEditor.apply()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
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