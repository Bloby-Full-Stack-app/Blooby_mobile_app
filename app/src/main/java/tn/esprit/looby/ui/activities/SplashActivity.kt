package tn.esprit.looby.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import tn.esprit.looby.utils.Constants

@SuppressLint("CustomSplashScreen")
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        checkUser()
    }

    private fun checkUser() {
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SESSION, MODE_PRIVATE)
        val userData = sharedPreferences.getString("USER_DATA", null)

        val intent: Intent = if (userData != null) {
            Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashActivity, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}