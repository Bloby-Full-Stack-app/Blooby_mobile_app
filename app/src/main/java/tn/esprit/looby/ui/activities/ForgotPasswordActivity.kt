package tn.esprit.looby.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.UserService

class ForgotPasswordActivity : AppCompatActivity() {

    var emailTIET: TextInputEditText? = null
    var nextBtn: Button? = null
    var resetCode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        nextBtn = findViewById(R.id.nextBtn)
        emailTIET = findViewById(R.id.emailTIET)

        resetCode = (1000 until 9999).random()

        println("RESET CODE IS : $resetCode")

        nextBtn!!.setOnClickListener {

            ApiService.userService.forgotPassword(
                UserService.ResetBody(
                    resetCode.toString(),
                    emailTIET!!.text.toString()
                )
            ).enqueue(
                object : Callback<UserService.MessageResponse> {
                    override fun onResponse(
                        call: Call<UserService.MessageResponse>,
                        response: Response<UserService.MessageResponse>
                    ) {
                        if (response.code() == 200) {
                            val intent = Intent(baseContext, ResetCodeActivity::class.java)
                            intent.putExtra("RESET_CODE", resetCode)
                            intent.putExtra("EMAIL", emailTIET!!.text.toString())
                            startActivity(intent)

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