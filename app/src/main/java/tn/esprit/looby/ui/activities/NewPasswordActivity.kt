package tn.esprit.looby.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.UserService

class NewPasswordActivity : AppCompatActivity() {

    var passwordConfirmationTIET: TextInputEditText? = null
    var passwordTIET: TextInputEditText? = null
    var nextBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        passwordConfirmationTIET = findViewById(R.id.passwordConfirmationTIET)
        passwordTIET = findViewById(R.id.passwordTIET)
        nextBtn = findViewById(R.id.nextBtn)

        val email = intent.getStringExtra("EMAIL")

        nextBtn!!.setOnClickListener {
            if (
                (passwordConfirmationTIET!!.text.toString() == passwordConfirmationTIET!!.text.toString())
                && (passwordConfirmationTIET!!.text.toString() != "")
            ) {
                ApiService.userService.updatePassword(
                    UserService.UpdatePasswordBody(
                        email!!,
                        passwordTIET!!.text.toString()
                    )
                ).enqueue(
                    object : Callback<UserService.MessageResponse> {
                        override fun onResponse(
                            call: Call<UserService.MessageResponse>,
                            response: Response<UserService.MessageResponse>
                        ) {
                            if (response.code() == 200) {
                                Toast.makeText(
                                    baseContext,
                                    "Password changed successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                finish()
                            } else {
                                println("status code is " + response.code())
                            }
                        }

                        override fun onFailure(
                            call: Call<UserService.MessageResponse>,
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
}