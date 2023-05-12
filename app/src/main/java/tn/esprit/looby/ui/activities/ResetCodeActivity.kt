package tn.esprit.looby.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import tn.esprit.looby.R

class ResetCodeActivity : AppCompatActivity() {

    var resetCodeTIET: TextInputEditText? = null
    var nextBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_code)

        resetCodeTIET = findViewById(R.id.resetCodeTIET)
        nextBtn = findViewById(R.id.nextBtn)

        val resetCode = intent.getIntExtra("RESET_CODE", 0).toString()
        val email = intent.getStringExtra("EMAIL")

        nextBtn!!.setOnClickListener {
            if (resetCode == resetCodeTIET!!.text.toString()) {
                val intent = Intent(baseContext, NewPasswordActivity::class.java)
                intent.putExtra("EMAIL", email!!)
                startActivity(intent)

                finish()
            }
        }

    }
}